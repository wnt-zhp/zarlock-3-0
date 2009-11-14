package cx.ath.jbzdak.zarlock.ui;

import cx.ath.jbzdak.common.iconManager.IconManager;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Properties;
import java.util.Map;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class DefaultIconManager {

   private static final Logger LOGGER = LoggerFactory.getLogger(DefaultIconManager.class);

   public static final IconManager ICON_MANAGER;

   static{
      ICON_MANAGER = new IconManager();
      ICON_MANAGER.setDefaultCollecion("silk");
      try{
         Properties properties = new Properties();
         properties.load(DefaultIconManager.class.getResourceAsStream("/icon.properties"));
         for(Map.Entry<Object, Object> entry : properties.entrySet()){
            ICON_MANAGER.setAlias((String) entry.getKey(), (String) entry.getValue());
         }

      }catch (Exception e){
         LOGGER.error("Exception while loading icon aliases");
      }
      
   }
}
