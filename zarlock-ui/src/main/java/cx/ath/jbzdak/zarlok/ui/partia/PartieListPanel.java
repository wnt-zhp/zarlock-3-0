package cx.ath.jbzdak.zarlok.ui.partia;

import cx.ath.jbzdak.common.famfamicons.IconManager;
import cx.ath.jbzdak.jpaGui.Transaction;
import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;
import cx.ath.jbzdak.jpaGui.db.dao.RefreshType;
import cx.ath.jbzdak.jpaGui.genericListeners.DoStuffMouseListener;
import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.jpaGui.ui.error.ErrorDialog;
import cx.ath.jbzdak.jpaGui.ui.form.DAOForm;
import cx.ath.jbzdak.jpaGui.ui.form.FormDialog;
import cx.ath.jbzdak.zarlok.db.dao.PartiaDAO;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.main.MainWindowModel;
import cx.ath.jbzdak.zarlok.ui.wyprowadzenie.WyprowadzenieEditTable;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-06-27
 */
public class PartieListPanel extends JPanel{

   private final MainWindowModel windowModel;

   private JTable table;

   private final PartiePanelFilter filter;
   
   private List<Partia> partie = Collections.emptyList();

   public PartieListPanel(MainWindowModel model) {
      super(new MigLayout("fill, wrap 1", "[fill, grow]", "[fill,grow][]"));
      windowModel = model;
      init();
      filter = new PartiePanelFilter(table);
      add(filter);
      add(new JScrollPane(table));
   }


