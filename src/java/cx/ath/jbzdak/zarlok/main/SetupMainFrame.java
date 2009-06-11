package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.ui.main.MainFrame;

/**
 * Task tworzący główną ramkę.
 * @author jb
 *
 */
public class SetupMainFrame extends Task<MainWindowModel> {

	public SetupMainFrame() {
		super(0, "INIT_MAIN_FRAME");
		
	}

	@Override
	public void doTask(MainWindowModel t, Object... o) throws Exception {
		t.mainFrame = new MainFrame(t);
	}

}
