package cx.ath.jbzdak.zarlock.ui.batch;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.awt.Component;
import java.util.Collection;

import cx.ath.jbzdak.zarlock.ui.ZarlockDbAdaptor;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 10, 2010
 */
public class UnitAdapter extends ZarlockDbAdaptor<String>{

   public UnitAdapter(Component owner) {
      super(owner);
   }

   @Override
   protected Collection<String> doInBackground(EntityManager manager) {
      Query query = manager.createNamedQuery("getUnits");
      return query.getResultList();
   }
}
