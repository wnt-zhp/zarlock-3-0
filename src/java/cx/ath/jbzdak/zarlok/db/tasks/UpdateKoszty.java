package cx.ath.jbzdak.zarlok.db.tasks;

import cx.ath.jbzdak.jpaGui.Transaction;
import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.entities.IloscOsob;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-04-21
 */
public class UpdateKoszty extends Task<DBManager>{

   private static final Logger LOGGER = Utils.makeLogger();

   public UpdateKoszty() {
      super(0, "Update koszty");
   }

   @Override
   public void doTask(DBManager manager, @Nullable Object... o) throws Exception {
      EntityManager em = manager.createEntityManager();
      try{
         updateDania(em);
         updateKoszt(em, "fetchPosilekKoszt", "updatePosilkiKoszt");
         updateKoszt(em, "fetchDzienKoszt", "updateDzienKoszt");
      }finally {
         em.close();
      }
   }

   private void updateDania(EntityManager em) {
      Transaction.execute(em, new Transaction() {
         @Override
         public void doTransaction(EntityManager entityManager) {
            Query q = entityManager.createQuery("SELECT d.id, d.posilek.iloscOsob FROM Danie d");
            Query kosztQuery = entityManager.createQuery("SELECT SUM(w.iloscJednostek * w.partia.cena ) FROM Danie d, IN(d.wyprowadzenia ) w WHERE d.id = :id");
            Query update = entityManager.createNamedQuery("updateDanieKoszt");
            for(Object[] o : (Iterable<? extends Object[]>) q.getResultList()){
               kosztQuery.setParameter("id", o[0]);
               Number koszt = (Number) kosztQuery.getSingleResult();
               koszt=koszt==null?0:koszt; //Jeśli nie ma wyprowadzeń może być null
               Integer iloscOsob = ((IloscOsob)o[1]).getSuma();
               update.setParameter("id", o[0]);
               update.setParameter("koszt", iloscOsob==null?BigDecimal.ZERO:Utils.round(BigDecimal.valueOf(koszt.doubleValue()/iloscOsob.intValue()),2));
               update.executeUpdate();
            }
         }
      });
   }

   private void updateKoszt(EntityManager entityManager, final String fetchQueryName, final String updateQueryName){
      Transaction.execute(entityManager, new Transaction() {
         @Override
         public void doTransaction(EntityManager entityManager) {
            Query q = entityManager.createNamedQuery(fetchQueryName);
            List<Object[]> results = q.getResultList();
            int updatedRows = 0;
            Query update;
            for(Object[] result : results){
               update = entityManager.createNamedQuery(updateQueryName);
               update.setParameter("id", result[0]);
               update.setParameter("koszt", result[1]);
               updatedRows += update.executeUpdate();
            }
            LOGGER.info("UPDATED {} rows", updatedRows);
         }
      });
   }
}
