package cx.ath.jbzdak.zarlock.ui.product.edit;

import cx.ath.jbzdak.jpaGui.db.dao.DAO;
import cx.ath.jbzdak.jpaGui.ui.form.FormFactory;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanel;
import cx.ath.jbzdak.jpaGui.ui.form.SimpleDAOForm;
import cx.ath.jbzdak.zarlock.ui.UnitAdaptor;
import cx.ath.jbzdak.zarlock.ui.product.ProductNameFormatter;
import cx.ath.jbzdak.zarlok.ZarlockBoundle;
import cx.ath.jbzdak.zarlok.entities.Product;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getString;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class ProductBasicDataEditPanel extends JPanel{

   private final SimpleDAOForm<Product> form;

   public ProductBasicDataEditPanel() {
      super(new MigLayout("fillx, wrap 1", "[grow, fill]"));
      FormFactory<Product> formFactory = new FormFactory<Product>();
      formFactory.setResourceBundle(ZarlockBoundle.getZarlockBundle());
      FormPanel namePanel = formFactory.decorateFormattedTextField(getString("product.name"), "name", new ProductNameFormatter());
      FormPanel unitPanel = formFactory.decotrateComboBox(getString("product.unit"), "unit", new UnitAdaptor(), false);
      FormPanel expiryPanel = formFactory.decorateFormattedTextField(getString("product.expiryDate"), "expiryDate", new ProductExpiryFormatter());
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
