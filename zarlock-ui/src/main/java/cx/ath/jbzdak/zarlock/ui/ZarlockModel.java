package cx.ath.jbzdak.zarlock.ui;

import cx.ath.jbzdak.jpaGui.db.Query;
import cx.ath.jbzdak.jpaGui.ui.tabbed.JBTabbedPane;
import cx.ath.jbzdak.zarlock.ui.product.ProductList;
import cx.ath.jbzdak.zarlock.ui.product.ProductTab;
import cx.ath.jbzdak.zarlok.DBHolder;
import cx.ath.jbzdak.zarlok.entities.Product;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-16
 */
public class ZarlockModel {

   private JBTabbedPane mainPanel = new JBTabbedPane();

   private ProductList productList = new ProductList();

   private ProductTab productTab = new ProductTab();

   ProductList getProductList() {
      return productList;
   }

   public JBTabbedPane getMainPanel() {
      return mainPanel;
   }

   public ProductTab getProductTab() {
      return productTab;
   }

   ActionListener getProductListListener(){
      return new ActionListener(){
         @Override
         public void actionPerformed(ActionEvent e) {
            if(JBTabbedPane.SELECTED.equals(e.getActionCommand())){
               Query q = DBHolder.getDbManager().createQuery("SELECT p FROM Product p");
               try{
                  productList.setProducts(q.getResultList());
               }finally {
                  q.close();
               }
            }
         }
      };
   }


   public void showBatchesFromProduct(Product p){
      
   }

   public void showProductDetails(Product p){
      
   }
}
