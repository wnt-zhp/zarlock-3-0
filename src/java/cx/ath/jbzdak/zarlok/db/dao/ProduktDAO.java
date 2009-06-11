package cx.ath.jbzdak.zarlok.db.dao;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.dao.AbstractDAO;
import cx.ath.jbzdak.zarlok.entities.Produkt;

public class ProduktDAO extends AbstractDAO<Produkt>{

	public ProduktDAO(DBManager manager) {
		super(manager, Produkt.class);
	}
 
}
