package cx.ath.jbzdak.zarlok;

import java.util.Properties;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-15
 */
public class ConfigHolder {

   private static Properties PROPERTIES;

   public static Properties getProperties() {
      return PROPERTIES;
   }

   public static void setProperties(Properties PROPERTIES) {
      ConfigHolder.PROPERTIES = PROPERTIES;
   }
}
