package cx.ath.jbzdak.zarlock.ui.batch;

import java.util.Date;
import java.util.regex.Pattern;

import cx.ath.jbzdak.jpaGui.FormattingException;
import cx.ath.jbzdak.jpaGui.ParsingException;
import cx.ath.jbzdak.jpaGui.ui.form.FormElement;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.DateFormatter;
import cx.ath.jbzdak.zarlok.ZarlockBoundle;

public class ExpiryDateFormatter extends DateFormatter{

	public static final Pattern ignorePattern
		= Pattern.compile("\\s*(?:nieist(?:otna)?)|(?:olej)|(?:n\\.?\\s?dot\\.?)\\s*");

   FormElement<?, ?, ?> bookingDateElement;

   boolean wasDateSet;

   public void setBookingDateElement(FormElement<?, ?, ?> bookingDateElement) {
      this.bookingDateElement = bookingDateElement;
   }

   @Override
	public Date parseValue(String text) throws Exception {
      Date date =  parseText(text);
      if(bookingDateElement != null && bookingDateElement.getValue() != null){
         Date bookingDate = (Date) bookingDateElement.getValue();
         if(date!= null && date.before(bookingDate)){
            throw new ParsingException(ZarlockBoundle.getString("batch.exception.expiryDateBeforeBooking"));
         }
      }
      wasDateSet = true; 
      return date;
	}

   private Date parseText(String text) throws Exception {
      if(ignorePattern.matcher(text).matches()){
         return null;
      }
      return super.parseValue(text);
   }

   @Override
	public String formatValue(Object value) throws FormattingException {
		if(value==null){
			return "nieistotna";
		}
		return super.formatValue(value);
	}
}
