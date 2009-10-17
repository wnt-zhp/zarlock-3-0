package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.DBLifecycleManagerHolder;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-15
 */
class StartDBTask extends Task{
   public StartDBTask() {
      super(100, "START_DB");
   }

   @Override
   public void doTask(@Nullable Object o, @Nullable Object[] objects) throws Exception {
      DBLifecycleManagerHolder.setLifecycleManager(new DBLauncher());
      
      DBLifecycleManagerHolder.getLifecycleManager().startDB();
   }
}
