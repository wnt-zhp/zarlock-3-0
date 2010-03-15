package cx.ath.jbzdak.zarlock.ui.product;

import cx.ath.jbzdak.jpaGui.db.Query;
import cx.ath.jbzdak.jpaGui.db.dao.DAO;
import cx.ath.jbzdak.jpaGui.ui.autoComplete.AutocompleteComboBox;
import cx.ath.jbzdak.jpaGui.ui.form.DefaultFormElement;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanel;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanelVisibility;
import cx.ath.jbzdak.jpaGui.ui.form.FormPanelConstraints;
import cx.ath.jbzdak.zarlock.ui.product.edit.ProductEditPanel;
import cx.ath.jbzdak.zarlok.DBHolder;
import cx.ath.jbzdak.zarlok.ZarlockBoundle;
import cx.ath.jbzdak.zarlok.entities.Product;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumSet;
import java.util.List;

import static cx.ath.jbzdak.zarlock.ui.DefaultIconManager.ICON_MANAGER;
import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getString;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-13
 */
public class ProductTab extends JPanel{

   private JPanel productSelectPanel;

   ProductEditPanel productEditPanel = new ProductEditPanel();

   StockLevelPanel stockLevelPanel = new StockLevelPanel();

   KartotekaPanel kartotekaPanel = new KartotekaPanel(); 

   Product product;

   public ProductTab() {
      setLayout(new MigLayout("fillx, filly", "[][grow]", "[][][grow]"));
      productEditPanel.setBorder(BorderFactory.createTitledBorder(getString("product.tab.basicData.title")));
      stockLevelPanel.setBorder(BorderFactory.createTitledBorder(getString("product.tab.stockLevel.title")));
      add(new ControlPanel(), "");
      add(kartotekaPanel, "span 1 3, wrap, growx, growy");
      add(productEditPanel, "wrap, growx");
      add(stockLevelPanel, "growx, growy");

   }


   public Product getProduct() {
      return product;
   }

   public void setProduct(Product product) {
      this.product = product;
      productEditPanel.setProduct(product);
      stockLevelPanel.setProduct(product);
      kartotekaPanel.setProduct(product);
   }

   private class ControlPanel extends JPanel{

      AutocompleteComboBox<String> productSelection = new AutocompleteComboBox<String>(new ProductNameAdaptor(DBHolder.getDbManager()), false);

      boolean willCreate = true;

      JButton addEditButton = new JButton();

      private ControlPanel() {
         super(new MigLayout("fillx"));
         add(new FormPanel(
                 new DefaultFormElement(productSelection,
                         "product.tab.selectProduct",
                         ZarlockBoundle.getZarlockBundle()
                 ),
                 FormPanelConstraints.createLargeConstraints(),
                 EnumSet.of(FormPanelVisibility.HIDE_ERROR_ICON)));
         add(addEditButton);
         productSelection.addPropertyChangeListener("selectedItem", new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
               String name = (String) evt.getNewValue();
               Query q = DBHolder.getDbManager().createNamedQuery("getProductIdByName");
               try{
                  q.setParameter("name", productSelection.getSelectedValue());
                  List<String> results = q.getResultList();
                  System.out.println("esults.size()" + results.size());
                  setWillCreate(results.size()==0);
               }finally {
                  q.close();
               }
            }
         });

         updateButtonLabel();
         addEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if(willCreate){
                  Product p = new Product();
                  p.setName(productSelection.getSelectedValue());
                  setProduct(p);
               }else{
                  Long id;
                  Query q = DBHolder.getDbManager().createNamedQuery("getProductIdByName");
                  try{
                     q.setParameter("name", productSelection.getSelectedValue());
                     id = (Long) q.getSingleResult();
                  }finally {
                     q.close();
                  }
                  DAO<Product> productDAO = DBHolder.getDbManager().getDao(Product.class);
                  productDAO.find(id);
                  setProduct(productDAO.getBean());
               }
            }
         });
      }

      public boolean isWillCreate() {
         return willCreate;
      }

      public void setWillCreate(boolean willCreate) {
         if(this.willCreate != willCreate){
            this.willCreate = willCreate;
            updateButtonLabel();
         }
      }

      private void updateButtonLabel() {
         if(willCreate){
            addEditButton.setIcon(ICON_MANAGER.getIcon("product.add"));
            addEditButton.setText(getString("product.tab.add"));
         }else{
            addEditButton.setIcon(ICON_MANAGER.getIcon("product.edit"));
            addEditButton.setText(getString("product.tab.edit"));
         }
      }
   }
}
