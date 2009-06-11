package cx.ath.jbzdak.zarlok.config;

import static cx.ath.jbzdak.zarlok.config.PreferencesKeys.LF_NAME;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Domyślna wartość dla look and feela.
 * @author jb
 *
 */
public class LFDefault extends DefaultValue {

	public LFDefault() {
		super(LF_NAME,false);
	}

	/**
	 * Zwraca NAZWĘ look and feela. Zwraca nazwę LFa z swing.defaultlaf.
	 * jak nie ma tej zmiennej ustawionej, lub nie znajdzie takiegl LF
	 * to zwraca ciąg znaków "Nimbus".
	 */
	@Override
	public String getDefaultValue() {
		String swingDefaultLf = System.getProperty("swing.defaultlaf");
		if(swingDefaultLf!=null){
			for(LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()){
				if(swingDefaultLf.equals(lafi.getClassName())){
					return lafi.getName();
				}
			}
		}
		return "Nimbus";
	}

}
