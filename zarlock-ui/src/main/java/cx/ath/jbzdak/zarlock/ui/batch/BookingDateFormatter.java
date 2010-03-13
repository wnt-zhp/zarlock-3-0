package cx.ath.jbzdak.zarlock.ui.batch;

import java.util.Calendar;
import java.util.Date;

import cx.ath.jbzdak.jpaGui.BeanHolder;
import cx.ath.jbzdak.jpaGui.BeanHolderAware;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.DateFormatter;
import cx.ath.jbzdak.jpaGui.ui.formatted.FormattedTextField;
import cx.ath.jbzdak.zarlok.entities.Batch;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 11, 2010
 */
public class BookingDateFormatter extends DateFormatter implements BeanHolderAware<Batch, BeanHolder<Batch>>{

   BeanHolder<Batch> beanHolder;

   FormattedTextField<Date> expiryField;

   ExpiryDateFormatter dateFormatter;

   @Override
   public Date parseValue(String text) throws Exception {
      Date date = super.parseValue(text);
      Batch batch = beanHolder.getBean();
      if(!dateFormatter.wasDateSet && batch.getProduct().getExpiryDate() != null){
         Calendar newExpiry = Calendar.getInstance();
         newExpiry.setTime(date);
         newExpiry.roll(Calendar.DAY_OF_MONTH, batch.getProduct().getExpiryDate());
         expiryField.setUserEnteredText(dateFormatter.formatValue(newExpiry.getTime()));
         expiryField.setValue(newExpiry.getTime());
         dateFormatter.wasDateSet = false;
      }
      return date;
   }

   public void setDateFormatter(ExpiryDateFormatter dateFormatter) {
      this.dateFormatter = dateFormatter;
   }

   public void setExpiryField(FormattedTextField expiryField) {
      this.expiryField = expiryField;
   }

   @Override
   public void setBeanHolder(BeanHolder<Batch> beanHolder) {
      this.beanHolder = beanHolder;
   }
//
//   @Override
//   public void setBeanHolder(BeanHolder<? extends Batch> beanHolder) {
//      this.beanHolder = beanHolder;
//   }
}
