package cx.ath.jbzdak.zarlok.main;

/**
 * Punkt wejścia dla aplikacji.
 *
 * Raczej nie zmieniać nazwy i paczki bo zawiera funkcję main.
 * @author jb
 *
 */
public class AppLauncher {

	public static void main(String[] args) throws Exception {
		MainWindowModel model = new  MainWindowModel();
		MainWindowInitializer.initialize(model);
		model.startApp();
	}

}
