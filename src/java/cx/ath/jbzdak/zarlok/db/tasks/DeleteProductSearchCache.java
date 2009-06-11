package cx.ath.jbzdak.zarlok.db.tasks;

import static cx.ath.jbzdak.jpaGui.Utils.makeLogger;
import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.db.ZarlockDBManager;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCache;
import javax.persistence.EntityManager;
import org.slf4j.Logger;

/**
 * Task czyszczący kesz {@link ProductSearchCache}. Encje te
 * są kasowane przy wyłączaniu i (na wszelk wypadek) włączaniu
 * bazy danych. Task ten powinien mieć mniejszy priorytet niż
 * {@link FillProductCacheTask}.
 * @author jb
 *
 */
public class DeleteProductSearchCache extends Task<ZarlockDBManager> {

	private final static Logger logger = makeLogger();

	public DeleteProductSearchCache() {
		super(-100, "DeleteProductSearchCache");
	}

	@Override
	public void doTask(ZarlockDBManager t, Object... o) throws Exception {
		EntityManager em = t.createEntityManager();
		em.getTransaction().begin();
		int results = em.createNamedQuery("deleteProductSearchCache").executeUpdate();
		logger.info("Deleted {} entries", results);
		em.getTransaction().commit();
		em.close();
	}

}
