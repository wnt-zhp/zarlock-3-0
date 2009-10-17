package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.task.TasksExecutor;
import cx.ath.jbzdak.jpaGui.ui.error.DisplayErrorDetailsDialog;
import org.slf4j.Logger;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Punkt wejścia dla aplikacji.
 *
 * Raczej nie zmieniać nazwy i paczki bo zawiera funkcję main.
 * @author jb
 *
 */
public class AppLauncher {

   private static final Logger LOGGER = Utils.makeLogger();

   private static final TasksExecutor<Void> INIT_TASKS= new TasksExecutor<Void>();

   static{
      INIT_TASKS.addTask(new LoadConfigTask());
      INIT_TASKS.addTask(new InitFolders());
      INIT_TASKS.addTask(new InitLogging());
      INIT_TASKS.addTask(new StartDBTask());
   }



	@SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
   public static void main(String[] args) throws Exception {
      try{
         LOGGER.info("************************************************************");
         LOGGER.info("************************************************************");
         LOGGER.info("");
         LOGGER.info("Starting zarlock ");
         LOGGER.info("");
         LOGGER.info("zarlockstart");          
         LOGGER.info("************************************************************");
         LOGGER.info("************************************************************");
         INIT_TASKS.executeThrow(null);
      } catch (Exception e){
         SQLException exception = Utils.findCauseOfClass(e, SQLException.class);
         if(exception != null && "08001".equals(exception.getSQLState())){
            JOptionPane.showMessageDialog(null,
                    "Nie można otworzyć bierzącej bazy danych, gdyż ta jest otwarta przez inną instancję programu.\nProgram nie może kontynuować");
         }else{
            LOGGER.error("", e);
            DisplayErrorDetailsDialog.showErrorDialog(e, null);
         }
         System.exit(42);
      }
	}

}
