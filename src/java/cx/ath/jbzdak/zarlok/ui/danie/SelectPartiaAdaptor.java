package cx.ath.jbzdak.zarlok.ui.danie;

import static cx.ath.jbzdak.jpaGui.Utils.makeLogger;
import cx.ath.jbzdak.jpaGui.autoComplete.AutoCompleteValueHolder;
import cx.ath.jbzdak.jpaGui.autoComplete.DbAdaptor;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCache;
import cx.ath.jbzdak.zarlok.ui.formatted.formatters.ProductSearchCacheFormatter;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectPartiaAdaptor extends
		DbAdaptor<List<AutoCompleteValueHolder>, AutoCompleteValueHolder> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = makeLogger();

	public SelectPartiaAdaptor(DBManager manager) {
		super(manager);
	}

	private final ProductSearchCacheFormatter productSearchCacheFormatter = new ProductSearchCacheFormatter();

	@SuppressWarnings("unchecked")
	@Override
	protected List<AutoCompleteValueHolder> doInBackground(
			EntityManager entityManager) {
		ProductSearchCache psc;
		try {
			psc = productSearchCacheFormatter.parseValue(getFilter());
			Query q = entityManager.createNamedQuery("getParieForWydanie");
			q.setParameter("nazwa", psc.getNazwaProduktu());
			q.setParameter("specyfikator", psc.getSpecyfikator());
			q.setParameter("jednostka", psc.getJednostka());
			List<Partia> results = q.getResultList();
			List<AutoCompleteValueHolder> list =
				new ArrayList<AutoCompleteValueHolder>(results.size());
			for (Partia p : results) {
				list.add(new AutoCompleteValueHolder(p.getSearchFormat(), p));
			}
			return list;
		} catch (Exception e) {
			logger.warn("", e);
			return Collections.emptyList();
		}
	}

	@Override
	protected void done() {
		setCurentFilteredResults(getUnsafe());
	}

	@Override
	public AutoCompleteValueHolder getValueHolderFromFilter() {
		return null;
	}
}
