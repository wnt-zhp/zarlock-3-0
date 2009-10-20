package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.ConfigHolder;
import cx.ath.jbzdak.zarlok.Constants;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-15
 */
class LoadConfigTask extends Task<Object>{
   private final String propertiesFilePath;


    LoadConfigTask() {
      super(0, "LOAD_CONFIG");
      propertiesFilePath= "/zarlock.properties";
   }

   LoadConfigTask(String propertiesFilePath) {
      this.propertiesFilePath = propertiesFilePath;
   }

   @Override
   public void doTask(@Nullable Object o, @Nullable Object[] objects) throws Exception {
      Properties defautProperties = new Properties();
      defautProperties.load(getClass().getResourceAsStream(propertiesFilePath));
      Properties properties = new Properties(defautProperties);
      File configDir = new File(Constants.configDir);
      File configFile = new File(configDir, Constants.configFile);
      System.out.println(configDir.getAbsolutePath());
      try {
         properties.load(new BufferedInputStream(new FileInputStream(configFile)));
      } catch (IOException e) {
         configDir.mkdirs();
         configFile.createNewFile();
      }
      ConfigHolder.setProperties(properties);
   }
}
