package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.ConfigHolder;
import cx.ath.jbzdak.zarlok.Constants;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.io.File;
import java.util.Properties;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-15
 */
class InitFolders extends Task<Object>{

   public InitFolders() {
      super(1, "INIT_FOLDERS");
   }

   //TODO dla visty i XP i windows 7
   private String getUserHome(){
      return System.getProperty("user.home");
   }

   private  void setDir(String name, String path){
      Properties properties = ConfigHolder.getProperties();
      properties.setProperty(name, path);
      new File(path).mkdirs();
   }

   @Override
   public void doTask(@Nullable Object o, @Nullable Object... objects) throws Exception {
      Properties properties = ConfigHolder.getProperties();
      String instanceName = properties.getProperty("instanceName");
      String rootDir = getUserHome() + File.separator + ".zarlock" + File.separator + instanceName;
      setDir("dir.root", rootDir);
      setDir("dir.db", rootDir + File.separator + Constants.dbDir);
      properties.setProperty("file.db", rootDir + File.separator + Constants.dbDir + File.separator + Constants.dbFile);
      setDir("dir.log", rootDir + File.separator + Constants.logDir);
      properties.setProperty("file.log", rootDir + File.separator + Constants.logDir + File.separator + Constants.logFile);
      setDir("dir.backup", rootDir + File.separator + Constants.backupDir);
   }
}
