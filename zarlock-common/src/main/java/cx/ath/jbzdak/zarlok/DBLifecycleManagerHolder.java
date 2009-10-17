package cx.ath.jbzdak.zarlok;

import cx.ath.jbzdak.jpaGui.db.LifecycleManager;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-15
 */
public class DBLifecycleManagerHolder {

   private static LifecycleManager LIFECYCLE_MANAGER;

   public static LifecycleManager getLifecycleManager(){
      return  LIFECYCLE_MANAGER;
   }

   public static  void setLifecycleManager(LifecycleManager lifecycleManager){
      LIFECYCLE_MANAGER = lifecycleManager;
   }
}
