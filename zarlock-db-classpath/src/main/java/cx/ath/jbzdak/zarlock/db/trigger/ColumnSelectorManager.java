package cx.ath.jbzdak.zarlock.db.trigger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.sql.*;


/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 13, 2010
 */
@ThreadSafe
public class ColumnSelectorManager {

   public static ColumnSelector createColumnSelector(Connection connection, String schema, String tableName) throws SQLException{
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      ResultSet resultSet = databaseMetaData.getColumns("", schema, tableName, null);
      Map<String, Integer> resultMap = new HashMap<String, Integer>();
      while(resultSet.next()){
         resultMap.put(resultSet.getString("COLUMN_NAME"), resultSet.getInt("ORDINAL_POSITION") - 1);
      }
      return new DefaultColumnSelector(resultMap);
   }

}
