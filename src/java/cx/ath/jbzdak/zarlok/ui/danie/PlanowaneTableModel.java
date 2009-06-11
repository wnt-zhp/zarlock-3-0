package cx.ath.jbzdak.zarlok.ui.danie;


import cx.ath.jbzdak.jpaGui.Transaction;
import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.table.EditableTableModel;
import cx.ath.jbzdak.zarlok.entities.Danie;
import cx.ath.jbzdak.zarlok.entities.PlanowaneWyprowadzenie;
import javax.persistence.EntityManager;
import javax.swing.JTable;
import static org.apache.commons.lang.StringUtils.isEmpty;

public class PlanowaneTableModel extends EditableTableModel<PlanowaneWyprowadzenie>{

	private Danie danie;

	public PlanowaneTableModel(DBManager dbManager, JTable table) {
		super(dbManager, PlanowaneWyprowadzenie.class, table);
	}

	@Override
	protected boolean compare(PlanowaneWyprowadzenie changed,
			PlanowaneWyprowadzenie orig) {
		return Utils.equals(changed.getProdukt(), orig.getProdukt()) &&
			Utils.equals(changed.getSpecyfikator(), orig.getSpecyfikator()) &&
			Utils.equals(changed.getJednostka(), orig.getJednostka()) &&
			Utils.equals(changed.getIloscJednostek(), orig.getIloscJednostek());
	}

	@Override
	public boolean isEditingDone(int idx) {
		PlanowaneWyprowadzenie w = getEntities().get(idx);
		return w.getProdukt()!=null && w.getIloscJednostek()!=null && !isEmpty(w.getJednostka());
	}

	@Override
	protected void persistingEntityEntry(PlanowaneWyprowadzenie t,
			EntityManager manager) {
		//Nic do roboty

	}

	@Override
	protected void preMergeEntry(PlanowaneWyprowadzenie t,
			EntityManager entityManager) {
			danie = entityManager.find(Danie.class, danie.getId());
			danie.getPlanowaneWyprowadzenia().add(t);
	}

	@Override
	protected void removeEntry(PlanowaneWyprowadzenie t, EntityManager manager) {
		danie = manager.find(Danie.class, danie.getId());
		danie.getPlanowaneWyprowadzenia().remove(t);
	}

	public Danie getDanie() {
		return danie;
	}

	public void setDanie(final Danie danie) {
		this.danie = danie;
		Transaction.execute(manager, new Transaction(){
			@Override
			public void doTransaction(EntityManager entityManager) {
				setEntities(danie.getPlanowaneWyprowadzenia());
			}
		});
	}

}
