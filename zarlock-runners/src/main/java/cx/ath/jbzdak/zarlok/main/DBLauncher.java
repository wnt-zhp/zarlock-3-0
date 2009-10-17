package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.jpaGui.db.DBLifecyclePhase;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.LifecycleManager;
import cx.ath.jbzdak.jpaGui.db.h2.DefaultH2Configuration;
import cx.ath.jbzdak.jpaGui.db.h2.H2Configuration;
import cx.ath.jbzdak.zarlok.ConfigHolder;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-15
 */
class DBLauncher implements LifecycleManager<DBManager<EntityManager>>{

   final H2Configuration h2Configuration = new DefaultH2Configuration(false);

   DBLauncher() {
      h2Configuration.setDatbaseUri(new File(ConfigHolder.getProperties().getProperty("file.db")).toURI());
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
}
