package cx.ath.jbzdak.zarlok.config;

import cx.ath.jbzdak.jpaGui.Utils;
import static cx.ath.jbzdak.zarlok.config.PreferencesKeys.*;
import org.slf4j.Logger;

import java.io.File;

/**
 * Statyczne metody dostępu do własności preferencji.
 * @author jb
 *
 */
public class Preferences {

   private static final Logger LOGGER = Utils.makeLogger();

	public static File getRootFolder(){
      File root =new File(PreferencesConfig.getString(ROOT_FOLDER));
      root.mkdirs();
		return root;
	}

	public static String getDatabaseFolderName(){
		return PreferencesConfig.getString(ROOT_FOLDER) + "/" +  PreferencesConfig.getString(DATABASE_CURRENT_FOLDER);
	}

	public static File getDatabaseFolder(){
		File folder = new File(PreferencesConfig.getString(ROOT_FOLDER), PreferencesConfig.getString(DATABASE_CURRENT_FOLDER));
      folder.mkdirs();
      return folder;
	}

   public static File getDocumenrsFolder(){
      File doc = new File(PreferencesConfig.getString(ROOT_FOLDER), PreferencesConfig.getString(DOCUMENTS_FOLDER));
      doc.mkdir();
      return doc;
   }

   public static File getStanMagazynuFolder(){
      File stanMagazynu = new File(getDocumenrsFolder(), PreferencesConfig.getString(STAN_MAG_FOLDER));
      stanMagazynu.mkdir();
      return stanMagazynu;
   }

   public static File getKatrotekaFolder(){
        File kartotekaFolder = new File(getDocumenrsFolder(), PreferencesConfig.getString(KARTOTEKI_FOLDER));
        kartotekaFolder.mkdir();
        return kartotekaFolder;
     }

     public static File getZapotrzebowaniaFolder(){
        File zapotrzebowaniaFolder = new File(getDocumenrsFolder(), PreferencesConfig.getString(ZZ_FOLDER));
        zapotrzebowaniaFolder.mkdir();
        return zapotrzebowaniaFolder;
     }


	public static String getDatabaseFilePrefix(){
		return PreferencesConfig.getString(DATABASE_FILENAME);
	}


	@Deprecated
	public static String getDatabaseFilename(){
		return PreferencesConfig.getString(ROOT_FOLDER)
			+ "/" + PreferencesConfig.getString(DATABASE_CURRENT_FOLDER)
			+ "/" + PreferencesConfig.getString(DATABASE_FILENAME);
	}

	public static String getDatabaseBackupFolder(){
		return PreferencesConfig.getString(ROOT_FOLDER) + File.separator + PreferencesConfig.getString(DATABASE_BACKUP_FOLDER);
	}
}
