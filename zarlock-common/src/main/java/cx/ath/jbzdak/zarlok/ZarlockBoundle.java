package cx.ath.jbzdak.zarlok;

import java.util.ResourceBundle;
import java.util.Locale;
import java.text.MessageFormat;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class ZarlockBoundle {

   static ResourceBundle zarlockBounle;

   public static String getString(String label){
      if(label!= null && zarlockBounle != null && zarlockBounle.containsKey(label)){
         return zarlockBounle.getString(label);
      }
      return label;
   }

    public static String getString(String label, Object... parameters){
       MessageFormat  format = new MessageFormat(getString(label));
       format.setLocale(Locale.getDefault());
       return format.format(parameters);
    }

   public static ResourceBundle getZarlockBounle() {
      return zarlockBounle;
   }

   static void setZarlockBounle(ResourceBundle zarlockBounle) {
      ZarlockBoundle.zarlockBounle = zarlockBounle;
   }

}
