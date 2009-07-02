package cx.ath.jbzdak.zarlok.ui.dzien;

import cx.ath.jbzdak.common.famfamicons.IconManager;
import cx.ath.jbzdak.jpaGui.Transaction;
import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.error.DisplayErrorDetailsDialog;
import cx.ath.jbzdak.jpaGui.ui.error.ErrorHandlers;
import cx.ath.jbzdak.zarlok.db.ZarlockDBManager;
import cx.ath.jbzdak.zarlok.db.tasks.UpdateIloscTeraz;
import cx.ath.jbzdak.zarlok.entities.Danie;
import cx.ath.jbzdak.zarlok.entities.Dzien;
import cx.ath.jbzdak.zarlok.entities.IloscOsob;
import cx.ath.jbzdak.zarlok.entities.Posilek;
import cx.ath.jbzdak.zarlok.raport.RaportException;
import cx.ath.jbzdak.zarlok.ui.iloscOsob.IloscOsobDialog;
import cx.ath.jbzdak.zarlok.ui.posilek.PosilekAddDialog;

import javax.persistence.EntityManager;
import javax.swing.*;
import java.awt.*;
import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TreePopupMenu extends JPopupMenu {
   private Dzien dzien;

   private Component invoker;

   private Posilek posilek;

   private Danie danie;

   private final DniTreePanelModel dniTreePanelModel;

   private final DBManager dbManager;

   boolean printEnable = Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.PRINT);

   private final JMenuItem makeZZMenuItem = new JMenuItem("Drukuj ZZ");
   private final JMenuItem printStanMagazynu = new JMenuItem("Drukuj stan magazynu");
   private final JMenuItem usunMenuItem = new JMenuItem("Usuń", IconManager.getIconSafe("cancel"));
   private final JMenuItem dodajPosilek = new JMenuItem("Dodaj posiłek");
   private final JMenuItem zmienIloscOsob = new JMenuItem("Zmien ilość osób");

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
      usunMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if (danie != null) {
               usunDanie(danie);
            } else if (posilek != null) {
               usunPosilek(posilek);
            } else if (dzien != null) {
               usunDzien(dzien);
            }
            try {
               new UpdateIloscTeraz().doTask((ZarlockDBManager) dbManager);
            } catch (Exception e1) {
               JOptionPane.showMessageDialog(TreePopupMenu.this, "Nie odświerzono ilości w magazynie po usunięciu. Zrestartuj program");
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
      zmienIloscOsob.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            IloscOsobDialog dialog = new IloscOsobDialog(SwingUtilities.getWindowAncestor(invoker));
            final IloscOsob ilosc = dzien.getIloscOsob();
            dialog.showDialog(ilosc);
            Transaction.execute(dbManager, new Transaction(){
               @Override
               public void doTransaction(EntityManager entityManager) throws Exception {
                  dzien.setIloscOsob(ilosc);
                  entityManager.merge(dzien);
               }
            });
         }
      });
      add(makeZZMenuItem);
      add(printStanMagazynu);
      add(dodajPosilek);
      add(zmienIloscOsob);
      addSeparator();
      add(usunMenuItem);
   }

   private void usunDzien(final Dzien d){
      int result = JOptionPane.showConfirmDialog(TreePopupMenu.this,
              PatternBeanFormatter.formatMessage(
                      "<html>Czy na pewno chcesz usunąć dzien {data}({#weekday,firstUppercase}{data}) " +
                              "spowoduje to nieodwracalną stratę zapisanych wyprowadzeń.</html>",dzien), "Pytanie", JOptionPane.YES_NO_OPTION);
      if(result == JOptionPane.YES_OPTION){
         Transaction.execute(dniTreePanelModel.getEntityManager(), new Transaction() {
            @Override
            public void doTransaction(EntityManager entityManager) throws Exception {
               dniTreePanelModel.removeDzien(d);
            }
         });
         dniTreePanelModel.generateTree();
      }
   }

   private void usunDanie(final Danie d){
      int result = JOptionPane.showConfirmDialog(TreePopupMenu.this,
              PatternBeanFormatter.formatMessage("<html> Czy na pewno chcesz usunąć to <strong>danie {nazwa}</strong>?<br/> Operacja jest nieodwracalna</html>", d),
              "Pytanie", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
      if(result == JOptionPane.YES_OPTION){
         Transaction.execute(dniTreePanelModel.getEntityManager(), new Transaction() {
            @Override
            public void doTransaction(EntityManager entityManager) throws Exception {
               danie = entityManager.find(Danie.class, danie.getId());
               entityManager.remove(danie);
               danie.getPosilek().getDania().remove(danie);

            }
         });
         dniTreePanelModel.generateTree();
      }
   }

   private void usunPosilek(final Posilek p){
          int result = JOptionPane.showConfirmDialog(TreePopupMenu.this,
                  PatternBeanFormatter.formatMessage("<html>" +
                    "Czy na pewno chcesz usunąć ten <strong>posiłek {nazwa}</strong>?<br/> Operacja jest nieodwracalna</html>", posilek),
                    "Pytanie", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(result == JOptionPane.YES_OPTION){
               Transaction.execute(dniTreePanelModel.getEntityManager(), new Transaction() {
                  @Override
                  public void doTransaction(EntityManager entityManager) throws Exception {
                     posilek = entityManager.find(Posilek.class,posilek.getId());
                     entityManager.remove(posilek);
                     posilek.getDzien().getPosilki().remove(posilek);
                  }
               });
               dniTreePanelModel.generateTree();
            }
   }

   @Override
   public void show(Component invoker, int x, int y) {
      this.invoker = invoker;
      makeZZMenuItem.setEnabled(false);
      printStanMagazynu.setEnabled(false);
      usunMenuItem.setEnabled(false);
      dodajPosilek.setEnabled(false);
      zmienIloscOsob.setEnabled(false);
      dzien = null;
      posilek = null;
      danie = null;
      if (dniTreePanelModel.selectedItems.size() == 1) {
         Object selected = dniTreePanelModel.selectedItems.get(0);
         if (selected instanceof Dzien) {
            this.dzien = (Dzien) selected;
            usunMenuItem.setEnabled(true);
            zmienIloscOsob.setEnabled(true);
         }
         if (selected instanceof Posilek){
            this.posilek = (Posilek) selected;
            this.dzien = posilek.getDzien();
            usunMenuItem.setEnabled(true);
         }
         if(selected instanceof Danie){
            this.danie = (Danie) selected;
            this.posilek = danie.getPosilek();
            this.dzien = posilek.getDzien();
            usunMenuItem.setEnabled(true);
         }
         makeZZMenuItem.setEnabled(true);
         printStanMagazynu.setEnabled(true);
         dodajPosilek.setEnabled(true);
         usunMenuItem.setEnabled(true);
      }
      super.show(invoker, x, y);
   }


}
