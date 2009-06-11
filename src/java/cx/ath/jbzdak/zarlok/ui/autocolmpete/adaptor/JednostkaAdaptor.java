package cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor;

import cx.ath.jbzdak.jpaGui.autoComplete.AutoCompleteValueHolder;
import cx.ath.jbzdak.jpaGui.autoComplete.DbAdaptor;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Adaptor przeszukujący wszystkie jednostki w produktach.
 * @author jb
 *
 */
public class JednostkaAdaptor extends
		DbAdaptor<Set<String>, AutoCompleteValueHolder> {

	private static final long serialVersionUID = 1L;


	public JednostkaAdaptor(DBManager manager) {
		super(manager);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Set<String> doInBackground(EntityManager entityManager) {
		Set<String> set = new HashSet<String>();
		Query q = entityManager.createNamedQuery("getProduktJednostka");
		q.setParameter("jednostka", getFilter());
		set.addAll(q.getResultList());
		q = entityManager.createNamedQuery("getPartiaJednostka");
		q.setParameter("jednostka", getFilter());
		set.addAll(q.getResultList());
		return set;
	}

	@Override
	protected void done() {
		List<AutoCompleteValueHolder> list = new ArrayList<AutoCompleteValueHolder>(
				getUnsafe().size());
		for (String s : getUnsafe()) {
			list.add(new AutoCompleteValueHolder(s));
		}
		setCurentFilteredResults(list);
	}


	@Override
	public AutoCompleteValueHolder getValueHolderFromFilter() {
		if(StringUtils.isBlank(getFilter())){
			return null;
		}
		return new AutoCompleteValueHolder(getFilter(), getFilter(), true);
	}


}
