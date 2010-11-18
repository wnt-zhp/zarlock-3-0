package cx.ath.jbzdak.zarlock.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.swing.event.SwingPropertyChangeSupport;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.db.*;
import cx.ath.jbzdak.jpaGui.ui.error.DisplayError;
import cx.ath.jbzdak.jpaGui.ui.error.ErrorDetailsDialog;
import cx.ath.jbzdak.jpaGui.ui.tabbed.JBTabbedPane;
import cx.ath.jbzdak.zarlock.ui.batch.AddBatchDialog;
import cx.ath.jbzdak.zarlock.ui.batch.BatchTab;
import cx.ath.jbzdak.zarlock.ui.batch.EditBatchDialog;
import cx.ath.jbzdak.zarlock.ui.product.ProductList;
import cx.ath.jbzdak.zarlock.ui.product.ProductTab;
import cx.ath.jbzdak.zarlok.DBHolder;
import cx.ath.jbzdak.zarlok.entities.Batch;
import cx.ath.jbzdak.zarlok.entities.Product;
import cx.ath.jbzdak.zarlok.entities.Course;
import cx.ath.jbzdak.zarlok.entities.xml.XMLLoader;
import cx.ath.jbzdak.zarlok.entities.xml.XMLStore;
import cx.ath.jbzdak.zarlok.entities.xml.XMLStoreManager;

import static cx.ath.jbzdak.zarlok.ZarlockBoundle.getString;
import cx.ath.jbzdak.common.LockUtils;
import cx.ath.jbzdak.common.Action;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-16
 */
public class ZarlockModel {

   private static final Logger LOGGER = LoggerFactory.getLogger(ZarlockModel.class);

   private final PropertyChangeSupport support = new SwingPropertyChangeSupport(this, true);

   private final ZarlockFrame zarlockFrame;

   private final JBTabbedPane mainPanel = new JBTabbedPane();

   private final ProductList productList = new ProductList();

   private final ProductTab productTab = new ProductTab();

   private final BatchTab batchTab = new BatchTab();

   private EditBatchDialog editBatchDialog;

   private AddBatchDialog batchDialog;

   private final Lock courseLock = new ReentrantLock();

   private Course editedCourse; 

   public ZarlockModel(ZarlockFrame zarlockFrame) {
      this.zarlockFrame = zarlockFrame;
      mainPanel.addListener(batchTab, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if(JBTabbedPane.SELECTED.equals(e.getActionCommand())){
               getDBManager().executeTransaction(new Transaction<EntityManager>() {
                  @Override
                  public void doTransaction(EntityManager entityManager) throws Exception {
                     batchTab.setBathes(fetchBatches());
                  }
               });
            }
         }
      });
      mainPanel.addListener(getProductList(), getProductListListener());

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

   public DBManager<EntityManager> getDBManager(){
      return DBHolder.getDbManager();
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

   public List<Batch> fetchBatches(){
      return getDBManager().executeTransaction(new ReturnableTransaction<EntityManager, List<Batch>>() {
         @Override
         public List<Batch> doTransaction(EntityManager entityManager) throws Exception {
            return entityManager.createQuery("SELECT b FROM Batch b").getResultList();
         }
      });
   }





   public BatchTab getBatchTab() {
      return batchTab;
   }

   public AddBatchDialog getBatchDialog() {
      if (batchDialog == null) {
         batchDialog = new AddBatchDialog(zarlockFrame);
         batchDialog.pack();
         Utils.initLocation(batchDialog);
      }
      return batchDialog;
   }

   public EditBatchDialog getEditBatchDialog() {
      if (editBatchDialog == null) {
         editBatchDialog = new EditBatchDialog(zarlockFrame);
         editBatchDialog.pack();
         Utils.initLocation(editBatchDialog);
      }
      return editBatchDialog;
   }

   public Course getEditedCourse() {
      courseLock.lock();
      try{
         return editedCourse;
      }finally {
         courseLock.unlock();
      }

   }

   public void setEditedCourse(final Course _editedCourse) {
      LockUtils.lockAndExecute(courseLock, new Action() {
         @Override
         public void exec() {
            Course oldEditedCourse = editedCourse;
            editedCourse = _editedCourse;
            support.firePropertyChange("editedCourse", oldEditedCourse, editedCourse);
         }
      });
   }

   public void showBatchesFromProduct(Product p){

   }

   public void showProductDetails(Product p){

   }

   public void addPropertyChangeListener(PropertyChangeListener listener) {
      support.addPropertyChangeListener(listener);
   }

   public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
      support.addPropertyChangeListener(propertyName, listener);
   }

   public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
      support.removePropertyChangeListener(propertyName, listener);
   }

   public void removePropertyChangeListener(PropertyChangeListener listener) {
      support.removePropertyChangeListener(listener);
   }
}
