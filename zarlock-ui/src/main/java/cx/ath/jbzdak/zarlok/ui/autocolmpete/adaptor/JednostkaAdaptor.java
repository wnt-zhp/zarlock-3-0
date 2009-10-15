package cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.autoComplete.DbAdaptor;
import org.apache.commons.lang.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Adaptor przeszukujący wszystkie jednostki w produktach.
 * @author jb
 *
 */
public class JednostkaAdaptor extends DbAdaptor<String> {

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
}
