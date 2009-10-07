package cx.ath.jbzdak.zarlok.config;

import cx.ath.jbzdak.jpaGui.app.ConfigEntry;
import cx.ath.jbzdak.jpaGui.app.ConfigurationSource;
import cx.ath.jbzdak.jpaGui.ui.form.AbstractFormElement;
import cx.ath.jbzdak.jpaGui.ui.formatted.MyFormattedTextField;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-04-25
 */
public class ConfigEntryFormElement<T extends MyFormattedTextField> extends AbstractFormElement<T>{

   private final ConfigEntry entry;

   private final ConfigurationSource configurationSource;

   public ConfigEntryFormElement(T renderer, ConfigEntry entry, ConfigurationSource source) {
      super(renderer, entry.getName(), entry.getShortDescription(), entry.getLongDescription());
      this.entry = entry;
      this.configurationSource = source;
   }

   @Override
   public void startEditing() {
      setEditable(true);
      getRenderer().setValueFromBean(entry.getSingleValue());
   }

   @Override
   public void startViewing() {
      setEditable(false);
      getRenderer().setValueFromBean(entry.getSingleValue());
   }

   @Override
   public void rollback() {
      setEditable(false);
   }

   @Override
   public void commit() {
      configurationSource.saveConfiguration(entry.getName(), getRenderer().getValue());
   }

   @Override
   public void clear() {
      getRenderer().setValueFromBean(null);
   }
}
