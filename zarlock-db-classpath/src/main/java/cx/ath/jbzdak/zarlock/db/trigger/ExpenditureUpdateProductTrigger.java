package cx.ath.jbzdak.zarlock.db.trigger;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-22
 */
public class ExpenditureUpdateProductTrigger extends DefaultTrigger{
   @Override
   protected void init(Connection conn) {
      assertTableName("EXPENDITURE");
   }

   @Override
   public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
      conn.setAutoCommit(false);
      try{
         if(getTriggerType() == UPDATE){
            if(oldRow[6] != newRow[6]){
               throw new SQLException("Can't change partia that is associated with wyprowadzenie", "ZARLOCK_ERR", 10);
            }
            if(oldRow[5] != newRow[5]){
               throw new SQLException("Can't change danie that is associated with wyprowadzenie", "ZARLOCK_ERR", 10);
            }
         }
         BigDecimal diff = BigDecimal.ZERO;
         Long id = null;
         if(oldRow!=null){
            diff = diff.add((BigDecimal) oldRow[3]);
            id = ((Number)oldRow[6]).longValue();
         }
         if(newRow!=null){
            diff = diff.subtract((BigDecimal) newRow[3]);
            id = ((Number)newRow[6]).longValue();
         }
         PreparedStatement s = conn.prepareStatement("UPDATE PARTIA SET ILOSC_TERAZ = ILOSC_TERAZ + ? WHERE PARTIA_ID = ?");
         try{
            s.setBigDecimal(2, diff);
            s.setLong(1, id);
         }finally {
            s.close();
         }
      }finally {
         conn.setAutoCommit(true);
      }
   }
}
