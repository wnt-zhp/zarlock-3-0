package cx.ath.jbzdak.zarlok.ui.wyprowadzenie;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.dao.DAO;
import cx.ath.jbzdak.jpaGui.ui.table.EditableTableModel;
import cx.ath.jbzdak.zarlok.entities.Danie;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.Wyprowadzenie;

import javax.persistence.EntityManager;
import javax.swing.*;
import java.math.MathContext;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jun 28, 2009
 */
public class WyprowadzeniaEditTableModel extends EditableTableModel<Wyprowadzenie> {

   private final DAO<Partia> partiaDao;

   private final DAO<Danie> danieDao;

   public WyprowadzeniaEditTableModel(DBManager dbManager, Class<Wyprowadzenie> clazz, JTable table) {
      super(dbManager, clazz, table);
      partiaDao = dbManager.getDao(Partia.class);
      danieDao = dbManager.getDao(Danie.class);
   }

   @Override
   protected boolean compare(Wyprowadzenie changed, Wyprowadzenie orig) {
      return false;
   }

   @Override
   protected void removeEntry(Wyprowadzenie wyprowadzenie, EntityManager manager) {
      partiaDao.setEntity(wyprowadzenie.getPartia());
      partiaDao.getEntity().setIloscTeraz(partiaDao.getEntity().getIloscTeraz().add(wyprowadzenie.getIloscJednostek(), MathContext.DECIMAL32));
      partiaDao.update();
      if(wyprowadzenie.getDanie()!=null){
         danieDao.setEntity(wyprowadzenie.getDanie());
         danieDao.getEntity().setKoszt(danieDao.getEntity().getKoszt().subtract(wyprowadzenie.getWartosc()));
         danieDao.update();
      }
   }

   @Override
   protected void preMergeEntry(Wyprowadzenie wyprowadzenie, EntityManager entityManager) {
      throw  new UnsupportedOperationException();
   }

   @Override
   protected void persistingEntityEntry(Wyprowadzenie wyprowadzenie, EntityManager manager) {
    throw  new UnsupportedOperationException();
   }

   @Override
   public boolean isEditingDone(int idx) {
      return true;
   }


}
