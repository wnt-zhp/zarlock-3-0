package cx.ath.jbzdak.zarlock;

import org.jdesktop.beansbinding.Converter;
import org.apache.commons.math.util.MathUtils;

import java.math.BigDecimal;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class RoundingConverter extends Converter<BigDecimal, BigDecimal>{
   @Override
   public BigDecimal convertForward(BigDecimal value) {
      return value.setScale(2, BigDecimal.ROUND_HALF_EVEN);
   }

   @Override
   public BigDecimal convertReverse(BigDecimal value) {
      return value.setScale(2, BigDecimal.ROUND_HALF_EVEN);
   }
}
