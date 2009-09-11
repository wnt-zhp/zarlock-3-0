package cx.ath.jbzdak.zarlok.db;

import static cx.ath.jbzdak.jpaGui.Utils.makeLogger;
import cx.ath.jbzdak.jpaGui.db.DBState;
import cx.ath.jbzdak.jpaGui.task.TasksExecutor;
import cx.ath.jbzdak.zarlok.db.tasks.CleanImportTables;
import cx.ath.jbzdak.zarlok.db.tasks.DeleteProductSearchCache;
import javax.persistence.EntityManagerFactory;
import org.slf4j.Logger;

import java.sql.SQLException;


/**
 * Zamyka bazę dancyh.
 * @author jb
 *
 */
public class DBClose {

   private static final Logger logger = makeLogger();

   private ZarlockDBManager manager;

   private final TasksExecutor<ZarlockDBManager> preStopTasks = new TasksExecutor<ZarlockDBManager>();

   private final TasksExecutor<ZarlockDBManager> postStopTasks = new TasksExecutor<ZarlockDBManager>();

   private DBBackup backup;

   {
      preStopTasks.addTask(new DeleteProductSearchCache());
      preStopTasks.addTask(new CleanImportTables());
   }

   public DBClose() {
      super();
   }

   /**
    * Zamyka bazę danych. Najpierw wykonuje {@link #preStopTasks}.
    * Potem wyłącza {@link EntityManagerFactory}, następnie wykonuje
    * {@link #postStopTasks}.
    *
    * @throws Exception kiedy jakiś task coś rzuci w fazie preStop
    */
   public void stopDatabase(ZarlockDBManager manager) throws Exception{
      this.manager = manager;
      manager.fireStateWillChange(DBState.CLOSED);
      doPreStop();
      doStop();
      doPostStop();
   }

   private void doPreStop() throws Exception {
      logger.info("Doing pre stop tasks");

      preStopTasks.executeThrow(manager);
   }

   private void doStop() {
      logger.info("Stopping database");
      logger.info("Sending shutdown to db!");
      manager.getEntityManagerFactory().close();
      try {
         manager.shutdown();
      } catch (SQLException e) {
         logger.error("Exception while closing db", e);
      }
      backup.makeBackupCopy(manager);
      logger.info("Closed entityManagerFactory");
   }

   private void doPostStop() {
      logger.info("Doing post stop tasks");
      postStopTasks.executeSwallow(manager);
   }

   public void setBackup(DBBackup backup) {
      this.backup = backup;
   }
}
