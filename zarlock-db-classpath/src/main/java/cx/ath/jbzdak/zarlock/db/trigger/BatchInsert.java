package cx.ath.jbzdak.zarlock.db.trigger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 14, 2010
 */
public class BatchInsert extends DefaultTrigger2{

   @Override
   protected void init(Connection conn) { }

   @Override
   public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
      ColumnSelector columnSelector = getColumnSelector(conn);
      newRow[columnSelector.getOrdinalForName("CURRENT_QTY")]
              = newRow[columnSelector.getOrdinalForName("START_QTY")];
   }
}
