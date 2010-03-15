package cx.ath.jbzdak.zarlock.db.trigger;

import java.sql.*;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 14, 2010
 */
public class BatchUpdate extends DefaultTrigger2{

   @Override
   protected void init(Connection conn) { }

   @Override
   public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
      ColumnSelector s = getColumnSelector(conn);
      Long id = (Long) newRow[s.getOrdinalForName("ID")];
      CallableStatement statement = conn.prepareCall("SELECT SUM(QUANTITY) FROM EXPENDITURE WHERE BATCH_ID = ?");
      ResultSet resultSet = null;
      try{
         statement.setLong(1, id);
         statement.execute();
         resultSet = statement.getResultSet();
         resultSet.next();
         BigDecimal expenditured = resultSet.getBigDecimal(1);
         BigDecimal startQty = (BigDecimal) newRow[s.getOrdinalForName("START_QTY")];
         expenditured = expenditured!=null?expenditured:BigDecimal.ZERO;
         newRow[s.getOrdinalForName("CURRENT_QTY")] = startQty.subtract(expenditured, MathContext.DECIMAL32);
      }finally {
         if(resultSet!=null){
            resultSet.close();
         }
         statement.close();
      }
   }
}
