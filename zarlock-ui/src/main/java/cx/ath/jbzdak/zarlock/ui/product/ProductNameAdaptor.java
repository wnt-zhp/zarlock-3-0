package cx.ath.jbzdak.zarlock.ui.product;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.autoComplete.DbAdaptor;

import javax.persistence.EntityManager;
import java.util.Collection;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-13
 */
public class ProductNameAdaptor extends DbAdaptor<String> {

   public ProductNameAdaptor(DBManager<EntityManager> manager) {
      super(manager);
   }

   @Override
   protected Collection<String> doInBackground(EntityManager query) {
      return query.createNamedQuery("getProductNamesByName").setParameter("name", getFilter()).getResultList();
   }
}
