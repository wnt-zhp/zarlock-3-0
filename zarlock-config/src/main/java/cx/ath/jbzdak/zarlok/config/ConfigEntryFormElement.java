package cx.ath.jbzdak.zarlok.config;

import cx.ath.jbzdak.jpaGui.BeanHolder;
import cx.ath.jbzdak.jpaGui.ui.form.PropertyFormElement;
import cx.ath.jbzdak.jpaGui.ui.formatted.FormattedTextField;
import org.jdesktop.beansbinding.Property;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-04-25
 */
public class ConfigEntryFormElement<T extends FormattedTextField> extends PropertyFormElement<T, Object, Object, BeanHolder<Object>> {
   public ConfigEntryFormElement(T t, String s) {
      super(t, s);
   }

   public ConfigEntryFormElement(T t, String s, String s1) {
      super(t, s, s1);
   }

   public ConfigEntryFormElement(T t, String s, Property<Object, Object> objectObjectProperty) {
      super(t, s, objectObjectProperty);
   }

   //   private final ConfigEntry entry;
//
//   private final ConfigurationSource configurationSource;

//   public ConfigEntryFormElement(T renderer, ConfigEntry entry, ConfigurationSource source) {
//      super(renderer, entry.getName(), entry.getShortDescription(), entry.getLongDescription());
//      this.entry = entry;
//      this.configurationSource = source;
//   }
//
//   @Override
//   public void startEditing() {
//      setEditable(true);
//      getRenderer().setValueFromBean(entry.getSingleValue());
//   }
//
//   @Override
//   public void startViewing() {
//      setEditable(false);
//      getRenderer().setValueFromBean(entry.getSingleValue());

   @Override
   protected void setRendererEditable(boolean b) {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public void setValue(Object o) {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public void clear() {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public Object getValue() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }
//   }
//
//   @Override
//   public void rollback() {
//      setEditable(false);
//   }
//
//   @Override
//   public void commit() {
//      configurationSource.saveConfiguration(entry.getName(), getRenderer().getValue());
//   }
//
//   @Override
//   public void clear() {
//      getRenderer().setValueFromBean(null);
//   }
}
