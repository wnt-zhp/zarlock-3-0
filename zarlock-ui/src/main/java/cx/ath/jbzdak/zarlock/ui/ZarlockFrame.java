package cx.ath.jbzdak.zarlock.ui;

import cx.ath.jbzdak.jpaGui.ui.tabbed.JBTabbedPane;

import javax.swing.*;
import java.awt.*;

import static cx.ath.jbzdak.zarlock.ui.DefaultIconManager.ICON_MANAGER;
import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getString;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-23
 */
public class ZarlockFrame extends JFrame{


   ZarlockModel zarlockModel = new ZarlockModel();

   private JBTabbedPane mainPanel = zarlockModel.getMainPanel();


   public ZarlockFrame() {
      initialize();
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }

   private void initialize(){
      setLayout(new BorderLayout());
      add(mainPanel, BorderLayout.CENTER);
      mainPanel.addTabCloseable(getString("product.tab.tabname"), ICON_MANAGER.getIcon("product"),
              zarlockModel.getProductTab(), getString("product.tab.tooltip"), false);
      mainPanel.addTabCloseable(getString("productList.tab.tabname"), ICON_MANAGER.getIcon("product.list"),
              zarlockModel.getProductList(), getString("productList.tab.tooltip"), false);
      mainPanel.addListener(zarlockModel.getProductList(), zarlockModel.getProductListListener());
   }

   public ZarlockModel getZarlockModel() {
      return zarlockModel;
   }
}
