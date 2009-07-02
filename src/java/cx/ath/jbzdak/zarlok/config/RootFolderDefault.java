package cx.ath.jbzdak.zarlok.config;

import java.io.File;
import java.util.Properties;

/**
 * Wartość generująca folder z danymi programu.
 * @author jb
 *
 */
public class RootFolderDefault extends DefaultValue {

	public static final String ROOT_FOLDER_NAME = "zarlok";

	public RootFolderDefault() {
		super(PreferencesKeys.ROOT_FOLDER);
	}

	@Override
	public String getDefaultValue() {
		Properties systemProperties = System.getProperties();
		String osName = systemProperties.getProperty("os.name");
		if(osName.matches("windows")){
			return getFolderForWindows(systemProperties, osName);
		}
		return getDefaultFolder(systemProperties);
	}


	private String getDefaultFolder(Properties properties){
		String homeFolder = properties.getProperty("user.home");
		return homeFolder + File.separatorChar + '.' + ROOT_FOLDER_NAME;
	}

	private String getFolderForWindows(Properties properties, String osName){
		if(osName.contains("XP")){
			return getFolderForXP(properties);
		}else if(false){//TODO wykrywanie Visty
			return getFolderForVista();
		}
		return getFolderForDefaultWindows(properties);
	}

	private String getFolderForXP(Properties properties){
		String homeFolder = properties.getProperty("user.dir");
		return homeFolder + File.separatorChar + "Application Data" + File.separatorChar + ROOT_FOLDER_NAME;
	}

	private String getFolderForVista(){
		return null; //TODO Implementacja
	}

	private String getFolderForDefaultWindows(Properties properties){
		String homeFolder = properties.getProperty("user.dir");
		return homeFolder + File.separatorChar + ROOT_FOLDER_NAME;
	}
}
