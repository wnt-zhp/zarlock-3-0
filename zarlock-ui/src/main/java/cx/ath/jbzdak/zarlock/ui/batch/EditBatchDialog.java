package cx.ath.jbzdak.zarlock.ui.batch;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.db.dao.DAO;
import cx.ath.jbzdak.jpaGui.ui.error.ErrorDialog;
import cx.ath.jbzdak.zarlock.ui.ButtonPanel;
import cx.ath.jbzdak.zarlock.ui.ZarlockUtils;
import cx.ath.jbzdak.zarlok.entities.Batch;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 15, 2010
 */
public class EditBatchDialog extends JDialog{

   BatchBasicDataPanel dataPanel = new BatchBasicDataPanel();

   ButtonPanel buttonPanel = new ButtonPanel("batch.action.save");

   DAO<Batch> batchDAO;

   public EditBatchDialog(Frame owner) {
      super(owner, true);
      setLayout(new MigLayout("wrap 1, fillx", "[grow, fill]", "[grow, fill][]"));
      add(dataPanel);
      add(buttonPanel);
      buttonPanel.cancelButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dataPanel.getForm().rollback();
         }
      });
      buttonPanel.okButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try{
               dataPanel.getForm().commit();
            }catch (Exception e2){
               ErrorDialog.displayErrorDialog(Arrays.<Object>asList(e2), (Window)getOwner());
            }
            setVisible(false);
         }
      });
      batchDAO = ZarlockUtils.getZarlockModel(this).getDBManager().getDao(Batch.class);
      dataPanel.setDao(batchDAO);
   }

   public void setBatch(Batch batch) {
      batchDAO.setBean(batch);
   }

   @Override
   public void setVisible(boolean b) {
      if(b){
         dataPanel.getForm().startEditing();
         pack();
         setSize(getWidth(), getHeight() + 20); //Dialog wychodził za niski. . . HACK
         Utils.initLocation(this);
      }
      super.setVisible(b);
   }
}
