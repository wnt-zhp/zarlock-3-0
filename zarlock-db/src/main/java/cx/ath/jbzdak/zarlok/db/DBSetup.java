package cx.ath.jbzdak.zarlok.db;

import static cx.ath.jbzdak.jpaGui.Utils.makeLogger;
import cx.ath.jbzdak.jpaGui.db.DBState;
import cx.ath.jbzdak.jpaGui.task.TasksExecutor;
import cx.ath.jbzdak.zarlok.config.ConfigurationException;
import cx.ath.jbzdak.zarlok.db.tasks.*;
import cx.ath.jbzdak.zarlok.entities.listeners.PartiaSearchCacheUpdater;
import cx.ath.jbzdak.zarlok.entities.listeners.ProductSearchCacheUpdater;
import cx.ath.jbzdak.zarlok.ui.danie.DaniaPanelCache;
import org.slf4j.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Otwiera vazę danych. Konfiguracja polega na dodawaniu tasków do odpowiednich
 * {@link TasksExecutor}ów.
 *
 * @see DBSetup#setupDB(ZarlockDBManager)
 * @author jb
 *
 */
public class DBSetup {

	private static final Logger logger = makeLogger();

	private ZarlockDBManager manager;

	private boolean dbInitialized = true;

	private final TasksExecutor<ZarlockDBManager> preStartTasks = new TasksExecutor<ZarlockDBManager>();

	private final TasksExecutor<ZarlockDBManager> dbInitTasksBeforeStart = new TasksExecutor<ZarlockDBManager>();

	private final TasksExecutor<ZarlockDBManager> dbInitTasksAfterStart = new TasksExecutor<ZarlockDBManager>();

	private final TasksExecutor<ZarlockDBManager> dbSetup = new TasksExecutor<ZarlockDBManager>();

	{
		dbSetup.addTask(new DeleteProductSearchCache());
		dbSetup.addTask(new FillProductCacheTask());
		dbSetup.addTask(new InitListenerDbManager(ProductSearchCacheUpdater.class));
		dbSetup.addTask(new InitListenerDbManager(PartiaSearchCacheUpdater.class));
		dbSetup.addTask(new InitListenerDbManager(DaniaPanelCache.class));
		dbSetup.addTask(new UpdateIloscTeraz());
		dbSetup.addTask(new CleanImportTables());
      dbSetup.addTask(new UpdateKoszty());
      dbSetup.addTask(new SetCollation());
	}

	public DBSetup() {
		super();
	}

   /**
	 * Kolejność wykonywania tasków:
	 * <ul>
	 * <li> {@link #preStartTasks} </li>
	 * <li> Jeśli się okarze że pliki bazy danych nie istniały. To odpalamy {@link #dbInitTasksBeforeStart} </li>
	 * <li> Włącza bazę danych i inicjalizuje fabrykę encji </li>
	 * <li> Jeśli pliki bazy nie istnuiały - odpalamy {@link #dbInitTasksAfterStart} </li>
	 * <li> {@link #dbSetup()} - ustawia całą resztę bazy danych</li>
	 * </ul>
	 * @param manager
	 * @throws Exception
	 */
	public void setupDB(ZarlockDBManager manager) throws Exception {
		this.manager = manager;
		logger.info("Setting up db located in {}, dbfile prefix is {}",
				manager.getDatabaseFolder(), manager.getDatabasePrefix());
		preStartSetup();
		if (dbWasInitialized()) {
			initBeforeStartSetup();
		}
		dbStartTasks();
		if (dbWasInitialized()) {
			initTasksAfterStart();
		}
		dbSetup();
      manager.fireStateWillChange(DBState.OPEN);
	}

   private void initBeforeStartSetup() throws Exception {
		logger.info("Starting initialization before start setup phase");
		dbInitTasksBeforeStart.executeThrow(manager);
	}

	private void initTasksAfterStart() throws Exception {
		logger.info("Starting initialization after start setup phase");
		dbInitTasksAfterStart.executeThrow(manager);
	}




	private void dbStartTasks() {
		logger.info("Starting the database");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("zarlok-pu", createProperties());
		manager.setEntityManagerFactory(emf);
		logger.info("DB startet");
	}

	@SuppressWarnings({"WeakerAccess"})
   protected Map<String, String> createProperties(){

		Map<String, String> properties = new HashMap<String, String>();
		properties.put("hibernate.connection.url", manager.getJDBCURL());
		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.putAll(manager.getPropertiesOverride());
		return properties;
	}

	private void dbSetup() throws Exception{
		logger.info("Starting setting up the database for url {}", manager.getJDBCURL());
		dbSetup.executeThrow(manager);

	}

	private boolean dbWasInitialized() {
		return dbInitialized;
	}

	private void checkNotNull(Object o, String message){
		if(o==null){
			throw new IllegalStateException("Field named '" + message + "' is null");
		}
	}

	private void preStartCheckDBFiles() throws ConfigurationException{
		checkNotNull(manager.getDatabaseFolder(), "databaseFolder");
		checkNotNull(manager.getDatabasePrefix(), "databasePrefix");
		checkNotNull(manager.getDatabaseBackupFolder(), "databaseBackupFolder");
		File dbRoot = new File(manager.getDatabaseFolder());
		if(dbRoot.mkdirs()){
			logger.info("We needed to create root database dir");
		}
		if(!manager.getLogFile().exists() && manager.getScriptFile().exists()){
			dbInitialized= false;
		}
		File backup = new File(manager.getDatabaseBackupFolder());
		if(!backup.exists()){
			if(!backup.mkdirs()){
				throw new ConfigurationException("Nie udało się utworzyć katalogu w któym " +
						"przechowywane będą kopie zapasowe bazy danych. Folder to:" + backup.getAbsolutePath());
			}
		}
	}

	private void preStartSetup() throws Exception {
		logger.info("Starting db prestart setup");
		preStartCheckDBFiles();
		preStartTasks.executeThrow(manager);
	}

}
