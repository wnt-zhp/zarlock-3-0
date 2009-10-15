package cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor;

import cx.ath.jbzdak.jpaGui.ui.autoComplete.DbAdaptor;
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
public class SpecyfikatorAdaptor extends DbAdaptor<String> {

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
}
