package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.ui.error.DisplayErrorDetailsDialog;

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

	public static void main(String[] args) throws Exception {
      try{
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
         }
         System.exit(42);
      }
	}

}
