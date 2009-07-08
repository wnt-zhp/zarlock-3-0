package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.ui.error.DisplayErrorDetailsDialog;
import javax.swing.JOptionPane;
import org.slf4j.Logger;

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
         MainWindowModel model = new  MainWindowModel();
         MainWindowInitializer.initialize(model);
         model.startApp();
      } catch (Exception e){
         SQLException exception = Utils.findCauseOfClass(e, SQLException.class);
         if(exception != null && "08001".equals(exception.getSQLState())){
            JOptionPane.showMessageDialog(null,
                    "Nie można otworzyć bierzącej bazy danych, gdyż ta jest otwarta przez inną instancję programu.\nProgram nie może kontynuować");
         }else{
            DisplayErrorDetailsDialog.showErrorDialog(e, null);
            LOGGER.error("", e);
         }
         System.exit(42);
      }
	}

}
