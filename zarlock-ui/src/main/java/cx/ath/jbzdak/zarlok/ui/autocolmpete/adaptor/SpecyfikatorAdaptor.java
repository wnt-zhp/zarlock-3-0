package cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor;

import cx.ath.jbzdak.jpaGui.autoComplete.AutoCompleteValueHolder;
import cx.ath.jbzdak.jpaGui.autoComplete.DbAdaptor;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import static org.apache.commons.lang.StringUtils.isEmpty;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptor przeszukujący produkty po specyfikatorze.
 * @author jb
 *
 */
public class SpecyfikatorAdaptor extends DbAdaptor<List<String>, AutoCompleteValueHolder> {

	private static final long serialVersionUID = 1L;

	public SpecyfikatorAdaptor(DBManager manager) {
		super(manager);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<String> doInBackground(EntityManager entityManager) {
		Query q = entityManager.createNamedQuery("getPartiaSpecyfikator");
		q.setParameter("specyfikator", getFilter());
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
		if(isEmpty(getFilter())){
			return null;
		}
		return new AutoCompleteValueHolder(getFilter(), getFilter(), true);
	}



}
