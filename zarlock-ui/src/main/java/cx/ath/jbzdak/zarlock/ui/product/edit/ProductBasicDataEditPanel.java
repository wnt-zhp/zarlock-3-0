package cx.ath.jbzdak.zarlock.ui.product.edit;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

import cx.ath.jbzdak.jpaGui.ui.formatted.FormattedTextField;
import cx.ath.jbzdak.jpaGui.ui.autoComplete.AutocompleteComboBox;
import cx.ath.jbzdak.jpaGui.ui.form.*;
import cx.ath.jbzdak.jpaGui.db.dao.DAO;
import cx.ath.jbzdak.zarlok.entities.Product;
import cx.ath.jbzdak.zarlok.ZarlockBoundle;
import cx.ath.jbzdak.zarlock.ui.product.ProductNameFormatter;
import cx.ath.jbzdak.zarlock.ui.UnitAdaptor;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class ProductBasicDataEditPanel extends JPanel{

   private final SimpleDAOForm<Product> form;

   public ProductBasicDataEditPanel() {
      super(new MigLayout("fillx, wrap 1", "[grow]"));
      FormFactory<Product> formFactory = new FormFactory<Product>();
      formFactory.setResourceBundle(ZarlockBoundle.getZarlockBounle());
      FormPanel namePanel  = formFactory.decorateFormattedTextField("Nazwa produktu", "name", new ProductNameFormatter());
      FormPanel unitPanel = formFactory.decotrateComboBox("Jednostka", "unit", new UnitAdaptor(), false);
      FormPanel expiryPanel = formFactory.decorateFormattedTextField("Data ważności", "expiryDate", new ProductExpiryFormatter());
      add(namePanel);
      add(unitPanel);
      add(expiryPanel);
      form = formFactory.getCreatedForm();
   }

   public void setBeanHolder(DAO<? extends Product> beanHolder) {
      form.setBeanHolder(beanHolder);
   }
   
   public SimpleDAOForm<Product> getForm() {
      return form;
   }
}
