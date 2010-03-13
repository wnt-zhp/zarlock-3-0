package cx.ath.jbzdak.zarlock.ui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 11, 2010
 */
public class ButtonPanel extends JPanel{

   public final JButton okButton; 

   public final JButton cancelButton;

   public ButtonPanel() {
      setLayout(new MigLayout("", "", "[fill, grow]"));
      okButton = ButtonFactory.createButton("Button.okButton");
      cancelButton = ButtonFactory.createButton("Button.cancelButton");
      add(okButton, "tag ok, h pref!");
      add(cancelButton, "tag cancel, h pref!");
   }
}
