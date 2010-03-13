package cx.ath.jbzdak.zarlock.ui.batch;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import cx.ath.jbzdak.jpaGui.db.dao.DAO;
import cx.ath.jbzdak.jpaGui.ui.autoComplete.AutocompleteComboBox;
import cx.ath.jbzdak.jpaGui.ui.autoComplete.ComboBoxElement;
import cx.ath.jbzdak.jpaGui.ui.form.*;
import cx.ath.jbzdak.jpaGui.ui.formatted.FormattedFieldElement;
import cx.ath.jbzdak.jpaGui.ui.formatted.FormattedTextField;
import cx.ath.jbzdak.jpaGui.ui.formatted.NotEmptyFormatter;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.BigDecimalFormatter;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.DateFormatter;
import cx.ath.jbzdak.zarlok.ZarlockBoundle;
import cx.ath.jbzdak.zarlok.entities.Batch;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Feb 23, 2010
 */
public class BatchBasicDataPanel extends JPanel {

   protected final SimpleDAOForm<Batch> form;

   protected DAO<? extends Batch> dao;

   public FormPanel<JTextField, JTextFieldFormElement<Batch>> productName;
   public FormPanel<AutocompleteComboBox<String>, ComboBoxElement<Batch, String>> specifier;
   public FormPanel<AutocompleteComboBox<String>, ComboBoxElement<Batch, String>> unit;
   public FormPanel<FormattedTextField, FormattedFieldElement<Batch,?>> startQty;
   public FormPanel<FormattedTextField, FormattedFieldElement<Batch, ?>> price;
   public FormPanel<FormattedTextField, FormattedFieldElement<Batch, ?>> expiryDatePanel;
   public FormPanel<FormattedTextField, FormattedFieldElement<Batch, ?>> bookingDatePanel;
   public FormPanel<FormattedTextField, FormattedFieldElement<Batch, ?>> fakturaNo;

   final PropertyChangeListener setBeanListener = new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
         expiryDateFormatter.wasDateSet = false; 
      }
   };
   private ExpiryDateFormatter expiryDateFormatter;

   public BatchBasicDataPanel() {
      super(new MigLayout("wrap 2, fillx", "[grow, fill|grow,fill]"));

      form = createForm();

      initGui();
   }

   protected SimpleDAOForm<Batch> createForm(){
            FormFactory<Batch> formFactory = new FormFactory<Batch>();
      formFactory.setResourceBundle(ZarlockBoundle.getZarlockBundle());
      productName = formFactory.decotrateJTextField("batch.product", "product.name");
      productName.getFormElement().setSettingValueErrorAction(SettingValueErrorAction.LOG);
      specifier = formFactory.decorateComboBox("batch.specifier", "specifier", new SpecifierAdaptor(this));
      unit = formFactory.decorateComboBox("batch.unit", "unit", new UnitAdapter(this));

      final PriceFormatter priceFormatter = new PriceFormatter();
      startQty = formFactory.decorateFormattedTextField("batch.startQty", "startQty", new StartQtyFormatter());
      price = formFactory.decorateFormattedTextField("batch.price", "price", new BigDecimalFormatter());
      startQty.addPropertyChangeListener("value", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            priceFormatter.setPrice(((Number)evt.getNewValue()).doubleValue());
         }
      });
      expiryDateFormatter = new ExpiryDateFormatter();
      expiryDatePanel = formFactory.decorateFormattedTextField("batch.expiryDate", "expiryDate",
              expiryDateFormatter);
      BookingDateFormatter bookingDateFormatter = new BookingDateFormatter();
      bookingDateFormatter.setExpiryField(expiryDatePanel.getFormElement().getRenderer());
      bookingDateFormatter.setDateFormatter(expiryDateFormatter);
      bookingDatePanel = formFactory.decorateFormattedTextField("batch.bookingDate", "bookingDate",
              bookingDateFormatter);
      bookingDatePanel.getFormElement().setReadNullValues(false);
      fakturaNo = formFactory.decorateFormattedTextField("batch.fakturaNo", "fakturaNo",
              new NotEmptyFormatter());
      fakturaNo.getFormElement().setReadNullValues(false);
      expiryDateFormatter.setBookingDateElement(bookingDatePanel.getFormElement());
      return formFactory.getCreatedForm();
   }

   protected void initGui(){
      add(productName, "span 2");
      add(specifier);
      add(unit);
      add(startQty);
      add(price);
      add(expiryDatePanel);
      add(bookingDatePanel);
      add(fakturaNo, "span 2");
   }

   public void setDao(DAO<? extends Batch> beanHolder) {
      if(dao != null){
         dao.removePropertyChangeListener("bean", setBeanListener);
      }
      form.setBeanHolder(beanHolder);
      dao = beanHolder;
      if(dao != null){
         dao.addPropertyChangeListener("bean", setBeanListener);
      }

   }

   public DAO<? extends Batch> getDao() {
      return dao;
   }

   public SimpleDAOForm<Batch> getForm() {
      return form;
   }

   Batch getBatch(){
      form.commit();
      return dao.getBean();
   }



}
