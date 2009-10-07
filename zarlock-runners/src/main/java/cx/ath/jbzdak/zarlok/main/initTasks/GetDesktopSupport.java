package cx.ath.jbzdak.zarlok.main.initTasks;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.main.MainWindowModel;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.Desktop.Action;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-04-29
 */
public class GetDesktopSupport extends Task<MainWindowModel>
{
   private static final Logger LOGGER = Utils.makeLogger();

   @Override
   public void doTask(@Nullable MainWindowModel o, @Nullable Object... o2) throws Exception {
      LOGGER.info("Determining desktop suppot");
      LOGGER.info("Desktop is supported: {}", Desktop.isDesktopSupported());
      if(Desktop.isDesktopSupported()){
         for(Action a : Action.values()){
            LOGGER.info("Action {} is supported {}", a, Desktop.getDesktop().isSupported(a));
         }
      }
   }
}
