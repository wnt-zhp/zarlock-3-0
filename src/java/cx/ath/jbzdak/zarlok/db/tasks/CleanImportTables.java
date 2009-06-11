package cx.ath.jbzdak.zarlok.db.tasks;

import cx.ath.jbzdak.jpaGui.Transaction;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.importer.ZZExpenditure;
import cx.ath.jbzdak.zarlok.importer.ZZMeal;
import cx.ath.jbzdak.zarlok.importer.ZZProduct;
import javax.persistence.EntityManager;

import java.util.List;

public class CleanImportTables extends Task<DBManager>{

	public CleanImportTables() {
		super(0, "Wyczyścić tabelki służące do improtu danych ze starego żarłoka");

	}

	@Override
	public void doTask(DBManager t, Object... o) throws Exception {
		EntityManager entityManager = t.createEntityManager();

		for(final ZZMeal m : (List<ZZMeal>) (entityManager.createQuery("SELECT m FROM ZZMeal m ").getResultList())){
			Transaction.execute(entityManager, new Transaction(){
				@Override
				public void doTransaction(EntityManager entityManager) {
					m.getExpenditures().clear();
					entityManager.remove(m);;
				}});
		}

		for(final ZZProduct p : (List<ZZProduct>) (entityManager.createQuery("SELECT p FROM ZZProduct p").getResultList())){
			Transaction.execute(entityManager, new Transaction(){
				@Override
				public void doTransaction(EntityManager entityManager) {

					for(ZZExpenditure e : p.getExpenditures()){
						e.setProduct(null);
						entityManager.remove(e);
					}
					p.getExpenditures().clear();
					entityManager.remove(p);
				}});
		}
	}

}
