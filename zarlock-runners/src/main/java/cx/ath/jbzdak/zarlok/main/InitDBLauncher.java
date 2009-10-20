package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.jpaGui.task.Task;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-20
 */
public class InitDBLauncher extends Task<AppLauncher> {
   public InitDBLauncher() {
      super(25, "INIT_DB_LAUNCHER");
   }

   @Override
   public void doTask(@Nullable AppLauncher appLauncher, @Nullable Object... o) throws Exception {
      appLauncher.dbLauncher=new DBLauncher();
   }
}
