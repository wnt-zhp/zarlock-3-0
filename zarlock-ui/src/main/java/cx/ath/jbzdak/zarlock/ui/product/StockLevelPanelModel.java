package cx.ath.jbzdak.zarlock.ui.product;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.JPAReturnableTransaction;
import cx.ath.jbzdak.zarlok.DBHolder;
import cx.ath.jbzdak.zarlok.entities.Product;
import cx.ath.jbzdak.zarlok.entities.misc.StockLevelBean;

import javax.persistence.EntityManager;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class StockLevelPanelModel {

   private final PropertyChangeSupport support = new PropertyChangeSupport(this);

   Product product;

   List<StockLevelBean> stockLevels = Collections.emptyList();

   public Product getProduct() {
      return product;
   }

   public void setProduct(Product product) {
      this.product = product;
      initStockLevels();
   }

   private void initStockLevels(){
      if(product==null){
         stockLevels = Collections.emptyList();
      }
      DBManager<EntityManager> manager = DBHolder.getDbManager();
      List<StockLevelBean> stock = manager.executeTransaction(new JPAReturnableTransaction<List<StockLevelBean>>(){
         @Override
         public List<StockLevelBean> doTransaction(EntityManager entityManager) throws Exception {
            return entityManager.createNamedQuery("getStockLevelsForProduct").setParameter("id", product.getId()).getResultList();
         }
      });
      setStockLevels(stock);
   }

   public List<StockLevelBean> getStockLevels() {
      return stockLevels;
   }

   public void setStockLevels(List<StockLevelBean> stockLevels) {
      List<StockLevelBean> oldStockLevels = this.stockLevels;
      this.stockLevels = stockLevels;
      support.firePropertyChange("stockLevels", oldStockLevels, this.stockLevels);
   }

   public void addPropertyChangeListener(PropertyChangeListener listener) {
      support.addPropertyChangeListener(listener);
   }

   public void removePropertyChangeListener(PropertyChangeListener listener) {
      support.removePropertyChangeListener(listener);
   }

   public PropertyChangeListener[] getPropertyChangeListeners() {
      return support.getPropertyChangeListeners();
   }

   public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
      support.addPropertyChangeListener(propertyName, listener);
   }

   public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
      support.removePropertyChangeListener(propertyName, listener);
   }

   public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
      return support.getPropertyChangeListeners(propertyName);
   }

   public boolean hasListeners(String propertyName) {
      return support.hasListeners(propertyName);
   }
}
