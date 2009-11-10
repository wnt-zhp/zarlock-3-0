package cx.ath.jbzdak.zarlok.db.tasks;

import static cx.ath.jbzdak.jpaGui.Utils.makeLogger;
import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.db.ZarlockDBManager;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.slf4j.Logger;

import java.util.List;

public class UpdateIloscTeraz extends Task<ZarlockDBManager> {

   private static final Logger LOG = makeLogger();

   public UpdateIloscTeraz() {
      super(0, "UPDATE_ILOSC_TEARZ");
   }

   @SuppressWarnings("unchecked")
   @Override
   public void doTask(ZarlockDBManager t, Object... o) throws Exception {
      EntityManager em = t.createEntityManager();
      try {
         em.getTransaction().begin();
         em.createQuery("UPDATE Partia p SET p.iloscTeraz = p.iloscPocz WHERE p.wyprowadzenia IS EMPTY")
                 .executeUpdate();
         List<Long> results = em.createQuery("SELECT p.id FROM Partia p WHERE p.wyprowadzenia IS NOT EMPTY")
                 .getResultList();
         for (Long i : results) {
            Number number = (Number) em.createQuery(
                    "SELECT SUM(w.iloscJednostek) FROM Expenditure w WHERE w.partia.id = :id GROUP BY w.partia")
                    .setParameter("id", i).getSingleResult();
            Query update = em
                    .createQuery("UPDATE Partia p SET p.iloscTeraz = (p.iloscPocz - :ilosc) WHERE (p.id = :id)")
                    .setParameter("id", i).setParameter("ilosc", number);
            update.executeUpdate();
         }
         em.getTransaction().commit();
         LOG.info("Zmienino {} wierszy", results.size());
      } catch (Exception e) {
         em.getTransaction().rollback();
         LOG.error("Error during updating iloscTeraz", e);
      } finally {
         em.close();
      }


   }

}

