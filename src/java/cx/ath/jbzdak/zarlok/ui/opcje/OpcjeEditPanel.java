package cx.ath.jbzdak.zarlok.ui.opcje;

import cx.ath.jbzdak.jpaGui.ui.form.FormElement;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanelConstraints;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanelMock;
import cx.ath.jbzdak.zarlok.config.PreferencesConfig;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-04-25
 */
public class OpcjeEditPanel extends JPanel{

   private OpcjeEditPanelModel model = new OpcjeEditPanelModel();

   private JButton okButton, cancelButton;

   public OpcjeEditPanel() {
      super(new MigLayout("wrap 1, fillx", "[fill]"));
      JPanel buttonPanel = new JPanel(new MigLayout());
      okButton = new JButton("OK");
      cancelButton = new JButton("Anuluj");
      buttonPanel.add(okButton, "tag ok");
      buttonPanel.add(cancelButton, "tag cancel");
      for(FormElement element : model.form.getForms()){
         FormPanelMock panel = new FormPanelMock(element, FormPanelConstraints.createCompactConstraints(),
                                                 PreferencesConfig.getResourceBundle());
         add(panel);
      }
      add(buttonPanel);
      initEvents();
      model.form.startEditing();
   }

   public void initEvents(){
      okButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            model.form.commit();

         }
      });
      cancelButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            model.form.rollback();
         }
      });
   }

   public JButton getOkButton() {
      return okButton;
   }

   public JButton getCancelButton() {
      return cancelButton;
   }
}