   @SuppressWarnings({"RedundantTypeArguments"/*Inaczej się nie chce kompilować*/})
   private void init(){
      table = new JTable();
      JTableBinding tableBinding=SwingBindings.<Partia, PartieListPanel>createJTableBinding(AutoBinding.UpdateStrategy.READ, this,
              BeanProperty.<PartieListPanel, List<Partia>>create("partie"), table);
      tableBinding.addColumnBinding(ELProperty.create("${produkt.nazwa} - ${specyfikator}"))
              .setColumnName("Nazwa").setEditable(false);
      tableBinding.addColumnBinding(BeanProperty.create("jednostka")).setColumnName("Jednostka").setEditable(false);
      tableBinding.addColumnBinding(BeanProperty.create("dataKsiegowania")).setColumnName("Data księgowania")
              .setEditable(false);
      tableBinding.addColumnBinding(BeanProperty.create("dataWaznosci")).setColumnName("Data ważności")
              .setEditable(false);
      tableBinding.addColumnBinding(ELProperty.create("${iloscTeraz}[${iloscPocz}]")).setColumnName("Ilość w magazynie")
              .setEditable(false);
      tableBinding.addColumnBinding(BeanProperty.create("numerFaktury")).setColumnName("Numer faktury").setEditable(true);
      //tableBinding.addBindingListener(new DebugBindingListener());
      tableBinding.bind();
      table.setAutoCreateRowSorter(true);
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      table.addMouseListener(new DoStuffMouseListener(){
         final PopupMenu menu = new PopupMenu();
         {
            table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
               @Override
               public void valueChanged(ListSelectionEvent e) {
                  if(table.getSelectedRow()!=-1){
                     menu.editPartiaMenuItem.setEnabled(true);
                     menu.showWyprowadzeniaMenuItem.setEnabled(true);
                  }else{
                     menu.editPartiaMenuItem.setEnabled(false);
                     menu.showWyprowadzeniaMenuItem.setEnabled(false);
                  }
               }
            });
         }
         @Override
         protected void doStuff(MouseEvent e) {
            if(e.isPopupTrigger() && table.getSelectedRow()!=-1){
               menu.show(table, e.getX(), e.getY()); 
            }
         }
      });
   }

   public void setPartie(List<Partia> partie) {
      List<Partia> oldPartie = this.partie;
      this.partie = partie;
      if( ! (partie instanceof ObservableList)){
         this.partie = ObservableCollections.observableList(partie);
      }
      filter.setModel(partie);
      firePropertyChange("partie", oldPartie, this.partie);
   }

   public List<Partia> getPartie() {
      return partie;
   }

   private class PopupMenu extends JPopupMenu{



      final JMenuItem editPartiaMenuItem = new JMenuItem("Edytuj");

      final JMenuItem showWyprowadzeniaMenuItem = new JMenuItem("Podejrzyj wyprowadzenia ");

      final JMenuItem usunPartie = new JMenuItem("Usuń partię");

      PopupMenu(){

         editPartiaMenuItem.addActionListener(new ActionListener() {

            final PartiaEditPanel partiaPanel = new PartiaEditPanel(windowModel.getManager());

            final FormDialog dialog = new FormDialog(windowModel.getMainFrame(), "Edycja partii", true);
            {
               dialog.setForm(partiaPanel.getForm());
               partiaPanel.getForm().getDao().setRefreshType(RefreshType.MERGE);
               dialog.setRenderedComponent(partiaPanel);
               dialog.addOkActionTask(new Task<DAOForm>() {
                  @Override
                  public void doTask(@Nullable DAOForm daoForm, @Nullable Object... o) throws Exception {
                     if(daoForm == null){
                        throw new IllegalArgumentException();
                     }
                     if(!daoForm.checkErrors().isEmpty()){
                        ErrorDialog.displayErrorDialog(daoForm.checkErrors(), Utils.getFrameForComponent(dialog));
                        return;
                     }
                     daoForm.commit();                      
                     Partia p = (Partia) daoForm.getEntity();
                     p.recalculateIloscTeraz();
                     daoForm.getDao().persistOrUpdate();
                     daoForm.getDao().closeTransaction();
                     dialog.setFinishedSuccesfully(true);
                  }
               });
            }
            @Override
            public void actionPerformed(ActionEvent e) {
               partiaPanel.setPartia(getSelectedPartia());
               dialog.showDialog();
               if(dialog.isFinishedSuccesfully()){
                  setSelectedPartia(partiaPanel.getForm().getEntity());
               }
            }
         });
         showWyprowadzeniaMenuItem.addActionListener(new ActionListener() {

            final WyprowadzenieEditTable table;
            final JDialog dialog;

            {
               table = new WyprowadzenieEditTable(windowModel.getManager());
               dialog = new JDialog(windowModel.getMainFrame(), "Lista wyprowadzen", true);
               dialog.setLayout(new MigLayout("fill, wrap 1", "[fill, grow]", "[fill, grow|]"));
               JButton closeButton = new JButton("Zamknij", IconManager.getIconSafe("zamknij"));
               closeButton.addActionListener(new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                     dialog.setVisible(false);
                  }
               });
               dialog.add(new JScrollPane(table));
               dialog.add(closeButton);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
               Transaction.execute(windowModel.getManager(), new Transaction() {
                  @Override
                  public void doTransaction(EntityManager entityManager) throws Exception {
                     table.setEntities(entityManager.createQuery("SELECT w FROM Wyprowadzenie w WHERE w.partia = :partia").setParameter("partia", getSelectedPartia()).getResultList());
                  }
               });
               dialog.pack();
               Utils.initLocation(dialog);
               dialog.setVisible(true);
               Transaction.execute(windowModel.getManager(), new Transaction() {
                  @Override
                  public void doTransaction(EntityManager entityManager) throws Exception {
                     setSelectedPartia(entityManager.find(Partia.class, getSelectedPartia().getId()));
                  }
               });
            }
         });
         usunPartie.addActionListener(new ActionListener() {

            private PatternBeanFormatter formatter = new PatternBeanFormatter("<html>Czy na pewno chcesz usunąć" +
                    "partię <span style=\"font-weight: bold;\">{searchFormat}</span> ma ona: " +
                    "<span style=\"font-weight: bold;\">{1}</span> wyprowadzeń</html>");

            private PartiaDAO dao = new PartiaDAO(windowModel.getManager());
            @Override
            public void actionPerformed(ActionEvent e) {
               Partia p = getSelectedPartia();
               dao.beginTransaction();
               dao.setEntity(getSelectedPartia());
               if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(PartieListPanel.this, formatter.format(dao.getEntity(), dao.getEntity().getWyprowadzenia().size()))){
                  removePartia(p);
                  dao.setEntity(p);
                  dao.remove();
               }
               dao.closeTransaction();
            }

            private void removePartia(Partia p){
               int row = table.getSelectedRow();
               if(row == -1){
                  throw new IllegalStateException();
               }
               getPartie().remove(getSelectedPartia());
               //table.tableChanged(new TableModelEvent(table.getModel(), row, row, -1, TableModelEvent.DELETE));
            }
         });
         add(editPartiaMenuItem);
         add(showWyprowadzeniaMenuItem);
         add(usunPartie);
      }

      private Partia getSelectedPartia(){
         final int row = table.getSelectedRow();
         if(row == -1){
            return null;
         }
           return getPartie().get(table.convertRowIndexToModel(row));
      }

      private void setSelectedPartia(Partia p){
         int row = table.getSelectedRow();
         if(row == -1){
            throw new IllegalStateException();
         }
         getPartie().set(table.convertRowIndexToModel(row), p);
         table.tableChanged(new TableModelEvent(table.getModel(), row));
      }


   }
}
