package cx.ath.jbzdak.zarlok.raport;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.ReturnableTransaction;
import cx.ath.jbzdak.jpaGui.db.Transaction;
import cx.ath.jbzdak.jpaGui.db.TransactionException;
import cx.ath.jbzdak.zarlok.entities.Dzien;
import cx.ath.jbzdak.zarlok.raport.kartoteki.KartotekaRaportFactory;
import cx.ath.jbzdak.zarlok.raport.stany.StanMagazynuFactory;
import cx.ath.jbzdak.zarlok.raport.zz.ZZFactory;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-04-28
 */
public class RaportFactoryImpl implements ReportFactory {

   private static final Logger LOGGER = Utils.makeLogger();

   @SuppressWarnings({"FieldCanBeLocal"})
   private DBManager manager;

   private final ZZFactory zzFactory = new ZZFactory();

   private final StanMagazynuFactory stanMagazynuFactory;

   private final KartotekaRaportFactory kartotekaRaportFactory;

   public RaportFactoryImpl() {
      this.stanMagazynuFactory = new StanMagazynuFactory();
      this.kartotekaRaportFactory = new KartotekaRaportFactory();
   }


   public void printStanMagazynu(Dzien d)throws ReportException {
     stanMagazynuFactory.printStanMag(d);
   }


   public void saveStanMagazynu(Dzien d)throws ReportException {
      stanMagazynuFactory.saveStanMagazynu(d, null);
   }

   public void printZZ(Dzien d) throws ReportException {
      zzFactory.printZZ(d);
   }

   public void saveZZ(Dzien d) throws ReportException {
      zzFactory.saveZZ(d);
   }

   public void saveKartoteki() throws ReportException {kartotekaRaportFactory.saveKartoteki();}

   public void saveDokumentacja() throws ReportException {
      stanMagazynuFactory.cleanStanMagazynuFolder();
      zzFactory.cleanZZFolder();
      List<Dzien> dni = (List<Dzien>) manager.executeTransaction(new ReturnableTransaction<EntityManager, List<Dzien>>() {
         @Override
         public List<Dzien> doTransaction(EntityManager entityManager) {
            return entityManager.createQuery("SELECT d FROM Dzien d").getResultList();
         }
      });
      for(final Dzien d : dni){
         try {
            manager.executeTransaction(new Transaction<EntityManager>(){
               @Override
               public void doTransaction(EntityManager entityManager) throws ReportException
               {
                  Dzien d2 = entityManager.find(Dzien.class, d.getId());
                  stanMagazynuFactory.saveStanMagazynu(d2, null);
                  saveZZ(d2);
               }
            });
         } catch (TransactionException e) {
            throw (ReportException) e.getCause();
         }

      }
      saveKartoteki();
   }

   public void setManager(DBManager manager) {
      this.manager = manager;
      stanMagazynuFactory.setManager(manager);
      kartotekaRaportFactory.setManager(manager);
   }
}
