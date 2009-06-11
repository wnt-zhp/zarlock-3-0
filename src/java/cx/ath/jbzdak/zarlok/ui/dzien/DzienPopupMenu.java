package cx.ath.jbzdak.zarlok.ui.dzien;

import cx.ath.jbzdak.jpaGui.Transaction;
import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.error.DisplayErrorDetailsDialog;
import cx.ath.jbzdak.jpaGui.ui.error.ErrorHandlers;
import cx.ath.jbzdak.zarlok.entities.Dzien;
import cx.ath.jbzdak.zarlok.entities.Posilek;
import cx.ath.jbzdak.zarlok.raport.RaportException;
import cx.ath.jbzdak.zarlok.ui.posilek.PosilekAddDialog;
import javax.persistence.EntityManager;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TreePopupMenu extends JPopupMenu {
   private Dzien d;

   private final DniTreePanelModel dniTreePanelModel;

   private final DBManager dbManager;

   boolean printEnable = Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.PRINT);


   private final JMenuItem makeZZMenuItem = new JMenuItem("Drukuj ZZ");
   private final JMenuItem saveZZMenuItem = new JMenuItem("Zapisz ZZ");
   private final JMenuItem saveStanMenuItem = new JMenuItem("Zapisz stan magazynu");
   private final JMenuItem printStanMagazynu = new JMenuItem("Drukuj stan magazynu");
   private final JMenuItem usunDzienMenuItem = new JMenuItem("Usuń ten dzień");
   private final JMenuItem dodajPosilek = new JMenuItem("Dodaj posiłek");

   TreePopupMenu(DBManager dbManager, DniTreePanelModel dniTreePanelModel) {
      this.dbManager = dbManager;
      this.dniTreePanelModel = dniTreePanelModel;
   }

   TreePopupMenu(String label, DBManager dbManager, DniTreePanelModel dniTreePanelModel) {
      super(label);
      this.dbManager = dbManager;
      this.dniTreePanelModel = dniTreePanelModel;
   }

   {
      makeZZMenuItem.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               dniTreePanelModel.raportFactory.printZZ(d);
            } catch (RaportException re) {
               DisplayErrorDetailsDialog dialog = new DisplayErrorDetailsDialog();
               dialog.setText(ErrorHandlers.createLongHandlers().getHandler(re).getMessage(re));
               dialog.pack();
               Utils.initLocation(dialog);
               dialog.setVisible(true);
            }
         }
      });
      saveZZMenuItem.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               dniTreePanelModel.raportFactory.saveZZ(d);
            } catch (RaportException re) {
               DisplayErrorDetailsDialog dialog = new DisplayErrorDetailsDialog();
               dialog.setText(ErrorHandlers.createLongHandlers().getHandler(re).getMessage(re));
               dialog.pack();
               Utils.initLocation(dialog);
               dialog.setVisible(true);
            }
         }
      });
      saveStanMenuItem.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try{
               dniTreePanelModel.raportFactory.saveStanMagazynu(d);
            }catch (RaportException re){
               DisplayErrorDetailsDialog.showErrorDialog(re, null);
            }
         }
      });
      printStanMagazynu.addActionListener(new ActionListener(){
         @Override
         public void actionPerformed(ActionEvent e) {
            try{
               dniTreePanelModel.raportFactory.printStanMagazynu(d);
            }catch (RaportException re){
               DisplayErrorDetailsDialog.showErrorDialog(re, null);
            }
         }
      });
      usunDzienMenuItem.addActionListener(new ActionListener() {
         PatternBeanFormatter formatter = new PatternBeanFormatter(
                 "<html>Czy na pewno chcesz usunąć dzien {data}({#weekday,firstUppercase}{data}) spowoduje to " +
                         "nieodwracalną stratę zapisanych wyprowadzeń.</html>"
         );

         public void actionPerformed(ActionEvent e) {

            int result = JOptionPane.showConfirmDialog(TreePopupMenu.this,
                                         formatter.format(d), "Pytanie", JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION){
               Transaction.execute(dbManager, new Transaction() {
                  @Override
                  public void doTransaction(EntityManager entityManager) throws Exception {
                     dniTreePanelModel.removeDzien(d);
                  }
               });
            }
         }
      });
      dodajPosilek.addActionListener(new ActionListener() {

         PosilekAddDialog dialog;

         public void actionPerformed(ActionEvent e) {
            if (dialog == null) {
               dialog = new PosilekAddDialog(SwingUtilities.getWindowAncestor(TreePopupMenu.this),dbManager);
            }
            dialog.setPosilek(new Posilek("", d));
            dialog.setVisible(true);
            d.getPosilki().add(dialog.getPosilek());
            Transaction.execute(dbManager, new Transaction() {
               @Override
               public void doTransaction(EntityManager entityManager) throws Exception {
                  entityManager.merge(d);
               }
            });
            dniTreePanelModel.generateTree();
         }
      });
      add(makeZZMenuItem);
      add(saveZZMenuItem);
      add(printStanMagazynu);
      add(saveStanMenuItem);
      add(dodajPosilek);
      addSeparator();
      add(usunDzienMenuItem);
   }

   @Override
   public void show(Component invoker, int x, int y) {
      if (dniTreePanelModel.selectedItems.size() == 1 && dniTreePanelModel.onlyDniSelected) {
         makeZZMenuItem.setEnabled(true);
         saveZZMenuItem.setEnabled(true);
         printStanMagazynu.setEnabled(true);
         saveStanMenuItem.setEnabled(true);
         usunDzienMenuItem.setEnabled(true);
         d = (Dzien) dniTreePanelModel.selectedItems.get(0);
      } else if(dniTreePanelModel.selectedItems.size() == 1 && dniTreePanelModel.onlyPosilkiSelected){
         makeZZMenuItem.setEnabled(false);
         saveZZMenuItem.setEnabled(false);
         printStanMagazynu.setEnabled(false);
         saveStanMenuItem.setEnabled(false);
         usunDzienMenuItem.setEnabled(false);
         dodajPosilek.setEnabled(true);
         d = ((Posilek) dniTreePanelModel.selectedItems.get(0)).getDzien();
      }else{
         makeZZMenuItem.setEnabled(false);
         saveZZMenuItem.setEnabled(false);
         printStanMagazynu.setEnabled(false);
         saveStanMenuItem.setEnabled(false);
         usunDzienMenuItem.setEnabled(false);
         dodajPosilek.setEnabled(false);
         d = null;
      }
      super.show(invoker, x, y);
   }
}
