package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.jpaGui.task.TasksExecutor;
import cx.ath.jbzdak.zarlok.db.DBBackup;
import cx.ath.jbzdak.zarlok.db.DBClose;
import cx.ath.jbzdak.zarlok.db.DBSetup;
import cx.ath.jbzdak.zarlok.db.ZarlockDBManager;
import cx.ath.jbzdak.zarlok.importer.Importer;
import cx.ath.jbzdak.zarlok.raport.RaportFactory;
import cx.ath.jbzdak.zarlok.ui.main.MainFrame;
import zzDataBase.LoadSaver;
import zzDataBase.StaticContent;
import zzEx.ZZIllegalEx;
import zzEx.ZZInternalDuplicateEntryEx;
import zzEx.ZZIoBadFormatEx;

import java.io.File;
import java.io.IOException;

/**
 * Model danych głównego okienka. Zawiera metody włączania i wyłączania aplikacji.
 * Żeby dodać taski do inicjalizacji patrz {@link MainWindowInitializer}. Dodawanie
 * tasków zamykania realizujemy przez {@link #getPostDBCloseTasks()} i {@link #getPreDBCloseTasks()}.
 *
 * Kolejność inicjalizacji
 *
 * preDBStartup:
 * 	-1000 ustawienie look and feel
 * @author jb
 *
 */
@SuppressWarnings({"UnusedDeclaration"})
public class MainWindowModel {

	private final TasksExecutor<MainWindowModel> preDBStartupTasks = new TasksExecutor<MainWindowModel>();

	private final TasksExecutor<MainWindowModel> postDBStartupTasks = new TasksExecutor<MainWindowModel>();

	private final TasksExecutor<MainWindowModel> preDBCloseTasks = new TasksExecutor<MainWindowModel>();

	private final TasksExecutor<MainWindowModel> postDBCloseTasks = new TasksExecutor<MainWindowModel>();

	private final DBSetup setup = new DBSetup();

	private final DBClose close = new DBClose();

   private final DBBackup backup = new DBBackup();
   {
      close.setBackup(backup);
   }

	private final ZarlockDBManager manager = new ZarlockDBManager();

   private final RaportFactory raportFactory = new RaportFactory();
   {
      raportFactory.setManager(manager);
   }

	MainFrame mainFrame;

	public void doImportFromOldDb(File file) throws ZZInternalDuplicateEntryEx, ZZIoBadFormatEx,  ZZIllegalEx, IOException{
		LoadSaver.load(StaticContent.sc, file);
		Importer.doImport(manager);
	}

	public void startApp() throws Exception{
		preDBStartupTasks.executeThrow(this);
		setup.setupDB(manager);
		postDBStartupTasks.executeThrow(this);
		mainFrame.setVisible(true);
	}

   public void clearDatabase() throws Exception{
      backup();
      close.stopDatabase(manager);
      for(File f : manager.getDatabaseFiles()){
         f.delete();
      }
      setup.setupDB(manager);
   }

	public void stopApp()throws Exception{
		preDBCloseTasks.executeThrow(this);
		close.stopDatabase(manager);
		postDBCloseTasks.executeThrow(this);
	}

   public void backup(){
      backup.doBackup(manager);
   }

    public void backup(File backupFile){
      backup.doBackup(manager, backupFile);
   }

   public void readBackup(File zip) throws Exception{
      try {
         close.stopDatabase(manager);
         backup.readFromBackup(zip, manager);
         setup.setupDB(manager);
      } finally {

      }
   }

	public TasksExecutor<MainWindowModel> getPreDBStartupTasks() {
		return preDBStartupTasks;
	}

	/**
	 * Obiekty otwierający bazę danych. Można modyfikować.
	 * @return
	 */
	public DBSetup getSetup() {
		return setup;
	}

	/**
	 * Obiekt zamykający bazę danych. W czasie inicjalizacji
	 * można go modyfikować.
	 */
	public DBClose getClose() {
		return close;
	}

	public ZarlockDBManager getManager() {
		return manager;
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public TasksExecutor<MainWindowModel> getPostDBStartupTasks() {
		return postDBStartupTasks;
	}

	/**
	 * Taski wykonywane podczas zamykania programu przed zamknięciem
	 * bazu danych.
	 * @return
	 */
	public TasksExecutor<MainWindowModel> getPreDBCloseTasks() {
		return preDBCloseTasks;
	}

	/**
	 * Taski wykonywane podczas zamykania programu po zamknięciu bazy danych.
	 * @return
	 */
	public TasksExecutor<MainWindowModel> getPostDBCloseTasks() {
		return postDBCloseTasks;
	}

   public RaportFactory getRaportFactory() {
      return raportFactory;
   }
}
