package cx.ath.jbzdak.zarlok;

import cx.ath.jbzdak.jpaGui.db.DBManager;

import javax.persistence.EntityManager;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-14
 */
public class DBManagerHolder {

   private static DBManager<EntityManager> DB_MANAGER;

   public static DBManager<EntityManager> getDBManager(){
      return DB_MANAGER;
   }
}
