package cx.ath.jbzdak.zarlok.db.tasks;

import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.db.ZarlockDBManager;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCache;
import cx.ath.jbzdak.zarlok.entities.Produkt;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;

import java.util.List;

/**
 * Wypełnia {@link ProductSearchCache} odpowiednimi
 * encjami.
 * @author jb
 *
 */
public class FillProductCacheTask extends Task<ZarlockDBManager> {

	public FillProductCacheTask() {
		super(0, "Fill product cache");
	}

	/**
	 * Działą tak: zczytuje odpowiednie dane z tabelek {@link Produkt} i {@link Partia}
	 * po czym wywala duplikaty.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doTask(ZarlockDBManager t, Object... o) throws Exception {
		EntityManager em = t.createEntityManager();
		em.getTransaction().begin();
		em.setFlushMode(FlushModeType.AUTO);
		Query q = em.createNamedQuery("getProductSearchCache");
		for(ProductSearchCache psc : (List<ProductSearchCache>) q.getResultList()){
			em.persist(psc);
		}
		q = em.createNamedQuery("getProductSearchCache2");
		for(ProductSearchCache psc : (List<ProductSearchCache>) q.getResultList()){
			em.persist(psc);
		}
		em.createNamedQuery("finishProductSearchCache").executeUpdate();
		em.getTransaction().commit();
		em.close();
	}

}
