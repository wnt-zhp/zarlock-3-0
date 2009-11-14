package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.jpaGui.task.Task;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-14
 */
public class SchemaCreator extends AppLauncher{
   {
      INIT_TASKS.addTask(new Task<AppLauncher>(1, "OVERRIDE_PROPERTIES"){
         @Override
         public void doTask(@Nullable AppLauncher o, @Nullable Object[] o2) throws Exception {
            new LoadConfigTask("/zarlock-schemagen.properties").doTask(o, o2);
         }
      });
   }

   public static void main(String[] args){
      SchemaCreator creator = new SchemaCreator();
      creator.start();
      creator.dbLauncher.getDbManager().executeNativeStatement("SCRIPT TO '/home/jb/schema.sql'");
      System.exit(0);
   }

}
