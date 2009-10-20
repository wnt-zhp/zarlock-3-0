package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.DBHolder;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-15
 */
class StartDBTask extends Task<AppLauncher>{
   public StartDBTask() {
      super(100, "START_DB");
   }

   @Override
   public void doTask(@Nullable AppLauncher o, @Nullable Object[] objects) throws Exception {
      DBHolder.setLifecycleManager(o.dbLauncher);
      DBHolder.getLifecycleManager().startDB();
   }
}
