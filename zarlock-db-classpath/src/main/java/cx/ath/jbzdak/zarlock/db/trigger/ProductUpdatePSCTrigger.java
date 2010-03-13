package cx.ath.jbzdak.zarlock.db.trigger;

import java.sql.*;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 13, 2010
 */
public class ProductUpdatePSCTrigger extends DefaultTrigger2{
   @Override
   protected void init(Connection conn) {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
      ColumnSelector columnSelector = getColumnSelector(conn);
      PreparedStatement statement = conn.prepareStatement("" +
              "SELECT COUNT(ID) FROM PRODUCT_SEARCH_CACHE WHERE PRODUCT_NAME = ? AND UNIT LIKE ?");
      try{
         String name = (String) newRow[columnSelector.getOrdinalForName("NAME")];
         statement.setString(1, name);
         String unit = (String) newRow[columnSelector.getOrdinalForName("UNIT")];
         if(unit==null || unit.length()==0){
            unit = "%";
         }
         statement.setString(2, unit);
         statement.execute();
         ResultSet resultSet = statement.getResultSet();
         resultSet.next();
         if(resultSet.getInt(1) == 0){
            PreparedStatement statement2 = conn.prepareStatement("" +
                    "INSERT INTO PRODUCT_SEARCH_CACHE(ID, PRODUCT_NAME, UNIT, PRODUCT_ID) " +
                    "VALUES (null, ?, ?, ?)");
            statement2.setString(1, name);
            statement2.setString(2, unit);
            Number id = (Number) newRow[columnSelector.getOrdinalForName("ID")];
            statement2.setInt(3, id.intValue());
            statement2.execute();
            statement2.close();
         }
      }finally {
         statement.close();
      }
   }
}
