package cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor;

import cx.ath.jbzdak.jpaGui.autoComplete.AutoCompleteValueHolder;
import cx.ath.jbzdak.jpaGui.autoComplete.DbAdaptor;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.zarlok.entities.Produkt;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptor przeszukujący produkty po nazwie.
 * @author jb
 *
 */
public class ProductAdaptor extends DbAdaptor<List<Produkt>, AutoCompleteValueHolder> {

	private static final long serialVersionUID = 1L;

	public ProductAdaptor(DBManager manager) {
		super(manager);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<Produkt> doInBackground(EntityManager manager) {
		Query q = manager.createNamedQuery("getProduktByNazwa");
		q.setParameter("nazwa", getFilter());
		return q.getResultList();
	}

	@Override
	protected void done() {
		List<AutoCompleteValueHolder> list = new  ArrayList<AutoCompleteValueHolder>(getUnsafe().size());
		for(Produkt p : getUnsafe()){
			list.add(new AutoCompleteValueHolder(p.getNazwa(), p));
		}
		setCurentFilteredResults(list);
	}

	@Override
	public AutoCompleteValueHolder getValueHolderFromFilter() {
		return null;
	}


}
