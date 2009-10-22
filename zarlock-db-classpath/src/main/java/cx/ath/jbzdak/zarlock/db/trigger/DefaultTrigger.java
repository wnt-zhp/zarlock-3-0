package cx.ath.jbzdak.zarlock.db.trigger;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-21
 */
public abstract class DefaultTrigger implements Trigger{

   private String schemaName;

   private String tableName;

   private String triggerName;

   private boolean before;

   private int triggerType;

   @Override
   public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {
      this.schemaName = schemaName;
      this.triggerName = triggerName;
      this.tableName = tableName;
      this.before = before;
      this.triggerType = type;
      init(conn);
   }

   protected void assertTableName(String tableName){
      if(!this.tableName.equals(tableName)){
         throw new IllegalStateException();
      }
   }

   protected abstract void init(Connection conn);

   protected String getSchemaName() {
      return schemaName;
   }

   protected String getTableName() {
      return tableName;
   }

   protected String getTriggerName() {
      return triggerName;
   }

   protected boolean isBefore() {
      return before;
   }

   protected int getTriggerType() {
      return triggerType;
   }

}
