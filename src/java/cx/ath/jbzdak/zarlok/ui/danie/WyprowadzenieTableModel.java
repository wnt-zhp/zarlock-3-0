package cx.ath.jbzdak.zarlok.ui.danie;

import cx.ath.jbzdak.jpaGui.Transaction;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.table.EditableTableModel;
import cx.ath.jbzdak.zarlok.entities.Danie;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.Wyprowadzenie;
import cx.ath.jbzdak.zarlok.entities.WyprowadzenieUtils;
import javax.persistence.EntityManager;
import javax.swing.JTable;

public class WyprowadzenieTableModel extends EditableTableModel<Wyprowadzenie>{

	public WyprowadzenieTableModel(DBManager dbManager, JTable table) {
		super(dbManager,Wyprowadzenie.class, table);
	}

	private Danie danie;

	@Override
	protected void removeEntry(Wyprowadzenie t, EntityManager manager) {
      t.setDanie(null);
		danie = manager.find(Danie.class, danie.getId());
//		danie.getWyprowadzenia().remove(t);
      danie.updateKoszt();
      danie.getPosilek().recalculateCost();
	}

	@Override
	protected void persistingEntityEntry(Wyprowadzenie t, EntityManager manager) {
		t.setDataWyprowadzenia(danie.getPosilek().getDzien().getData());
		t.setTytulem(WyprowadzenieUtils
				.getTytulemFromDanie(danie));
		danie = manager.find(Danie.class, danie.getId());
      t.setDanie(danie);
      danie.updateKoszt();
      danie.getPosilek().recalculateCost();
	}

	@Override
	protected void preMergeEntry(Wyprowadzenie t, EntityManager entityManager) {
		t.setPartia(entityManager.find(Partia.class, t.getPartia().getId()));
      danie = entityManager.merge(danie);
      danie.updateKoszt();
      danie.getPosilek().recalculateCost();
      t.getPartia().recalculateIloscTeraz();
	}

	public void setDanie(final Danie danie) {
		Transaction.execute(manager, new Transaction(){
			@Override
			public void doTransaction(EntityManager entityManager) {
				WyprowadzenieTableModel.this.danie = entityManager.find(Danie.class, danie.getId());
				setEntities(WyprowadzenieTableModel.this.danie.getWyprowadzenia());
			}
		});
	}

	@Override
	public boolean isEditingDone(int idx) {
		//Wyprowadzenie w = getEntities().get(idx);
		//return w.getPartia()!=null && w.getIloscJednostek() != null;
      return  true;
	}

	@Override
	protected boolean compare(Wyprowadzenie changed, Wyprowadzenie orig) {
		return !WyprowadzenieUtils.compareContents(changed, orig);
	}

	protected Danie getDanie() {
		return danie;
	}
}