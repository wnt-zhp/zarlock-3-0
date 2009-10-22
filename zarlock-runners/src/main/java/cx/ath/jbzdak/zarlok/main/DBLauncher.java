package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.jpaGui.Factory;
import cx.ath.jbzdak.jpaGui.db.DBLifecyclePhase;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.JpaDbManager;
import cx.ath.jbzdak.jpaGui.db.LifecycleManager;
import cx.ath.jbzdak.jpaGui.db.h2.DefaultH2Configuration;
import cx.ath.jbzdak.jpaGui.db.h2.H2Configuration;
import cx.ath.jbzdak.jpaGui.db.lifecycleListenerPack.CheckVersionListener;
import cx.ath.jbzdak.jpaGui.db.lifecycleListenerPack.DefaultUpdateDBPack;
import cx.ath.jbzdak.jpaGui.db.lifecycleListenerPack.InitializeDBPack;
import cx.ath.jbzdak.zarlok.ConfigHolder;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.EnumSet;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-15
 */
class DBLauncher implements LifecycleManager<DBManager<EntityManager>>{

   private final H2Configuration h2Configuration = new DefaultH2Configuration(false);

   DBLauncher() {
      h2Configuration.setDialect(H2Dialect.class.getCanonicalName());
      h2Configuration.setDatbaseUri(new File(ConfigHolder.getProperties().getProperty("file.db")).toURI());
      //h2Configuration.setSchemaAutoCreate(Hbm2ddl.valueOf(ConfigHolder.getProperties().getProperty("schemaAutoCreate")));
      h2Configuration.addListenerPack(new InitializeDBPack(new Factory<Reader>() {
         @Override
         public Reader make() {
            return new InputStreamReader(getClass().getResourceAsStream("/schema.sql"), Charset.forName("ASCII"));
         }
      }));
      h2Configuration.addListener(EnumSet.of(DBLifecyclePhase.PRE_START), new CheckVersionListener("SELECT ENTRY_VALUE FROM CONFIG_ENTRY WHERE ENTRY_NAME = 'DATABASE_VERSION' ") );
      if(ConfigHolder.getProperties().getProperty("dbVersions")!=null){
         String dbVersions =  ConfigHolder.getProperties().getProperty("dbVersions");
         h2Configuration.addListenerPack( new DefaultUpdateDBPack(dbVersions, "/updateSchemas"));
      }
   }

   public List mayGoToPhase(DBLifecyclePhase phase) {
      return h2Configuration.mayGoToPhase(phase);
   }

   public void startDB() throws Exception {
      h2Configuration.startDB();
   }

   public void backupDB(Object[] objects) throws Exception {
      h2Configuration.backupDB(objects);
   }

   public void readBackup(Object[] objects) throws Exception {
      h2Configuration.readBackup(objects);
   }

   public void closeDB() throws Exception {
      h2Configuration.closeDB();
   }

   public JpaDbManager getDbManager() {
      return h2Configuration.getDbManager();
   }
}
