package cx.ath.jbzdak.zarlock.db.trigger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-21
 */
public class ConfigEntryTrigger extends DefaultTrigger{
   public ConfigEntryTrigger() {
   }

   @Override
   public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
      if(getTriggerType()!=INSERT){
         if(newRow[2]== null || !((Boolean) newRow[2])){
            throw new SQLException("Unsupported opperation", new SecurityException("Unsupported operation"));
         }
      }
   }

   @Override
   protected void init(Connection conn) {
      assertTableName("CONFIG_ENTRY");
   }
}
