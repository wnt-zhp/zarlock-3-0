package cx.ath.jbzdak.zarlok.ui.produkt;

import cx.ath.jbzdak.jpaGui.autoComplete.AutoCompleteValueHolder;
import cx.ath.jbzdak.jpaGui.autoComplete.SwingWorkerAdaptor;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.List;

public class ProductAutocompleteAdaptor extends SwingWorkerAdaptor<List<String>, AutoCompleteValueHolder> {

	private static final long serialVersionUID = 1L;

	private EntityManager entityManager;

	public ProductAutocompleteAdaptor() {
		super();
	}

	public ProductAutocompleteAdaptor(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@SuppressWarnings("unchecked")
	@Override
	protected List<String> doInBackground() {
		Query q = entityManager.createNamedQuery("getProduktNazwa");
		q.setParameter("nazwa", this.getFilter());
		return q.getResultList();
	}


	@Override
	protected void done() {
		List<AutoCompleteValueHolder> list = new ArrayList<AutoCompleteValueHolder>(getUnsafe().size());
		for(String s : getUnsafe()){
			list.add(new AutoCompleteValueHolder(s));
		}
		setCurentFilteredResults(list);

	}

	@Override
	public AutoCompleteValueHolder getValueHolderFromFilter() {
		return null;
	}



}
