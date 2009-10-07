package cx.ath.jbzdak.zarlok.ui.partia;

import cx.ath.jbzdak.jpaGui.genericListeners.DebugPropertyChangeListener;
import cx.ath.jbzdak.jpaGui.ui.formatted.MyFormattedTextField;
import cx.ath.jbzdak.zarlok.ui.formatted.formatters.CenaFormatter;

import javax.swing.*;
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

   MyFormattedTextField cenaField;

   boolean cenaJednostkowaSelected = true;

   private SelectTypComboBox(CenaFormatter cenaFormatter) {
      super(new Object[]{CENA_JEDNOSTKOWA, CENA_SUMARYCZNA});
      setSelectedItem(CENA_JEDNOSTKOWA);
      setEditable(false);
      setCenaFormatter(cenaFormatter);

   }

   public SelectTypComboBox(MyFormattedTextField cenaField) {
      this((CenaFormatter) cenaField.getFormatter());
      this.cenaField = cenaField;
      cenaField.addPropertyChangeListener(new DebugPropertyChangeListener());
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
            if(cenaField!=null){
               try {
                  cenaField.attemptParseText();
                  cenaField.formatValue();
               } catch (Exception e) {
                  e.printStackTrace();
               }

            }
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
