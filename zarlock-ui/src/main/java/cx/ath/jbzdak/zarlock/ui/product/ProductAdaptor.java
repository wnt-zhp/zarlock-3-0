package cx.ath.jbzdak.zarlock.ui.product;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.autoComplete.AutoCompleteAdaptor;
import cx.ath.jbzdak.jpaGui.ui.autoComplete.DbAdaptor;
import cx.ath.jbzdak.zarlok.entities.Product;
import org.jdesktop.swingx.autocomplete.ComboBoxAdaptor;

import javax.persistence.EntityManager;
import java.util.Collection;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-13
 */
public class ProductAdaptor extends DbAdaptor<Product> {

   public ProductAdaptor(DBManager<EntityManager> manager) {
      super(manager);
   }

   @Override
   protected Collection<Product> doInBackground(EntityManager query) {
      return query.createNamedQuery("getProductByName").setParameter("name", getFilter()).getResultList();
   }
}
