package cx.ath.jbzdak.zarlok.ui.dzien;

import cx.ath.jbzdak.common.famfamicons.IconManager;
import cx.ath.jbzdak.jpaGui.Transaction;
import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.error.DisplayErrorDetailsDialog;
import cx.ath.jbzdak.jpaGui.ui.error.ErrorHandlers;
import cx.ath.jbzdak.zarlok.entities.Danie;
import cx.ath.jbzdak.zarlok.entities.Dzien;
import cx.ath.jbzdak.zarlok.entities.Posilek;
import cx.ath.jbzdak.zarlok.raport.RaportException;
import cx.ath.jbzdak.zarlok.ui.posilek.PosilekAddDialog;

import javax.persistence.EntityManager;
import javax.swing.*;
import java.awt.*;
import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TreePopupMenu extends JPopupMenu {
   private Dzien dzien;

   private Posilek posilek;

   private Danie danie;

   private final DniTreePanelModel dniTreePanelModel;

   private final DBManager dbManager;

   boolean printEnable = Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.PRINT);

   private final JMenuItem makeZZMenuItem = new JMenuItem("Drukuj ZZ");
   private final JMenuItem printStanMagazynu = new JMenuItem("Drukuj stan magazynu");
   private final JMenuItem usunDzienMenuItem = new JMenuItem("Usuń ten dzień", IconManager.getIconSafe("cancel"));
   private final JMenuItem usunPosilek = new JMenuItem("Usuń ten posilek", IconManager.getIconSafe("cancel"));
   private final JMenuItem usunDanie = new JMenuItem("Usun danie", IconManager.getIconSafe("cancel"));
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
               dniTreePanelModel.raportFactory.printZZ(dzien);
            } catch (RaportException re) {
               DisplayErrorDetailsDialog dialog = new DisplayErrorDetailsDialog();
               dialog.setText(ErrorHandlers.createLongHandlers().getHandler(re).getMessage(re));
               dialog.pack();
               Utils.initLocation(dialog);
               dialog.setVisible(true);
            }
         }
      });
      printStanMagazynu.addActionListener(new ActionListener(){
         @Override
         public void actionPerformed(ActionEvent e) {
            try{
               dniTreePanelModel.raportFactory.printStanMagazynu(dzien);
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
                                         formatter.format(dzien), "Pytanie", JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION){
               Transaction.execute(dniTreePanelModel.getEntityManager(), new Transaction() {
                  @Override
                  public void doTransaction(EntityManager entityManager) throws Exception {
                     dniTreePanelModel.removeDzien(dzien);
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
            dialog.setPosilek(new Posilek("", dzien));
            dialog.setVisible(true);
            dzien.getPosilki().add(dialog.getPosilek());
            Transaction.execute(dniTreePanelModel.getEntityManager(), new Transaction() {
               @Override
               public void doTransaction(EntityManager entityManager) throws Exception {
                  entityManager.merge(dzien);
               }
            });
            dniTreePanelModel.generateTree();
         }
      });
      usunPosilek.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showConfirmDialog(TreePopupMenu.this, "<html>" +
                    "Czy na pewno chcesz usunąć ten <strong>posiłek</strong>?<br/> Operacja jest nieodwracalna</html>",
                    "Pytanie", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(result == JOptionPane.YES_OPTION){
               Transaction.execute(dniTreePanelModel.getEntityManager(), new Transaction() {
                  @Override
                  public void doTransaction(EntityManager entityManager) throws Exception {
                     entityManager.remove(entityManager.merge(posilek));
                     posilek.getDzien().getPosilki().remove(posilek);

                  }
               });
               dniTreePanelModel.generateTree(); 
            }
         }
      });

      usunDanie.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showConfirmDialog(TreePopupMenu.this,
                     "<html> Czy na pewno chcesz usunąć to <strong>danie</strong>?<br/> Operacja jest nieodwracalna</html>",
                     "Pytanie", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(result == JOptionPane.YES_OPTION){
               Transaction.execute(dniTreePanelModel.getEntityManager(), new Transaction() {
                  @Override
                  public void doTransaction(EntityManager entityManager) throws Exception {
                     entityManager.remove(entityManager.merge(danie));
                     danie.getPosilek().getDania().remove(danie);
                     dniTreePanelModel.generateTree();
                  }
               });
            }
         }
      });

      add(makeZZMenuItem);
      add(printStanMagazynu);
      add(dodajPosilek);
      addSeparator();
      add(usunDzienMenuItem);
      add(usunPosilek);
      add(usunDanie);
   }

   @Override
   public void show(Component invoker, int x, int y) {
         makeZZMenuItem.setEnabled(false);
         printStanMagazynu.setEnabled(false);
         usunDzienMenuItem.setEnabled(false);
         dodajPosilek.setEnabled(false);
         usunDanie.setEnabled(false);
         usunPosilek.setEnabled(false);
         dzien = null;
         posilek = null;
            danie = null;
         if (dniTreePanelModel.selectedItems.size() == 1) {
            Object selected = dniTreePanelModel.selectedItems.get(0);
            if (selected instanceof Dzien) {
               this.dzien = (Dzien) selected;
               usunDzienMenuItem.setEnabled(true);
            }
            if (selected instanceof Posilek){
               this.posilek = (Posilek) selected;
               this.dzien = posilek.getDzien();
               usunPosilek.setEnabled(true);
            }
            if(selected instanceof Danie){
               this.danie = (Danie) selected;
               this.posilek = danie.getPosilek();
               this.dzien = posilek.getDzien();
               usunDanie.setEnabled(true);
            }
            makeZZMenuItem.setEnabled(true);
            printStanMagazynu.setEnabled(true);
            dodajPosilek.setEnabled(true);   
         }
      super.show(invoker, x, y);
   }


}
