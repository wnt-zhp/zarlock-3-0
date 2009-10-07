package cx.ath.jbzdak.zarlok.main.initTasks;

import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.config.LFDefault;
import cx.ath.jbzdak.zarlok.config.PreferencesConfig;
import static cx.ath.jbzdak.zarlok.config.PreferencesKeys.LF_NAME;
import cx.ath.jbzdak.zarlok.main.MainWindowModel;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Ustawia look and feela.
 *
 * @see {@link LFDefault}.
 * @author jb
 *
 */
public class SetupLF extends Task<MainWindowModel> {

	public SetupLF() {
		super(-1000, "SETUP_LF");
	}

	@Override
	public void doTask(MainWindowModel t, Object... o) throws Exception {
//		UIManager.getDefaults().put("defaultFont", new Font("CupolaUnicode", Font.PLAIN,13));
//		UIManager.getDefaults().put("TableHeader.font", new Font("CupolaUnicode", Font.PLAIN,13));

		for(LookAndFeelInfo lfi : UIManager.getInstalledLookAndFeels()){
			if(lfi.getName().equals(PreferencesConfig.getString(LF_NAME))){
				UIManager.setLookAndFeel(lfi.getClassName());
			}
		}

	}

}
