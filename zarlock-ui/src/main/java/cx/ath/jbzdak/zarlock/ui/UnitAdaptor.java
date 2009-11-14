package cx.ath.jbzdak.zarlock.ui;

import cx.ath.jbzdak.jpaGui.ui.autoComplete.AutoCompleteAdaptor;
import cx.ath.jbzdak.jpaGui.ui.autoComplete.DbAdaptor;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.zarlok.DBHolder;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class UnitAdaptor extends DbAdaptor<String> {

   public UnitAdaptor() {
      super(DBHolder.getDbManager());
   }

   @Override
   protected Collection<String> doInBackground(EntityManager query) {
      Set<String> units = new HashSet<String>();
      units.addAll(query.createQuery("SELECT DISTINCT b.unit FROM Batch b").getResultList());
      units.addAll(query.createQuery("SELECT DISTINCT p.unit FROM Product p").getResultList());
      return units;
   }
}
