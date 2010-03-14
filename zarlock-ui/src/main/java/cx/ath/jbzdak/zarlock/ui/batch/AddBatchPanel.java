package cx.ath.jbzdak.zarlock.ui.batch;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.beansbinding.ObjectProperty;

import javax.swing.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumSet;

import cx.ath.jbzdak.jpaGui.db.dao.DAO;
import cx.ath.jbzdak.jpaGui.ui.autoComplete.AutocompleteComboBox;
import cx.ath.jbzdak.jpaGui.ui.autoComplete.ComboBoxElement;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanel;
import cx.ath.jbzdak.jpaGui.ui.form.SimpleDAOForm;
import cx.ath.jbzdak.jpaGui.ui.form.DefaultFormElement;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanelVisibility;
import cx.ath.jbzdak.zarlok.entities.Batch;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCache;
import cx.ath.jbzdak.zarlok.entities.Product;
import cx.ath.jbzdak.zarlok.ZarlockBoundle;
import cx.ath.jbzdak.zarlock.ui.ZarlockUtils;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 10, 2010
 */
public class AddBatchPanel extends JPanel{

   DAO<Batch> dao;

   DAO<Product> productDAO;

   FormPanel productPanel;

   final BatchBasicDataPanel dataPanel = new BatchBasicDataPanel();

   public AddBatchPanel() {
      super(new MigLayout("wrap 1, fillx", "[fill]", "[|grow, fill]"));
      AutocompleteComboBox productNameComboBox = new AutocompleteComboBox(new ProductSearchCacheAdaptor(this));
      DefaultFormElement element = new DefaultFormElement(
              productNameComboBox,
              "AddBatchPanel.selectProduct",
              ZarlockBoundle.getZarlockBundle()
      );
      productNameComboBox.setFormatter(new ProducSearchCacheFormatter());
      productNameComboBox.setStrict(true);
      productNameComboBox.addPropertyChangeListener("selectedItem", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            ProductSearchCache cache = (ProductSearchCache) evt.getNewValue();
            getProductDAO().find(cache.getProductId());
            getProductDAO().beginTransaction();
            dao.setBean(new Batch());
            dao.getBean().setProduct(productDAO.getBean());
            dao.getBean().setUnit(cache.getUnit());
            dao.getBean().setSpecifier(cache.getSpecifier());
            getProductDAO().commitTransaction();
            dataPanel.getForm().startEditing();
         }
      });
      productPanel = new FormPanel(element, null, EnumSet.of(FormPanelVisibility.HIDE_ERROR_ICON));      
      add(productPanel);
      add(dataPanel);
   }

   void reset(){
      dao.setBean(null);
   }

   Batch getBatch(){
      return dataPanel.getBatch();
   }

   public void setDao(DAO<Batch> dao) {
      this.dao = dao;
      dataPanel.setDao(dao);
   }

   public SimpleDAOForm<Batch> getForm() {
      return dataPanel.getForm();
   }

   public DAO<? extends Batch> getDao() {
      return dataPanel.getDao();
   }

   public DAO<Product> getProductDAO() {
      if (productDAO == null) {
         productDAO = ZarlockUtils.getZarlockModel(this).getDBManager().getDao(Product.class);
      }
      return productDAO;
   }
}
