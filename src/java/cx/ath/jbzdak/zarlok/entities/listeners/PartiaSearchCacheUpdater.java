package cx.ath.jbzdak.zarlok.entities.listeners;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.zarlok.entities.Partia;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class PartiaSearchCacheUpdater {

	private static DBManager manager;

	@PrePersist @PreUpdate
    //fixme włączyć i zobaczyć co się stanie
	public void updatePSC(Partia p){
//		new InternalError().printStackTrace();
//		EntityManager eManager = manager.createEntityManager();
//		try{
//			eManager.getTransaction().begin();
//			ProductSearchCache cache = new ProductSearchCache(p.getProdukt().getNazwa(), p.getSpecyfikator(), p.getJednostka(),  p.getProdukt().getId());
//			eManager.persist(cache);
//		}catch (RuntimeException e) {
//			eManager.getTransaction().rollback();
//			throw e;
//		}
//		finally{
//			eManager.close();
//		}
	}

	public static DBManager getManager() {
		return manager;
	}

	public static void setManager(DBManager manager) {
		PartiaSearchCacheUpdater.manager = manager;
	}
}
