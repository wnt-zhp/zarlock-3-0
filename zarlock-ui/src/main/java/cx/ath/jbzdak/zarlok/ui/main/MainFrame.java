package cx.ath.jbzdak.zarlok.ui.main;

import cx.ath.jbzdak.common.famfamicons.IconManager;
import cx.ath.jbzdak.jpaGui.Utils;
import static cx.ath.jbzdak.jpaGui.Utils.initLocation;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.Transaction;
import cx.ath.jbzdak.jpaGui.ui.error.DisplayErrorDetailsDialog;
import cx.ath.jbzdak.jpaGui.ui.error.ErrorHandlers;
import cx.ath.jbzdak.jpaGui.ui.tabbed.JBTabbedPane;
import cx.ath.jbzdak.zarlok.config.Preferences;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.Produkt;
import cx.ath.jbzdak.zarlok.main.MainWindowModel;
import cx.ath.jbzdak.zarlok.raport.RaportException;
import cx.ath.jbzdak.zarlok.ui.dzien.DniTab;
import cx.ath.jbzdak.zarlok.ui.opcje.OpcjeEditPanel;
import cx.ath.jbzdak.zarlok.ui.partia.PartiaAddDialog;
import cx.ath.jbzdak.zarlok.ui.partia.PartieListPanel;
import cx.ath.jbzdak.zarlok.ui.partia.StanMagazynuActionListener;
import cx.ath.jbzdak.zarlok.ui.partia.StanMagazynuPanel;
import cx.ath.jbzdak.zarlok.ui.produkt.ProductAddDialog;
import cx.ath.jbzdak.zarlok.ui.produkt.ProductEditPanel;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.swing.*;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainFrame extends JFrame {

   private static final long serialVersionUID = 1L;
   private static final Logger LOGGER = Utils.makeLogger();
   private JPanel jContentPane = null;
   private JToolBar jJToolBarBar = null;
   private JMenuItem jMenuItem = null;
   private MainWindowModel mainWindowModel = null;  //  @jve:decl-index=0:visual-constraint="607,13"
   private ProductAddDialog productAddDialog = null;  //  @jve:decl-index=0:visual-constraint="36,667"
   private JBTabbedPane jTabbedPane = null;
   private ProductEditPanel productEditPanel = null;
   private PartiaAddDialog partiaAddDialog = null;  //  @jve:decl-index=0:visual-constraint="982,695"
   private PartieListPanel partieListPanel = null;
   private JMenuItem jMenuItem1 = null;
   private StanMagazynuPanel partiePanel = null;
   private StanMagazynuActionListener partieActionListener = null;  //  @jve:decl-index=0:visual-constraint="214,32"
   private DniTab dniTab = null;
   private JMenuBar jJMenuBar = null;
   private JMenu jMenu = null;
   private JMenuItem resetDatabase = null;
   private JMenuItem jMenuItem2 = null;
   private JMenuItem backupMenuItem = null;
   private JMenuItem saveDatabaseMenuItem = null;
   private JMenuItem readBackupMenuItem = null;
   private JFileChooser oldDatabaseChooser = null;  //  @jve:decl-index=0:visual-constraint="103,192"
   private JMenu programMenu;
   private JMenu dokumentyMenu;
   private JMenuItem opcjeMenuItem;
   private JMenuItem pokazKatalogDokumentow;
   private JMenuItem generujKartoteki;
   private JMenuItem generujDokumentacje;


   public MainFrame(MainWindowModel mainWindowModel) throws HeadlessException {
      super();
      this.mainWindowModel = mainWindowModel;
      initialize();
   }

   /**
    * This method initializes this
    *
    * @return void
    */
   private void initialize() {
      this.setSize(800,600);
      this.setJMenuBar(getJJMenuBar());
      this.setContentPane(getJContentPane());
      this.setTitle("Żarłok 2.0.1 by JBzdak software");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      getProductAddDialog();
   }

   /**
    * This method initializes jContentPane
    *
    * @return javax.swing.JPanel
    */
   private JPanel getJContentPane() {
      if (jContentPane == null) {
         jContentPane = new JPanel();
         jContentPane.setLayout(new BorderLayout());
         jContentPane.add(getJJToolBarBar(), BorderLayout.NORTH);
         jContentPane.add(getJTabbedPane(), BorderLayout.CENTER);
      }
      return jContentPane;
   }

   /**
    * This method initializes jJToolBarBar
    *
    * @return javax.swing.JToolBar
    */
   private JToolBar getJJToolBarBar() {
      if (jJToolBarBar == null) {
         jJToolBarBar = new JToolBar();
         jJToolBarBar.setFloatable(false);
//			jJToolBarBar.add(getJMenuItem());
//			jJToolBarBar.add(getJMenuItem1());
      }
      return jJToolBarBar;
   }

   /**
    * This method initializes jMenuItem
    *
    * @return javax.swing.JMenuItem
    */
   private JMenuItem getJMenuItem() {
      if (jMenuItem == null) {
         jMenuItem = new JMenuItem();
         jMenuItem.setText("Produkty");
         jMenuItem.setIcon(IconManager.getScaled("application_add",1.25));
         jMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               getProductAddDialog().setProdukt(new Produkt());
               getProductAddDialog().startEditing();
               getProductAddDialog().setVisible(true);
            }
         });
      }
      return jMenuItem;
   }

   /**
    * This method initializes mainWindowModel
    *
    * @return cx.ath.jbzdak.zarlok.main.MainWindowModel
    */
   private MainWindowModel getMainWindowModel() {
      if (mainWindowModel == null) {
         mainWindowModel = new MainWindowModel();
      }
      return mainWindowModel;
   }

   /**
    * This method initializes productAddDialog
    *
    * @return cx.ath.jbzdak.zarlok.ui.produkt.ProductAddDialog
    */
   private ProductAddDialog getProductAddDialog() {
      if (productAddDialog == null) {
         productAddDialog = new ProductAddDialog(this);
         productAddDialog.setDBManager(mainWindowModel.getManager());
      }
      return productAddDialog;
   }

   /**
    * This method initializes jTabbedPane
    *
    * @return javax.swing.JTabbedPane
    */
   private JBTabbedPane getJTabbedPane() {
      if (jTabbedPane == null) {
         jTabbedPane = new JBTabbedPane();
         jTabbedPane.addTab("Produkt", IconManager.getIconSafe("book_add"), getProductEditPanel(), null);
         jTabbedPane.addTab("Stan magazynu", null, getStanMagazynuPanel(), null);
         jTabbedPane.addTab("Partie", null, getPartieListPanel(), null);
         jTabbedPane.addTab("Dni", null, getDniTab(), null);
         //jTabbedPane.addTabCloseable("XXX", IconManager.getIconSafe("accept"), new JPanel(), null, true);
         jTabbedPane.addListener(getStanMagazynuPanel(), getPartieActionListener());
         jTabbedPane.addListener(getPartieListPanel(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if("SELECTED".equals(e.getActionCommand())){
                  Transaction.execute(mainWindowModel.getManager(),new Transaction() {
                     @Override
                     public void doTransaction(EntityManager entityManager) throws Exception {
                        List<Partia> partie = entityManager.createQuery("SELECT p FROM Partia p").getResultList();
                        for (Partia partia : partie) {
                           partia.getWyprowadzenia();
                        }
                        getPartieListPanel().setPartie(partie);                        
                     }
                  });
               }
            }
         });
         //jTabbedPane.addTab(("Dni", null, )

      }
      return jTabbedPane;
   }

   /**
    * This method initializes productEditPanel
    *
    * @return cx.ath.jbzdak.zarlok.ui.produkt.ProductEditPanel
    */
   private ProductEditPanel getProductEditPanel() {
      if (productEditPanel == null) {
         productEditPanel = new ProductEditPanel(getManager());
      }
      return productEditPanel;
   }

   /**
    * This method initializes partiaAddDialog
    *
    * @return cx.ath.jbzdak.zarlok.ui.partia.PartiaAddDialog
    */
   private PartiaAddDialog getPartiaAddDialog() {
      if (partiaAddDialog == null) {
         partiaAddDialog = new PartiaAddDialog(this, getManager());
      }
      return partiaAddDialog;
   }

   private DBManager getManager(){
      return getMainWindowModel()==null?null:getMainWindowModel().getManager();
   }

   /**
    * This method initializes jMenuItem1
    *
    * @return javax.swing.JMenuItem
    */
   private JMenuItem getJMenuItem1() {
      if (jMenuItem1 == null) {
         jMenuItem1 = new JMenuItem();
         jMenuItem1.setText("Partie");
         jMenuItem1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               getPartiaAddDialog().getForm().setEntity(new Partia());
               getPartiaAddDialog().getForm().rollback();
               getPartiaAddDialog().setVisible(true);
            }

         });
      }
      return jMenuItem1;
   }

   /**
    * This method initializes partiePanel
    *
    * @return cx.ath.jbzdak.zarlok.ui.partia.PartiePanel
    */
   private StanMagazynuPanel getStanMagazynuPanel() {
      if (partiePanel == null) {
         partiePanel = new StanMagazynuPanel();
      }
      return partiePanel;
   }

   /**
    * This method initializes partieActionListener
    *
    * @return cx.ath.jbzdak.zarlok.ui.partia.PartieActionListener
    */
   private StanMagazynuActionListener getPartieActionListener() {
      if (partieActionListener == null) {
         partieActionListener = new StanMagazynuActionListener(getManager(), getStanMagazynuPanel());
      }
      return partieActionListener;
   }

   /**
    * This method initializes dniTab
    *
    * @return cx.ath.jbzdak.zarlok.ui.dzien.DniTab
    */
   private DniTab getDniTab() {
      if (dniTab == null) {
         dniTab = new DniTab(getMainWindowModel());
      }
      return dniTab;
   }

   /**
    * This method initializes jJMenuBar
    *
    * @return javax.swing.JMenuBar
    */
   private JMenuBar getJJMenuBar() {
      if (jJMenuBar == null) {
         jJMenuBar = new JMenuBar();
         jJMenuBar.setLayout(new MigLayout("", "[][][]:push[][][pref!, align right]"));
         jJMenuBar.add(getJMenu());
         jJMenuBar.add(getProgramMenu());
         jJMenuBar.add(getDokumentyMenu());
         jJMenuBar.add(getJMenuItem());
         jJMenuBar.add(getJMenuItem1());
         jJMenuBar.add(new JMenuItem("ver. 2.0.2"));
      }
      return jJMenuBar;
   }

   /**
    * This method initializes jMenu
    *
    * @return javax.swing.JMenu
    */
   private JMenu getJMenu() {
      if (jMenu == null) {
         jMenu = new JMenu();
         jMenu.setText("Baza danych");
         jMenu.add(getJMenuItem2());
         jMenu.add(getBackupMenuItem());
         jMenu.add(getReadBackupMenuItem());
         jMenu.add(getSaveDatabaseMenuItem());
         jMenu.add(getResetDatabase());
      }
      return jMenu;
   }

   /**
    * This method initializes jMenuItem2
    *
    * @return javax.swing.JMenuItem
    */
   private JMenuItem getJMenuItem2() {
      if (jMenuItem2 == null) {
         jMenuItem2 = new JMenuItem();
         jMenuItem2.setText("Importuj bazę starego Żarłoka");
         jMenuItem2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               if(getOldDatabaseChooser().showOpenDialog(MainFrame.this) == APPROVE_OPTION){
                  try{
                     getMainWindowModel().doImportFromOldDb(getOldDatabaseChooser().getSelectedFile());
                     JOptionPane.showMessageDialog(MainFrame.this, "Dało radę!");
                  }catch (Exception ex) {
                     DisplayErrorDetailsDialog dialog = new DisplayErrorDetailsDialog(MainFrame.this);
                     dialog.setText(ErrorHandlers.createLongHandlers().getHandler(ex).getMessage(ex));
                     dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                     initLocation(dialog);
                     dialog.setVisible(true);
                  }
               }
            }
         });

      }
      return jMenuItem2;
   }

   JMenuItem getBackupMenuItem() {
      if (backupMenuItem == null) {
         backupMenuItem = new JMenuItem("Utwórz backup bazy danych");
         backupMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_MASK));
         backupMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               getMainWindowModel().backup();
            }
         });
      }
      return backupMenuItem;
   }

   JMenuItem getReadBackupMenuItem() {
      if (readBackupMenuItem == null) {
         readBackupMenuItem = new JMenuItem("Odczytaj backup bazy danych");
         readBackupMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               getOldDatabaseChooser().setCurrentDirectory(new File (getMainWindowModel().getManager().getDatabaseBackupFolder()));
               if(APPROVE_OPTION == getOldDatabaseChooser().showOpenDialog(MainFrame.this)){
                  try {
                     getMainWindowModel().readBackup(getOldDatabaseChooser().getSelectedFile());
                  } catch (Exception e1) {
                     JOptionPane.showMessageDialog(MainFrame.this, "Nie udało się odtworzyć bazy danych" +
                             "lepiej zrestartuj program...", "Błąd", JOptionPane.ERROR_MESSAGE);
                  }
               }
            }
         });
      }
      return readBackupMenuItem;
   }

   JMenuItem getSaveDatabaseMenuItem() {
      if (saveDatabaseMenuItem == null) {
         saveDatabaseMenuItem = new JMenuItem("Zapisz bazę danych do. . .");
         saveDatabaseMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if(APPROVE_OPTION == getOldDatabaseChooser().showSaveDialog(MainFrame.this)){
                  try {
                     getMainWindowModel().backup(getOldDatabaseChooser().getSelectedFile());
                  } catch (Exception e1) {
                     JOptionPane.showMessageDialog(MainFrame.this, "Nie udało się odtworzyć bazy danych" +
                             "lepiej zrestartuj program...", "Błąd", JOptionPane.ERROR_MESSAGE);
                  }
               }
            }
         });
      }
      return saveDatabaseMenuItem;
   }

   /**
    * This method initializes oldDatabaseChooser
    *
    * @return javax.swing.JFileChooser
    */
   private JFileChooser getOldDatabaseChooser() {
      if (oldDatabaseChooser == null) {
         oldDatabaseChooser = new JFileChooser();
         oldDatabaseChooser.setFileHidingEnabled(false);
         oldDatabaseChooser.setFileFilter(new FileFilter(){

            @Override
            public boolean accept(File f) {
               return f.isDirectory() || f.getName().endsWith("zip");
            }

            @Override
            public String getDescription() {
               return "Pliki zip";
            }

         });
      }
      return oldDatabaseChooser;
   }

   JMenu getProgramMenu() {
      if (programMenu == null) {
        programMenu = new JMenu();
         programMenu.setText("Program");
         programMenu.add(getOpcjeMenuItem());
      }
      return programMenu;
   }

   JMenuItem getOpcjeMenuItem() {
      if (opcjeMenuItem == null) {
         opcjeMenuItem = new JMenuItem();
         opcjeMenuItem.setText("Zmień opcje");
         opcjeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               OpcjeEditPanel panel = new OpcjeEditPanel();
               jTabbedPane.addTabCloseable("Opcje", null, panel, "Panel pozwalający edytować opcje", true);
               panel.getOkButton().addActionListener(new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                     JOptionPane.showMessageDialog(MainFrame.this, "Zapisano");
                  }
               });
               jTabbedPane.setSelectedComponent(panel);
            }
         });
      }
      return opcjeMenuItem;
   }

   JMenu getDokumentyMenu() {
      if (dokumentyMenu == null) {
        dokumentyMenu = new JMenu("Dokumenty");
        if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)){
           dokumentyMenu.add(getPokazKatalogDokumentow());

        }
         dokumentyMenu.add(getGenerujKartoteki());
         dokumentyMenu.add(getGenerujDokumentacje());
      }
      return dokumentyMenu;
   }

   JMenuItem getPokazKatalogDokumentow() {
      if (pokazKatalogDokumentow == null) {
        pokazKatalogDokumentow = new JMenuItem("Pokaż katalog z dokumentami");
        pokazKatalogDokumentow.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
              if(Desktop.isDesktopSupported())
              if(Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)){
                 try {
                    Desktop.getDesktop().browse(Preferences.getDocumenrsFolder().toURI());
                 } catch (IOException e1) {
                    LOGGER.warn("Exception occoured", e1);

                 }
              }
           }
        });
      }
      return pokazKatalogDokumentow;
   }

   JMenuItem getGenerujDokumentacje() {
      if (generujDokumentacje == null) {
        generujDokumentacje = new JMenuItem("Twórz dokumentyację");
         generujDokumentacje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  try {
                  getMainWindowModel().getRaportFactory().saveDokumentacja();
               } catch (RaportException raportExceptionForUser) {
                  DisplayErrorDetailsDialog.showErrorDialog(raportExceptionForUser, MainFrame.this);
               }
            }
         });
      }
      return generujDokumentacje;
   }

   JMenuItem getGenerujKartoteki() {
      if (generujKartoteki == null) {
        generujKartoteki = new JMenuItem("Twórz kartoteki");
         generujKartoteki.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try {
                  getMainWindowModel().getRaportFactory().saveKartoteki();
               } catch (RaportException raportExceptionForUser) {
                  DisplayErrorDetailsDialog.showErrorDialog(raportExceptionForUser, MainFrame.this);
               }
            }
         });
      }
      return generujKartoteki;
   }

   JMenuItem getResetDatabase() {
      if (resetDatabase == null) {
        resetDatabase = new JMenuItem("Wyczyść bazę danych");
         resetDatabase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try {
                  getMainWindowModel().clearDatabase();
               } catch (Exception e1) {
                  DisplayErrorDetailsDialog.showErrorDialog(e1, MainFrame.this);
               }
            }
         });

      }
      return resetDatabase;
   }

   PartieListPanel getPartieListPanel() {
      if (partieListPanel == null) {
        partieListPanel = new PartieListPanel(getMainWindowModel());
      }
      return partieListPanel;
   }
}

