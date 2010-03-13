package cx.ath.jbzdak.zarlok;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.LifecycleManager;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-15
 */
public class DBHolder {

   public static DBManager getDbManager() {
      return LIFECYCLE_MANAGER.getDbManager();
   }


   private static LifecycleManager LIFECYCLE_MANAGER;

   public static LifecycleManager getLifecycleManager(){
      return  LIFECYCLE_MANAGER;
   }

   static void setLifecycleManager(LifecycleManager lifecycleManager){
      LIFECYCLE_MANAGER = lifecycleManager;
   }


}
