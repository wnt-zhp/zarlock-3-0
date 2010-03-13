package cx.ath.jbzdak.zarlock.ui.batch;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collection;
import java.awt.Component;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.autoComplete.DbAdaptor;
import cx.ath.jbzdak.zarlock.ui.ZarlockDbAdaptor;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Feb 23, 2010
 */
public class SpecifierAdaptor extends ZarlockDbAdaptor<String> {

   public SpecifierAdaptor(Component owner) {
      super(owner);
   }

   @Override
   protected Collection<String> doInBackground(EntityManager manager) {
      Query q = manager.createNamedQuery("getSpecifiers");
      return q.getResultList();
   }

}
