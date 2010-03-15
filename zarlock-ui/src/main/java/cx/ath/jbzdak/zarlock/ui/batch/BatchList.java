package cx.ath.jbzdak.zarlock.ui.batch;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import cx.ath.jbzdak.jpaGui.genericListeners.PopupMenuMouseListener;
import cx.ath.jbzdak.jpaGui.ui.query.FullTextPanel;
import cx.ath.jbzdak.jpaGui.ui.query.FullTextQuery;
import cx.ath.jbzdak.jpaGui.ui.query.FulltextFilter;
import cx.ath.jbzdak.zarlock.ui.ZarlockUtils;
import cx.ath.jbzdak.zarlok.ZarlockBoundle;
import cx.ath.jbzdak.zarlok.entities.Batch;

import static cx.ath.jbzdak.zarlock.ui.DefaultIconManager.ICON_MANAGER;
import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getString;
import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getZarlockBundle;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 14, 2010
 */
public class BatchList extends JPanel{


   JTable batchTable;

   List<Batch> bathes = new ArrayList<Batch>();

   FulltextFilter<TableModel, Integer> filter = new FulltextFilter<TableModel, Integer>(0,2);

   FullTextPanel fullTextPanel = new FullTextPanel(filter, getZarlockBundle());

   public BatchList() {
      super(new MigLayout("fillx, filly, wrap 1", "[grow, fill]", "[][grow, fill]"));
      batchTable = new JTable();
      initBinding();
      initRowSorter();
      initTable();
      initPopupMenu();
      add(fullTextPanel);
      add(new JScrollPane(batchTable));

   }

   private void initRowSorter() {
      final TableRowSorter rowSorter = new TableRowSorter(batchTable.getModel());
      rowSorter.setRowFilter(filter);
      filter.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if(FullTextQuery.QUERY_CHANGED_COMMAND.equals(e.getActionCommand())){
               rowSorter.sort();
            }
         }
      });

      batchTable.setRowSorter(rowSorter);
   }

   private void initTable() {
      batchTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer(){
         @Override
         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(value == null){
               value = ZarlockBoundle.getString("batch.expiryDate.notImportant");
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         }
      });
      batchTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
   }

   private void initBinding() {
      JTableBinding binding = SwingBindings.createJTableBinding(UpdateStrategy.READ, this,
              BeanProperty.<BatchList, List<Batch>>create("bathes"), batchTable);
      binding.addColumnBinding(BeanProperty.create("fullName")).setColumnName(getString("batch.fullName")).setEditable(false);
      binding.addColumnBinding(BeanProperty.create("currentQty")).setColumnName(getString("batch.currentQty")).setEditable(false);
      binding.addColumnBinding(BeanProperty.create("unit")).setColumnName(getString("batch.unit")).setEditable(false);
      binding.addColumnBinding(BeanProperty.create("bookingDate")).setColumnName(getString("batch.bookingDate")).setEditable(false);
      binding.addColumnBinding(BeanProperty.create("expiryDate")).setColumnName(getString("batch.expiryDate")).setEditable(false);
      binding.addColumnBinding(BeanProperty.create("fakturaNo")).setColumnName(getString("batch.fakturaNo")).setEditable(false);
      binding.bind();
   }

   private void  initPopupMenu(){
      JPopupMenu tableMenu = new JPopupMenu(getString("batchList.popupMenu.label"));
      JMenuItem jMenuItem = new JMenuItem(getString("batch.action.edit"), ICON_MANAGER.getIcon("batch.action.edit"));
      jMenuItem.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            int row = batchTable.getSelectedRow();
            if(row < 0){
               JOptionPane.showMessageDialog(BatchList.this,
                       getString("BatchList.popup.selectBatchFirst"),
                       getString("default.errorDialogTitle"), JOptionPane.INFORMATION_MESSAGE);
               return;
            }
            int batchIdx = batchTable.convertRowIndexToModel(row);
            EditBatchDialog dialog = ZarlockUtils.getZarlockModel(BatchList.this).getEditBatchDialog();
            dialog.setBatch(bathes.get(batchIdx));
            dialog.setVisible(true);
            setBathes(ZarlockUtils.getZarlockModel(BatchList.this).fetchBatches());
         }
      });
      tableMenu.add(jMenuItem);
      batchTable.addMouseListener(new PopupMenuMouseListener(tableMenu, batchTable));
   }

   public List<Batch> getBathes() {
      return bathes;
   }

   public void setBathes(List<Batch> bathes) {
      List<Batch> oldBathes = this.bathes;
      this.bathes = bathes;
      firePropertyChange("bathes", oldBathes, this.bathes);
   }


}
