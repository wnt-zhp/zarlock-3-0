package cx.ath.jbzdak.zarlock.ui;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import cx.ath.jbzdak.jpaGui.ui.tabbed.JBTabbedPane;

import static cx.ath.jbzdak.zarlock.ui.DefaultIconManager.ICON_MANAGER;
import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getString;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-23
 */
public class ZarlockFrame extends JFrame{


   ZarlockModel zarlockModel = new ZarlockModel(this);

   private JBTabbedPane mainPanel = zarlockModel.getMainPanel();

   private JFileChooser xmlFileChooser = new JFileChooser();
   {
      xmlFileChooser.setFileFilter(new FileFilter() {
         @Override
         public boolean accept(File f) {
            return f.isDirectory() || f.getName().endsWith("xml");
         }

         @Override
         public String getDescription() {
            return "xml";
         }
      });
   }

   public ZarlockFrame() {
      initialize();
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }

   private void initialize(){
      setLayout(new BorderLayout());
      add(mainPanel, BorderLayout.CENTER);
      initializeMenu();
      mainPanel.addTabCloseable(getString("product.tab.tabname"), ICON_MANAGER.getIcon("product"),
              zarlockModel.getProductTab(), getString("product.tab.tooltip"), false);
      mainPanel.addTabCloseable(getString("productList.tab.tabname"), ICON_MANAGER.getIcon("product.list"),
              zarlockModel.getProductList(), getString("productList.tab.tooltip"), false);
      mainPanel.addListener(zarlockModel.getProductList(), zarlockModel.getProductListListener());
   }

   void initializeMenu(){
      JMenuBar menuBar = new JMenuBar();
      {
         JMenu databaseMenu = new JMenu(getString("mainFrame.menu.databaseMenu.label"));
         databaseMenu.setIcon(ICON_MANAGER.getIcon("menu.database"));
         JMenuItem clearDB = new JMenuItem(getString("mainFrame.menu.databaseMenu.clearDB.label"));
         clearDB.setIcon(ICON_MANAGER.getIcon("menu.database.clear"));
         clearDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              zarlockModel.clearDB();
            }
         });
         databaseMenu.add(clearDB);
         JMenuItem readFromXML = new JMenuItem(getString("mainFrame.menu.databaseMenu.importXML.label"));
         readFromXML.setIcon(ICON_MANAGER.getIcon("menu.database.importXML"));
         readFromXML.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if(xmlFileChooser.showOpenDialog(ZarlockFrame.this)==JFileChooser.APPROVE_OPTION){
                  zarlockModel.loadFromXml(false, xmlFileChooser.getSelectedFile());
               }               
            }
         });
         databaseMenu.add(readFromXML);
         menuBar.add(databaseMenu);
         JMenuItem addBatch = new JMenuItem(getString("mainFrame.menu.addExpenditure.label"));
         addBatch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               zarlockModel.getBatchDialog().setVisible(true);
            }
         });
         menuBar.add(addBatch);
      }
      setJMenuBar(menuBar);
   }

   public ZarlockModel getZarlockModel() {
      return zarlockModel;
   }
}
