package cx.ath.jbzdak.zarlok.entities.listeners;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.Transaction;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCache;
import cx.ath.jbzdak.zarlok.entities.Produkt;

import javax.persistence.EntityManager;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

public class ProductSearchCacheUpdater {

   private static DBManager<EntityManager> manager;

   @PostPersist @PostUpdate
   public void updatePSC(final Produkt p){
      manager.executeTransaction(new Transaction<EntityManager>() {
         @Override
         public void doTransaction(EntityManager entityManager) throws Exception {
            ProductSearchCache cache = new ProductSearchCache(p.getNazwa(), p.getJednostka(), p.getId());
            entityManager.persist(cache);
         }
      });
   }

   public static DBManager<EntityManager> getManager() {
      return manager;
   }

   public static void setManager(DBManager<EntityManager>  manager) {
      ProductSearchCacheUpdater.manager = manager;
   }

}
