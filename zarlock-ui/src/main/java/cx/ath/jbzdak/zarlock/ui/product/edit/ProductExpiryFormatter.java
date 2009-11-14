package cx.ath.jbzdak.zarlock.ui.product.edit;

import cx.ath.jbzdak.jpaGui.Formatter;
import cx.ath.jbzdak.jpaGui.FormattingException;
import cx.ath.jbzdak.jpaGui.ParsingException;

import java.awt.event.ActionListener;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class ProductExpiryFormatter implements Formatter<Integer, Integer>{

   @Override
   public Integer parseValue(String text) throws Exception {
      if(text.trim().isEmpty()){
         return -1;
      }
      Integer integer = new Integer(text);
      if(integer < 0){
         throw new ParsingException("Data ważności musi być niezerowa. Pole może być puste");
      }
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public String formatValue(Integer value) throws FormattingException {
      if(value == -1){
         return "";
      }
      return value.toString();
   }

   @Override
   public void addFormatterChangedListener(ActionListener actionListener) {
   }

   @Override
   public void removeFormatterChangedListener(ActionListener actionListener) {
   }
}
