package cx.ath.jbzdak.zarlok.config;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.app.ConfigEntry;
import cx.ath.jbzdak.jpaGui.app.ConfigurationSource;
import cx.ath.jbzdak.jpaGui.app.DefaultConfigEntry;
import static cx.ath.jbzdak.zarlok.config.PreferencesKeys.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Uważać przy refactoringu -- po wypierdoli userom konfiguracje z java.util.prefs
 *
 * Zarządza preferencjami w systemie.
 * @author jb
 *
 */
public class PreferencesConfig{

	private static final String PROPERTIES_PREFIX = "cx.ath.jbzdak.zarlok.2-0.";

//	private static final Preferences PREFERENCES;

   private static final Properties PROPERTIES;

	private static final Logger log = Logger.getLogger(PreferencesConfig.class);

   public static ResourceBundle getResourceBundle() {
      return RESOURCE_BUNDLE;
   }

   private static final ResourceBundle RESOURCE_BUNDLE;

   private static final Map<String, DefaultValue > DEFAULTS = new HashMap<String, DefaultValue>();

   static{
      try {
         RESOURCE_BUNDLE =  new PropertyResourceBundle(PreferencesConfig.class.getResourceAsStream("configEntries.properties"));
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   @edu.umd.cs.findbugs.annotations.SuppressWarnings( value = {"RV_RETURN_VALUE_IGNORED_BAD_PRACTICE"}, justification = "Plik może istnieć wcześniej")
   private static File getPropertiesFile() throws IOException {
      File configFile;
     try {
         File configFolder = new File(Utils.guessProgramFolder(), "config");
         configFolder.mkdirs();
         configFile = new File(configFolder, "zarlock-config.xml");
         configFile.createNewFile();
      } catch (IOException e) {
         //log.info("Exception while loading properties, starting from idea? falling bact to home/.zarlock/config", e);
         File configFolder = new File(System.getProperty("user.dir"), ".zarlock/config");
         configFolder.mkdirs();
         configFile = new File(configFolder, "zarlock-config.xml");
         configFile.createNewFile();
      }
      return configFile;
   }

   public static ConfigurationSource getConfigurationSource(){
      return new ConfigurationSource() {

         final Map<String, ConfigEntry<String>> map;

         {
            map = new HashMap<String, ConfigEntry<String>>();
            for (String s : DEFAULTS.keySet()){
               map.put(s, DefaultConfigEntry.createStringEntry(s,DEFAULTS.get(s).getDefaultValue(),RESOURCE_BUNDLE));
            }
            for (String s : PROPERTIES.stringPropertyNames()) {
               map.put(s, DefaultConfigEntry.createStringEntry(s, PROPERTIES.getProperty(s),RESOURCE_BUNDLE));
            }
         }

         @Override
         public Map<String, ? extends ConfigEntry> getConfiguration() {
            return  map;
         }

         @Override
         public Set<String> getSupportedKeys() {
            return map.keySet();
         }

         @Override
         public void saveConfiguration(String key, Object value) {
           PROPERTIES.setProperty(key, String.valueOf(value));
            try {
               saveProperties();
            } catch (ConfigurationException e) {
               log.fatal("Error while saving configuration", e);
            }
         }
      };
   }

   @edu.umd.cs.findbugs.annotations.SuppressWarnings("OBL_UNSATISFIED_OBLIGATION")
   private static void saveProperties() throws ConfigurationException{
      FileOutputStream os = null;
      FileInputStream is = null;
      try{
         os = new FileOutputStream(getPropertiesFile());
         PROPERTIES.storeToXML(os, "");
         is = new FileInputStream(getPropertiesFile());
         PROPERTIES.loadFromXML(is);
      }catch (RuntimeException re){
         throw re;
      }catch (Exception e){
         throw new ConfigurationException(e);
      }finally {
         try {
            os.close();
         } catch (Exception e) {
            log.debug(e);
         }
         try {
            is.close();
         } catch (IOException e) {
            log.debug(e);
         }
      }
   }

   static{
      PROPERTIES =  new Properties();
      try {
         PROPERTIES.loadFromXML(new FileInputStream(getPropertiesFile()));
      } catch (IOException e) {
         log.fatal("Exception while loading properties", e);
      }
   }


	private static void registerDefaultValue(DefaultValue defaultValue){
		if(DEFAULTS.containsKey(defaultValue.getKey())){
			log.warn("Going to override default value for key " +
					defaultValue.getKey() + "' poprzednia wartość: " +
					DEFAULTS.get(defaultValue.getKey()) + " nowa: " +
					defaultValue);
		}
		DEFAULTS.put(defaultValue.getKey(), defaultValue);
	}

	public static String getString(String key){
		String result = (String) PROPERTIES.get(PROPERTIES_PREFIX + key);
		if(result==null){
			DefaultValue def = DEFAULTS.get(key);
			if(def!=null){
				String a = def.getDefaultValue();
				if(def.isFeedValueToProperties()){
					PROPERTIES.put(PROPERTIES_PREFIX + key, a);
               try {
                  saveProperties();
               } catch (ConfigurationException e) {
                  log.info("Error while saving properties", e);
               }
            }
				return a;
			}
			return null;
		}
		return result;
	}

//   public static String getConfigEntry(){
//
//   }


	static{
		registerDefaultValue(new RootFolderDefault());
		registerDefaultValue(new SimpleDefaultValue(DATABASE_BACKUP_FOLDER, "backup"));
		registerDefaultValue(new SimpleDefaultValue(DATABASE_CURRENT_FOLDER, "database"));
		registerDefaultValue(new SimpleDefaultValue(DATABASE_FILENAME, "dbfile"));
		registerDefaultValue(new SimpleDefaultValue(DOCUMENTS_FOLDER, "documents"));
      registerDefaultValue(new SimpleDefaultValue(KARTOTEKI_FOLDER, "kartoteki"));
      registerDefaultValue(new SimpleDefaultValue(ZZ_FOLDER, "zapotrzebowania"));
      registerDefaultValue(new SimpleDefaultValue(STAN_MAG_FOLDER, "stany_magazynow"));
      registerDefaultValue(new SimpleDefaultValue(NAZWA_OBOZU_1, "Wpisz nazwę obozu"));
      registerDefaultValue(new SimpleDefaultValue(NAZWA_OBOZU_2, "Wejdź w Opcje - konfiguracja"));
      registerDefaultValue(new SimpleDefaultValue(NAZWA_OBOZU_3, "I ustaw pola: nazwa obozu linia 1, 2 i 3"));
      registerDefaultValue(new SimpleDefaultValue(KOMENDANT, "Wpisz imię komendanta"));
      registerDefaultValue(new SimpleDefaultValue(KWATERMISTRZ, "Wpisz imię kwatermistrza"));
      registerDefaultValue(new SimpleDefaultValue(CZLONEK_RADY_OBOZU, "Wpisz imię członka rady obozu zatwierdzającego ZZ"));
      registerDefaultValue(new SimpleDefaultValue(STAWKA_ŻYWIENIOWA, ""));
      registerDefaultValue(new SimpleDefaultValue(EDITABLE_KEYS, "stawka.*;nazwa.*;imie.*", false));
		registerDefaultValue(new LFDefault());
	}

}
