package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.ConfigHolder;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;


/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-17
 */
class InitLogging extends Task<Object>{
   public InitLogging() {
      super(50, "INIT_LOGGING");
   }

   @Override
   public void doTask(@Nullable Object aVoid, @Nullable Object... objects) throws Exception {
      RollingFileAppender appender = new RollingFileAppender();
      appender.setLayout(new PatternLayout("%d [%t] %-5p %c - %m%n"));
      appender.setFile(ConfigHolder.getProperties().getProperty("file.log"));
      appender.setMaxBackupIndex(10);
      appender.setMaxFileSize("100KB");
      appender.rollOver();
      Logger rootLogger = Logger.getRootLogger();
      rootLogger.addAppender(appender);
   }
}
