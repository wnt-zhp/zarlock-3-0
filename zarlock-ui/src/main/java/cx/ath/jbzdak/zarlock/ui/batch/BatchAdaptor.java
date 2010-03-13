package cx.ath.jbzdak.zarlock.ui.batch;

import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.autoComplete.DbAdaptor;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCache;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCacheUtils;

import static cx.ath.jbzdak.jpaGui.Utils.makeLogger;
import static org.apache.commons.lang.StringUtils.defaultString;

/**
 * Adaptor przy dodawaniu partii z {@link ProductSearchCache}
 * @author jb
 *
 */
public class BatchAdaptor extends DbAdaptor<ProductSearchCache> {

	private static final long serialVersionUID = 1L;

	private static final Logger log = makeLogger();

	public BatchAdaptor(DBManager manager) {
		super(manager);
	}

   @SuppressWarnings("unchecked")
	@Override
	protected List<ProductSearchCache> doInBackground(EntityManager entityManager) {
		ProductSearchCache filter;
		try {
			filter = ProductSearchCacheUtils.parse(getFilter());
			return getResultsList(entityManager, filter);
		} catch (Exception e) {
			log.warn("", e);
			return Collections.emptyList();
		}
	}


    protected List<ProductSearchCache> getResultsList(EntityManager entityManager, ProductSearchCache filter){
        Query q = entityManager.createNamedQuery("filterProductSearchCache");
        q.setParameter("nazwaProduktu", defaultString(filter.getProductName()));
        q.setParameter("jednostka",  defaultString(filter.getUnit()));
        q.setParameter("specyfikator",  defaultString(filter.getSpecifier()));
        return q.getResultList();
    }

	private List<ProductSearchCache> filterResultList(List<ProductSearchCache> unfiltered, ProductSearchCache filter){
//	    Set<ProductSearchCache> pscSet = new TreeSet<ProductSearchCache>(new Compare());
//		if(unfiltered.size()<20){
//			return unfiltered;
//		}
//		if(filter.getJednostka()==null){
//			for(ProductSearchCache psc : unfiltered){
//				psc.setJednostka(null);
//				pscSet.add(psc);
//			}
//			unfiltered = new ArrayList<ProductSearchCache>(pscSet);
//			if(pscSet.size()<20){
//				return unfiltered;
//			}
//		}
//		if(filter.getSpecyfikator()==null){
//			for(ProductSearchCache psc : unfiltered){
//				psc.setSpecyfikator(null);
//				pscSet.add(psc);
//			}
//			unfiltered = new ArrayList<ProductSearchCache>(pscSet);
//		}
		return unfiltered;

	}
}
