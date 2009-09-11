package cx.ath.jbzdak.zarlok.db.dao;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.dao.AbstractDAO;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.Wyprowadzenie;

public class WyprowadzenieDAO extends AbstractDAO<Wyprowadzenie> {

	public WyprowadzenieDAO(DBManager manager) {
		super(manager, Wyprowadzenie.class);
		setAutoCreateEntity(true);
	}

	@Override
	public void setEntity(Wyprowadzenie entity) {
		super.setEntity(entity);
	}

	@Override
	public void beginTransaction() {
		if(entityManager == null || !entityManager.isOpen()){
			entityManager = manager.createEntityManager();
			transactionWideEntityManager = true;
		}else{
			transactionWideEntityManager = false;
		}
		Partia p = 	getEntity().getPartia();
		if(p!=null){
			if(!getEntityManager().contains(p)){
				getEntity().setPartia(getEntityManager().merge(p));
			}
		}
		super.beginTransaction();
	}
	

}
