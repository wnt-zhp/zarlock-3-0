package cx.ath.jbzdak.zarlock.ui;

import cx.ath.jbzdak.jpaGui.UserRuntimeException;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.LifecycleManager;
import cx.ath.jbzdak.jpaGui.db.Query;
import cx.ath.jbzdak.jpaGui.db.Transaction;
import cx.ath.jbzdak.jpaGui.ui.error.DisplayError;
import cx.ath.jbzdak.jpaGui.ui.error.ErrorDetailsDialog;
import cx.ath.jbzdak.jpaGui.ui.tabbed.JBTabbedPane;
import cx.ath.jbzdak.zarlock.ui.product.ProductList;
import cx.ath.jbzdak.zarlock.ui.product.ProductTab;
import cx.ath.jbzdak.zarlok.DBHolder;
import cx.ath.jbzdak.zarlok.ZarlockBoundle;
import cx.ath.jbzdak.zarlok.entities.Product;
import cx.ath.jbzdak.zarlok.entities.xml.XMLLoader;
import cx.ath.jbzdak.zarlok.entities.xml.XMLStore;
import cx.ath.jbzdak.zarlok.entities.xml.XMLStoreManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getString;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-16
 */
public class ZarlockModel {

   private static final Logger LOGGER = LoggerFactory.getLogger(ZarlockModel.class);

   private JBTabbedPane mainPanel = new JBTabbedPane();

   private ProductList productList = new ProductList();

   private ProductTab productTab = new ProductTab();

   private final ZarlockFrame zarlockFrame;

   public ZarlockModel(ZarlockFrame zarlockFrame) {
      this.zarlockFrame = zarlockFrame;
   }

   ProductList getProductList() {
      return productList;
   }

   public JBTabbedPane getMainPanel() {
      return mainPanel;
   }

   public ProductTab getProductTab() {
      return productTab;
   }

   public void clearDB(){
      try{
         DBHolder.getLifecycleManager().clearDB();
      }catch (Exception e){
         ErrorDetailsDialog.showErrorDialog(e, zarlockFrame);
      }
   }

   public void loadFromXml(boolean clearDB, File readFrom) {
      if(!readFrom.isFile()){
         DisplayError.showError(getString("exception.message.fileNotFound"), zarlockFrame);
         return;
      }
      final XMLStore store;
      try {
         store = XMLStoreManager.loadStore(readFrom);
      } catch (Exception e) {
         ErrorDetailsDialog.showErrorDialog(e, zarlockFrame);
         return;
      }
      DBManager<EntityManager> dbManager = DBHolder.getDbManager();
      if(clearDB){
         LifecycleManager manager = DBHolder.getLifecycleManager();
         try{
            manager.clearDB();
         }catch (Exception e){
            DisplayError.showError(e, zarlockFrame);
         }
      }
      dbManager.executeTransaction(new Transaction<EntityManager>() {
         @Override
         public void doTransaction(EntityManager entityManager) throws Exception {
            XMLLoader loader = new XMLLoader(entityManager, store);
            loader.load();
         }
      });
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
