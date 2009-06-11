package cx.ath.jbzdak.zarlok.ui.opcje;

import cx.ath.jbzdak.jpaGui.app.ConfigEntry;
import cx.ath.jbzdak.jpaGui.app.ConfigurationSource;
import cx.ath.jbzdak.jpaGui.ui.form.FormElement;
import cx.ath.jbzdak.jpaGui.ui.form.SimpleForm;
import cx.ath.jbzdak.jpaGui.ui.formatted.MyFormattedTextField;
import cx.ath.jbzdak.jpaGui.ui.formatted.NoopFormatter;
import cx.ath.jbzdak.zarlok.config.ConfigEntryFormElement;
import cx.ath.jbzdak.zarlok.config.PreferencesConfig;
import cx.ath.jbzdak.zarlok.config.PreferencesKeys;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-04-27
 */
public class OpcjeEditPanelModel {

   SimpleForm<FormElement, ConfigEntry> form = new SimpleForm<FormElement, ConfigEntry>();

   private ConfigurationSource source = PreferencesConfig.getConfigurationSource();

   private Map<String, ? extends ConfigEntry> configuration = source.getConfiguration();

   private Set<String> acceptedKeys;

   {
      acceptedKeys = new HashSet<String>(
              Arrays.asList(PreferencesConfig.getString(PreferencesKeys.EDITABLE_KEYS).split(";")));
   }

   void addToForm(FormElement fe){
      form.add(fe);
   }

   void addToForm(String key){
      MyFormattedTextField textField = new MyFormattedTextField(new NoopFormatter());
      ConfigEntryFormElement element = new ConfigEntryFormElement(textField, configuration.get(key), source);
      addToForm(element);
   }

   private boolean isAccepted(String key){
      for (String acceptedKey : acceptedKeys) {
         if(Pattern.compile(acceptedKey).matcher(key).matches()){
            return true;
         }
      }
      return false;
   }

   public OpcjeEditPanelModel() {
      for(String key : source.getSupportedKeys()){
         if(isAccepted(key)){
            addToForm(key);
         }
      }
   }

   public void commit(){
      form.commit();
   }
}
