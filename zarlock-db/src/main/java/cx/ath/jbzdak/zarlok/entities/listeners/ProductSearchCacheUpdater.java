package cx.ath.jbzdak.zarlok.entities.listeners;

import cx.ath.jbzdak.jpaGui.Transaction;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCache;
import cx.ath.jbzdak.zarlok.entities.Produkt;

import javax.persistence.EntityManager;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

public class ProductSearchCacheUpdater {

	private static DBManager manager;

	@PostPersist @PostUpdate
	public void updatePSC(final Produkt p){
        Transaction.execute(manager, new Transaction() {
            @Override
            public void doTransaction(EntityManager entityManager) throws Exception {
              ProductSearchCache cache = new ProductSearchCache(p.getNazwa(), p.getJednostka(), p.getId());
			  entityManager.persist(cache);
            }
        });

	}

	public static DBManager getManager() {
		return manager;
	}

	public static void setManager(DBManager manager) {
		ProductSearchCacheUpdater.manager = manager;
	}

}
