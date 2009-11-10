package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.zarlok.main.AppLauncher;
import cx.ath.jbzdak.jpaGui.task.Task;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-20
 */
public class TstAPPLauncher extends AppLauncher{
   {
      INIT_TASKS.addTask(new Task<AppLauncher>(1, "OVERRIDE_PROPERTIES"){
         @Override
         public void doTask(@Nullable AppLauncher o, @Nullable Object[] o2) throws Exception {
            new LoadConfigTask("/zarlock-test.properties").doTask(o, o2);
         }
      });
   }
}
