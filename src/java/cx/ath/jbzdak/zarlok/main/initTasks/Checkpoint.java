package cx.ath.jbzdak.zarlok.main.initTasks;

import cx.ath.jbzdak.jpaGui.Transaction;
import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.main.MainWindowModel;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import org.slf4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-07-02
 */
public class Checkpoint extends Task<MainWindowModel>{

   public Checkpoint() {
      super(1000, "CHECKPOINT");
   }

   @Override
   public void doTask(final @Nullable MainWindowModel mainWindowModel, @Nullable Object... o) throws Exception {
      Timer timer = new Timer(true);

      long delay = 1000 * 60 * 15;
      timer.scheduleAtFixedRate(new TimerTask() {
         Logger log = Utils.makeLogger();
         @Override
         public void run() {
            Transaction.execute(mainWindowModel.getManager(), new Transaction() {
               @Override
               public void doTransaction(EntityManager entityManager) throws Exception {
                  try {
                     entityManager.createNativeQuery("CHECKPOINT;").executeUpdate();
                     log.info("Executing checkpoint");
                  } catch (Exception e) {
                     log.error("", e);
                  }
               }
            });

         }
      }, delay, delay);
   }
}
