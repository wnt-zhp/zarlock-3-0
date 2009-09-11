package cx.ath.jbzdak.zarlok.db;

import cx.ath.jbzdak.jpaGui.Transaction;
import cx.ath.jbzdak.jpaGui.task.Task;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-06-08
 */
public class SetCollation extends Task<ZarlockDBManager> {
   public SetCollation() {
      super(-1000, "Ustaw kollacje"); 
   }

   @Override
   public void doTask(@Nullable ZarlockDBManager manager, @Nullable Object... o) throws Exception {
      Transaction.execute(manager, new Transaction() {
         @Override
         public void doTransaction(EntityManager entityManager) throws Exception {
            entityManager.createNativeQuery("SET DATABASE COLLATION \"Polish\"").executeUpdate();
         }
      });
   }
}
