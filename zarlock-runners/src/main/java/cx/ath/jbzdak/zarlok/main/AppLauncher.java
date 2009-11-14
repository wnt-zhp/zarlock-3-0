package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.task.TasksExecutor;
import cx.ath.jbzdak.jpaGui.ui.error.DisplayErrorDetailsDialog;
import cx.ath.jbzdak.zarlok.LoadResouresTask;
import cx.ath.jbzdak.zarlok.StartDBTask;
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

   protected final TasksExecutor<AppLauncher> INIT_TASKS= new TasksExecutor<AppLauncher>();

   public DBLauncher dbLauncher;

   {
      INIT_TASKS.addTask(new LoadConfigTask());
      INIT_TASKS.addTask(new InitDBLauncher());
      INIT_TASKS.addTask(new InitFolders());
      INIT_TASKS.addTask(new InitLogging());
      INIT_TASKS.addTask(new StartDBTask());
      INIT_TASKS.addTask(new LoadResouresTask());
   }

   public void start(){
        try{
         LOGGER.info("************************************************************");
         LOGGER.info("************************************************************");
         LOGGER.info("**                                                        **");
         LOGGER.info("**                    Starting zarlock                    **");
         LOGGER.info("**                                                        **");
         LOGGER.info("**zarlockStart**********************************************");
         LOGGER.info("************************************************************");
         INIT_TASKS.executeThrow(this);
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

	@SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
   public static void main(String[] args) throws Exception {
      new AppLauncher().start();
	}

}
