package cx.ath.jbzdak.zarlock.ui.batch;

import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import cx.ath.jbzdak.zarlok.entities.Batch;
import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getString;
import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getZarlockBundle;
import cx.ath.jbzdak.jpaGui.ui.query.FulltextFilter;
import cx.ath.jbzdak.jpaGui.ui.query.FullTextPanel;
import cx.ath.jbzdak.jpaGui.ui.query.FullTextQuery;

import net.miginfocom.swing.MigLayout;

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
      JTableBinding binding = SwingBindings.createJTableBinding(UpdateStrategy.READ, this,
              BeanProperty.<BatchList, List<Batch>>create("bathes"), batchTable);
      binding.addColumnBinding(BeanProperty.create("fullName")).setColumnName(getString("batch.fullName")).setEditable(false);
      binding.addColumnBinding(BeanProperty.create("currentQty")).setColumnName(getString("batch.currentQty")).setEditable(false);
      binding.addColumnBinding(BeanProperty.create("unit")).setColumnName(getString("batch.unit")).setEditable(false);
      binding.addColumnBinding(BeanProperty.create("bookingDate")).setColumnName(getString("batch.bookingDate")).setEditable(false);
      binding.addColumnBinding(BeanProperty.create("expiryDate")).setColumnName(getString("batch.expiryDate")).setEditable(false);
      binding.bind();
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
      add(fullTextPanel);
      add(new JScrollPane(batchTable));
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
