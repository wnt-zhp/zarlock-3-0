package cx.ath.jbzdak.zarlock.ui.batch;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;

import cx.ath.jbzdak.zarlok.entities.Expenditure;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 11, 2010
 */
public final class BatchUtils {

   public static BigDecimal countExpendituredQty(Collection<Expenditure> expenditures){
      BigDecimal amountExpenditured = BigDecimal.ZERO;
      for(Expenditure e : expenditures){
         amountExpenditured = amountExpenditured.add(e.getQuantity(), MathContext.DECIMAL32);
      }
      return amountExpenditured; 
   }
}
