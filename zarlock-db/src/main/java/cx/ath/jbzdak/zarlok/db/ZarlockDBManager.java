package cx.ath.jbzdak.zarlok.db;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.db.JpaDbManager;
import cx.ath.jbzdak.zarlok.config.Preferences;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Manager bazy danych. Głównie zawiera różne propertiesy bazy danych oraz
 * po wystartowaniu umożliwia utworzenie {@link EntityManager}, przy pomocy
 * {@link EntityManagerFactory}.
 *
 * bazę danych otwiera się za pomocą {@link DBSetup} a zamyka za pomocą {@link DBClose}.
 *
 * @author jb
 *
 */
public class ZarlockDBManager extends JpaDbManager{

   private static final Logger LOGGER = Utils.makeLogger();

	private String databaseFolder = Preferences.getDatabaseFolderName();

	private String databasePrefix = Preferences.getDatabaseFilePrefix();

	private String databaseBackupFolder = Preferences.getDatabaseBackupFolder();

	private Map<String, String> propertiesOverride = new HashMap<String, String>();

	private EntityManagerFactory entityManagerFactory;

	private boolean databaseOpened;

	public String getDatabaseFolder() {
		return databaseFolder;
	}

	public void setDatabaseFolder(String databaseFolder) {
		checkDatabaseClosed();
		this.databaseFolder = databaseFolder;
	}

	public String getDatabasePrefix() {
		return databasePrefix;
	}

	public void setDatabaseBackupFolder(String databaseBackupFolder) {
		checkDatabaseClosed();
		this.databaseBackupFolder = databaseBackupFolder;
	}

	public void setDatabasePrefix(String databasePrefix) {
		checkDatabaseClosed();
		this.databasePrefix = databasePrefix;
	}

	public boolean isDatabaseOpened() {
		return databaseOpened;
	}

	private void checkDatabaseClosed(){
		if(databaseOpened){
			throw new IllegalStateException("This action is permissible only for closed DBManagers");
		}
	}

	public File getPropertiesFile(){
		return new File(databaseFolder, databasePrefix + ".properties");
	}

	public File getLogFile(){
		return new File(databaseFolder, databasePrefix + ".log");
	}

	public File getScriptFile(){
		return new File(databaseFolder, databasePrefix + ".script");
	}

	public File getBackupFile(){
		return new File(databaseFolder, databasePrefix + ".backup");
	}

	public File getDataFile(){
		return new File(databaseFolder, databasePrefix + ".data");
	}

   public File[] getDatabaseFiles(){
      File[] databaseFiles = new File[5];
      databaseFiles[0] = getPropertiesFile();
      databaseFiles[1] = getLogFile();
      databaseFiles[2] = getScriptFile();
      databaseFiles[3] = getBackupFile();
      databaseFiles[4] = getDataFile();
      return databaseFiles;
   }

	public String getDatabaseBackupFolder() {
		return databaseBackupFolder;
	}

	void setDatabaseOpened(boolean databaseOpened) {
		this.databaseOpened = databaseOpened;
	}

	public EntityManager createEntityManager(){
		return entityManagerFactory.createEntityManager();
	}

	EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	public Map<String, String> getPropertiesOverride() {
		return propertiesOverride;
	}

	public void setPropertiesOverride(Map<String, String> propertiesOverride) {
		checkDatabaseClosed();
		this.propertiesOverride = propertiesOverride;
	}

   String getDbLocation(){
		StringBuilder sbr = new StringBuilder();
		if(!StringUtils.isEmpty(getDatabaseFolder())){
			sbr.append(getDatabaseFolder());
			sbr.append("/");
		}
		sbr.append(getDatabasePrefix());
		return sbr.toString();
	}

   String getJDBCURL(){
      return "jdbc:hsqldb:file:" + getDbLocation();
   }

   void shutdown() throws SQLException {
      checkDatabaseClosed();
      sendStatement("SHUTDOWN SCRIPT;");
   }

   void sendStatement(String statementStr) throws SQLException {
      Connection connection = DriverManager.getConnection(getJDBCURL());
      LOGGER.info("Sending statement {} to {}", statementStr, getJDBCURL());
      try{
         Statement statement = connection.createStatement();
         try {
            statement.execute(statementStr);
            statement.close();
         } catch (SQLException e) {
           statement.close();
            LOGGER.error("Error while commiting statement",e);
         }
      }finally {
         connection.close();
      }
   }

   void checkpoint() throws SQLException {
      checkDatabaseClosed();
      sendStatement("CHECKPOINT DEFRAG;");
   }
}
