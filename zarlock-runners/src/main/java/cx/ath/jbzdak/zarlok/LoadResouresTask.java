package cx.ath.jbzdak.zarlok;

import cx.ath.jbzdak.jpaGui.task.Task;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class LoadResouresTask extends Task<Object>{

   private static final Logger LOGGER = LoggerFactory.getLogger(LoadResouresTask.class);

   public LoadResouresTask() {
      super(100, "LOAD_RESOURCES");
   }

   @Override
   public void doTask(@Nullable Object o, @Nullable Object... varargs) throws Exception {
      Locale locale = Locale.getDefault();
      if(ConfigHolder.getProperties().contains("overrideLocale")){
         try{
            locale = new Locale(ConfigHolder.getProperties().getProperty("overrideLocale"));
            Locale.setDefault(locale);
         }catch (Exception e){
            LOGGER.warn("Exceptin while parsing override locale. ", e);
         }
      }
      ResourceBundle resourceBundle = ResourceBundle.getBundle("zarlockBoundle", locale);
      ZarlockBoundle.setZarlockBundle(resourceBundle);
   }
}
