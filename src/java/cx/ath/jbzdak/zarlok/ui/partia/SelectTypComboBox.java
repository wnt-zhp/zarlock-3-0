package cx.ath.jbzdak.zarlok.ui.partia;

import cx.ath.jbzdak.zarlok.ui.formatted.formatters.CenaFormatter;
import javax.swing.JComboBox;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;

import java.beans.PropertyChangeSupport;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-06-11
 */
public class SelectTypComboBox extends JComboBox{

   public static final String CENA_JEDNOSTKOWA = "Cena jednostkowa";

   public static final String CENA_SUMARYCZNA = "Cena sumaryczna";

   private PropertyChangeSupport support = new PropertyChangeSupport(this);

   CenaFormatter cenaFormatter;

   boolean cenaJednostkowaSelected = true;

   public SelectTypComboBox(CenaFormatter cenaFormatter) {
      super(new Object[]{CENA_JEDNOSTKOWA, CENA_SUMARYCZNA});
      setSelectedItem(CENA_JEDNOSTKOWA);
      setEditable(false);
      setCenaFormatter(cenaFormatter);
   }

   public void setCenaFormatter(CenaFormatter cenaFormatter) {
      this.cenaFormatter = cenaFormatter;
      if(cenaFormatter!=null){
         cenaFormatter.setCenaJednostkowa(cenaJednostkowaSelected);
      }
   }

   public void setCenaJednostkowaSelected(boolean cenaJednostkowaSelected) {
      if(this.cenaJednostkowaSelected != cenaJednostkowaSelected){
         if(cenaFormatter != null){
            cenaFormatter.setCenaJednostkowa(cenaJednostkowaSelected);
         }
      }
      this.cenaJednostkowaSelected = cenaJednostkowaSelected;
   }

   @Override
   public void setSelectedItem(Object anObject) {
      super.setSelectedItem(anObject);
      setCenaJednostkowaSelected(anObject == CENA_JEDNOSTKOWA);
   }
}
