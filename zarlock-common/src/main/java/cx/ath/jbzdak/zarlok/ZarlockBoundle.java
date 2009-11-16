package cx.ath.jbzdak.zarlok;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class ZarlockBoundle {

   static ResourceBundle zarlockBundle;

   public static String getString(String label){
      if(label!= null && zarlockBundle != null && zarlockBundle.containsKey(label)){
         return zarlockBundle.getString(label);
      }
      return label;
   }

    public static String getString(String label, Object... parameters){
       MessageFormat  format = new MessageFormat(getString(label));
       format.setLocale(Locale.getDefault());
       return format.format(parameters);
    }

   public static ResourceBundle getZarlockBundle() {
      return zarlockBundle;
   }

   static void setZarlockBundle(ResourceBundle zarlockBounle) {
      ZarlockBoundle.zarlockBundle = zarlockBounle;
   }

}
