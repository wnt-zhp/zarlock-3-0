package cx.ath.jbzdak.zarlok.entities.listeners;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.zarlok.entities.Batch;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class PartiaSearchCacheUpdater {

	private static DBManager<EntityManager> manager;

	@PrePersist @PreUpdate
	public void updatePSC(final Batch p){

//        JPATransaction.execute(manager, new Transaction<EntityManager>() {
//            @Override
//            public void doTransaction(EntityManager entityManager) throws Exception {
//                ProductSearchCache cache = new ProductSearchCache(p.getProdukt().getName(), p.getSpecifier(), p.getUnit(),  p.getProdukt().getId());
//                entityManager.persist(cache);
//            }
//        });
	}

	public static DBManager getManager() {
		return manager;
	}

	public static void setManager(DBManager manager) {
		PartiaSearchCacheUpdater.manager = manager;
	}
}
