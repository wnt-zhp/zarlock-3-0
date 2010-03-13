package cx.ath.jbzdak.zarlock.db.trigger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 13, 2010
 */
public abstract class DefaultTrigger2 extends DefaultTrigger{

   volatile ColumnSelector columnSelector;

   Lock lock = new ReentrantLock();

   public ColumnSelector getColumnSelector(Connection connection) throws SQLException{
      if (columnSelector == null) {
         lock.lock();
         synchronized (this){
            if(columnSelector == null){
               columnSelector = ColumnSelectorManager.createColumnSelector(connection, getSchemaName(), getTableName());
            }
         }
      }
      return columnSelector;
   }
}
