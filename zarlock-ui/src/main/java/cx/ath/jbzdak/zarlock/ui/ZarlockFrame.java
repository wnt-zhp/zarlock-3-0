package cx.ath.jbzdak.zarlock.ui;

import net.miginfocom.swing.MigLayout;
import net.miginfocom.layout.CC;

import javax.swing.*;

import cx.ath.jbzdak.jpaGui.ui.tabbed.JBTabbedPane;

import java.awt.*;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-23
 */
public class ZarlockFrame extends JFrame{

   private JBTabbedPane mainPanel = new JBTabbedPane();

   public ZarlockFrame() {
      initialize();
   }

   private void initialize(){
      setLayout(new BorderLayout());
      add(mainPanel, BorderLayout.CENTER);
   }
}
