package cx.ath.jbzdak.zarlock.ui.batch;

import java.math.BigDecimal;

import cx.ath.jbzdak.jpaGui.BeanHolder;
import cx.ath.jbzdak.jpaGui.BeanHolderAware;
import cx.ath.jbzdak.jpaGui.ParsingException;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.BigDecimalFormatter;
import cx.ath.jbzdak.zarlok.ZarlockBoundle;
import cx.ath.jbzdak.zarlok.entities.Batch;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 11, 2010
 */
public class StartQtyFormatter extends BigDecimalFormatter implements BeanHolderAware<Batch, BeanHolder<Batch>>{

   BeanHolder<Batch> beanHolder;

   public void setBeanHolder(BeanHolder<Batch> beanHolder) {
      this.beanHolder = beanHolder;
   }

   @Override
   public BigDecimal parseValue(String text) throws Exception {
      BigDecimal result = super.parseValue(text);
      Batch batch = beanHolder.getBean();
      if(batch == null || batch.getId() == null){
         return result;
      }
      BigDecimal amountExpenditured = BatchUtils.countExpendituredQty(batch.getExpenditures());
      if(amountExpenditured.compareTo(result) > 1){
         throw new ParsingException(ZarlockBoundle.getString("batch.exception.startQtyTooLow"));
      }
      return result;
   }
}
