package cx.ath.jbzdak.zarlock.ui.day.peopleNo;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import cx.ath.jbzdak.zarlock.ui.ButtonPanel;
import cx.ath.jbzdak.zarlok.entities.PeopleNo;
import cx.ath.jbzdak.jpaGui.ui.error.ErrorDetailsDialog;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 17, 2010
 */
public class PeopleNoDialog extends JDialog{
   private PeopleNoPanel noPanel;

   public PeopleNoDialog(Frame owner) {
      super(owner, true);
      setLayout(new MigLayout("fillx, filly, wrap 1", "[]", "[grow, fill][]"));
      ButtonPanel buttonPanel = new ButtonPanel("peopleNo.action.save");
      noPanel = new PeopleNoPanel();
      add(noPanel);
      add(buttonPanel);
      buttonPanel.cancelButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            setVisible(false);
            noPanel.getForm().rollback();
         }
      });
      buttonPanel.okButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               noPanel.getForm().commit();
               setVisible(false);
            } catch (Exception e1) {
               ErrorDetailsDialog.showErrorDialog(e, (Frame) getOwner());
            }
         }
      });
   }

   @Override
   public void setVisible(boolean b) {
      noPanel.getForm().startEditing();
      super.setVisible(b);
   }

   public void setBean(PeopleNo bean) {
      noPanel.setBean(bean);
   }

   public PeopleNo getBean() {
      return noPanel.getBean();
   }
}
