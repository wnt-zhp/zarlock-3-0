package cx.ath.jbzdak.zarlok.ui.partia;

import cx.ath.jbzdak.jpaGui.ui.form.AbstractFormElement;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanelConstraints;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanelMock;
import cx.ath.jbzdak.jpaGui.ui.formatted.FormattedFieldElement;
import cx.ath.jbzdak.jpaGui.ui.formatted.MyFormattedTextField;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.DateFormatter;
import cx.ath.jbzdak.jpaGui.ui.query.FulltextFilter;
import cx.ath.jbzdak.jpaGui.ui.query.FulltextJTtextField;
import cx.ath.jbzdak.jpaGui.ui.query.GeneralFilter;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.PartieUtils;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-06-27
 */
public class PartiePanelFilter extends JPanel{

   private final JCheckBox nieWydaneBox = new JCheckBox();

   private final JTable table;

   private List<Partia> model;

   private final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>();

   private final DatyFilter datyFilter = new DatyFilter();

   private final GeneralFilter<TableModel, Integer> wydanoFilter = new GeneralFilter<TableModel, Integer>(4) {
      private final Pattern pat = Pattern.compile("([\\d.,]+)\\[([.,\\d]+)\\]");

      @Override
      protected boolean include(Entry<? extends TableModel, ? extends Integer> entry, int index) {
         String value = entry.getStringValue(index);
         System.out.println(value);
         Matcher m = pat.matcher(value);
         if(m.find()){
            Double terazWMag = Double.valueOf(m.group(1));
            return terazWMag > 0.01;
         }
         return false;
      }
   };

   public PartiePanelFilter(JTable table) {
      super(new MigLayout("fillx, wrap 4", "[min!|fill, grow|min!|min!]"));
      this.table = table;
      FulltextFilter<TableModel, Integer> fulltextFilter = new FulltextFilter<TableModel, Integer>(0, 1, 2, 3, 4, 5, 6);
      FulltextJTtextField fulltextJTtextField = new FulltextJTtextField(fulltextFilter);
      add(new JLabel("Zapytanie:"));
      add(fulltextJTtextField, "span 3");
      final MyFormattedTextField jestWMagTextField = new MyFormattedTextField(new DateFormatter());
      AbstractFormElement element =   new FormattedFieldElement(
              jestWMagTextField,
              "Było w magazynie:"
      );
      jestWMagTextField.setEchoErrorsAtOnce(false);
      element.setShortDescription("Pozwala wyszukiwać partie które były w magazynie danego dnia");
      FormPanelMock panel = new FormPanelMock(element, FormPanelConstraints.createLargeConstraints());
      add(panel, "span 2, growx");
      add(new JLabel("Nie wydane"));
      add(nieWydaneBox);
      RowFilter<TableModel, Integer> tableFilter = RowFilter.andFilter(Arrays.asList(fulltextFilter, wydanoFilter, datyFilter));

      panel.getFormElement().addPropertyChangeListener("error", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            datyFilter.setEnabled(!(Boolean) evt.getNewValue());
            datyFilter.setData((Date) jestWMagTextField.getValue());
            sorter.sort();
         }
      });
      table.addPropertyChangeListener("model", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            sorter.setModel(PartiePanelFilter.this.table.getModel());
         }
      });
      fulltextJTtextField.addQueryChangedListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            sorter.sort();
         }
      });

      ItemListener listener = new  ItemListener(){
         @Override
         public void itemStateChanged(ItemEvent e) {
            wydanoFilter.setEnabled(nieWydaneBox.isSelected());
            sorter.sort();
         }
	   };
      nieWydaneBox.addItemListener(listener);
      sorter.setModel(table.getModel());
      table.setRowSorter(sorter);
      sorter.setRowFilter(tableFilter);

      datyFilter.setEnabled(!element.isError());
      wydanoFilter.setEnabled(nieWydaneBox.isSelected());
   }

   public void setModel(List<Partia> model) {
      this.model = model;
   }

   private class DatyFilter extends RowFilter<TableModel, Integer>{

      private Date data;

      private boolean enabled = true;

      @Override
      public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
         if(!enabled){
            return true;
         }
         Integer row = entry.getIdentifier();
         //row = table.convertRowIndexToModel(row);
         Partia p = model.get(row);
         //System.out.println("p=" + p);
         System.out.println("data" + DateFormat.getDateInstance(DateFormat.SHORT).format(data));
         if(!p.getDataKsiegowania().before(data)){
            return false;
         }
         if(p.getDataWaznosci()!=null && !p.getDataWaznosci().after(data)){
            return false;
         }
         if(p.getIloscTeraz().compareTo(BigDecimal.ZERO)<=0){
            return false;
         }
         System.out.println("Ilosc" + PartieUtils.getIloscForDay(p, data).compareTo(new BigDecimal("0.01")));
         if(PartieUtils.getIloscForDay(p, data).compareTo(new BigDecimal("0.01"))<=0){
            return false;
         }
         return  true;
      }

      public Date getData() {
         return data;
      }

      public void setData(Date data) {
         this.data = data;
      }

      public boolean isEnabled() {
         return enabled;
      }

      public void setEnabled(boolean enabled) {
         this.enabled = enabled;
      }
   }
}

