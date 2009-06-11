package cx.ath.jbzdak.zarlok.entities.listeners;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCache;
import cx.ath.jbzdak.zarlok.entities.Produkt;
import javax.persistence.EntityManager;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

public class ProductSearchCacheUpdater {

	private static DBManager manager;

	@PostPersist @PostUpdate
	public void updatePSC(Produkt p){
		EntityManager eManager = manager.createEntityManager();
		try{
			eManager.getTransaction().begin();
			ProductSearchCache cache = new ProductSearchCache(p.getNazwa(), p.getJednostka(), p.getId());
			eManager.persist(cache);
			eManager.getTransaction().commit();
		}catch (RuntimeException e) {
			eManager.getTransaction().rollback();
			throw e;
		}
		finally{
			eManager.close();
		}
	}

	public static DBManager getManager() {
		return manager;
	}

	public static void setManager(DBManager manager) {
		ProductSearchCacheUpdater.manager = manager;
	}

}
