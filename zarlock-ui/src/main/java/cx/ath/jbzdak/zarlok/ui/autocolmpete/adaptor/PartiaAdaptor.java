package cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.ui.autoComplete.DbAdaptor;
import static cx.ath.jbzdak.jpaGui.Utils.makeLogger;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCache;
import cx.ath.jbzdak.zarlok.ui.formatted.formatters.ProductSearchCacheFormatter;
import static org.apache.commons.lang.StringUtils.defaultString;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.*;

/**
 * Adaptor przy dodawaniu partii z {@link ProductSearchCache}
 * @author jb
 *
 */
public class PartiaAdaptor extends DbAdaptor<ProductSearchCache> {

	private static final long serialVersionUID = 1L;

	private static final Logger log = makeLogger();

	private ProductSearchCacheFormatter formatter
		= new ProductSearchCacheFormatter();

	public PartiaAdaptor(DBManager manager) {
		super(manager);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<ProductSearchCache> doInBackground(EntityManager entityManager) {
		ProductSearchCache filter;
		try {
			filter = formatter.parseValue(getFilter());
			if(filter.getNazwaProduktu()!=null && filter.getJednostka()!=null && filter.getSpecyfikator()!=null){
				return getResultsList(entityManager, filter);
			}
			return filterResultList(getResultsList(entityManager, filter), filter);
		} catch (Exception e) {
			log.warn("", e);
			return Collections.emptyList();
		}
	}


    protected List<ProductSearchCache> getResultsList(EntityManager entityManager, ProductSearchCache filter){
        Query q = entityManager.createNamedQuery("filterProductSearchCache");
        q.setParameter("nazwaProduktu", defaultString(filter.getNazwaProduktu()));
        q.setParameter("jednostka",  defaultString(filter.getJednostka()));
        q.setParameter("specyfikator",  defaultString(filter.getSpecyfikator()));
        return q.getResultList();
    }

	private List<ProductSearchCache> filterResultList(List<ProductSearchCache> unfiltered, ProductSearchCache filter){
	    Set<ProductSearchCache> pscSet = new TreeSet<ProductSearchCache>(new Compare());
		if(unfiltered.size()<20){
			return unfiltered;
		}
		if(filter.getJednostka()==null){
			for(ProductSearchCache psc : unfiltered){
				psc.setJednostka(null);
				pscSet.add(psc);
			}
			unfiltered = new ArrayList<ProductSearchCache>(pscSet);
			if(pscSet.size()<20){
				return unfiltered;
			}
		}
		if(filter.getSpecyfikator()==null){
			for(ProductSearchCache psc : unfiltered){
				psc.setSpecyfikator(null);
				pscSet.add(psc);
			}
			unfiltered = new ArrayList<ProductSearchCache>(pscSet);
		}
		return unfiltered;

	}

	private static class Compare implements Comparator<ProductSearchCache>, Serializable{

		private static final long serialVersionUID = 1L;

		@Override
		public int compare(ProductSearchCache o1, ProductSearchCache o2) {
			int result;
			if((result = Utils.compare(o1.getNazwaProduktu(), o2.getNazwaProduktu()))!=0){
				return result;
			}
			if((result = Utils.compare(o1.getSpecyfikator(), o2.getSpecyfikator()))!=0){
				return result;
			}
			if((result = Utils.compare(o1.getJednostka(), o2.getJednostka()))!=0){
				return result;
			}
			return 0;
		}

	}

}
