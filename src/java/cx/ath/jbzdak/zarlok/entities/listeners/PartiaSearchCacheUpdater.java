package cx.ath.jbzdak.zarlok.entities.listeners;

import cx.ath.jbzdak.jpaGui.Transaction;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCache;
import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class PartiaSearchCacheUpdater {

	private static DBManager manager;

	@PrePersist @PreUpdate
	public void updatePSC(final Partia p){
        Transaction.execute(manager, new Transaction() {
            @Override
            public void doTransaction(EntityManager entityManager) throws Exception {
                ProductSearchCache cache = new ProductSearchCache(p.getProdukt().getNazwa(), p.getSpecyfikator(), p.getJednostka(),  p.getProdukt().getId());
                entityManager.persist(cache);
            }
        });
	}

	public static DBManager getManager() {
		return manager;
	}

	public static void setManager(DBManager manager) {
		PartiaSearchCacheUpdater.manager = manager;
	}
}
