package cx.ath.jbzdak.zarlock.ui.batch;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import cx.ath.jbzdak.jpaGui.db.dao.DAO;
import cx.ath.jbzdak.zarlock.ui.ButtonPanel;
import cx.ath.jbzdak.zarlock.ui.ZarlockModel;
import cx.ath.jbzdak.zarlock.ui.ZarlockUtils;
import cx.ath.jbzdak.zarlok.entities.Batch;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 11, 2010
 */
public class AddBatchDialog extends JDialog{

   AddBatchPanel addBatchPanel;
   
   public DAO<Batch> dao;


   public AddBatchDialog(Window owner) {
      super(owner);
      setModalityType(ModalityType.DOCUMENT_MODAL);
      setLayout(new MigLayout("wrap 1, fillx, filly","[fill, grow]", "[fill][pref]"));
      addBatchPanel = new AddBatchPanel();
      ButtonPanel buttonPanel = new ButtonPanel();
      add(addBatchPanel);
      add(buttonPanel, "h pref");
      ZarlockModel zarlockModel = ZarlockUtils.getZarlockModel(this);
      dao = zarlockModel.getDBManager().getDao(Batch.class);
      addBatchPanel.setDao(dao);
      dao.setAutoCreateEntity(true);
      buttonPanel.okButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            setVisible(false);
            addBatchPanel.getForm().commit();
         }
      });
      buttonPanel.cancelButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            setVisible(false);
            addBatchPanel.getForm().rollback();
         }
      });
   }

   @Override
   public void setVisible(boolean b) {
      pack();
      setSize(getWidth(), getHeight() + 20); //Dialog wychodził za niski. . . HACK
      super.setVisible(b);
   }
}
