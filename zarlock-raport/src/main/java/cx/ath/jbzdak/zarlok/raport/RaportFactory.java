package cx.ath.jbzdak.zarlok.raport;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.JPAReturnableTransaction;
import cx.ath.jbzdak.jpaGui.db.JPATransaction;
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
public class RaportFactory {

   private static final Logger LOGGER = Utils.makeLogger();

   @SuppressWarnings({"FieldCanBeLocal"})
   private DBManager manager;

   private final ZZFactory zzFactory = new ZZFactory();

   private final StanMagazynuFactory stanMagazynuFactory;

   private final KartotekaRaportFactory kartotekaRaportFactory;

   public RaportFactory() {
      this.stanMagazynuFactory = new StanMagazynuFactory();
      this.kartotekaRaportFactory = new KartotekaRaportFactory();
   }


   public void printStanMagazynu(Dzien d)throws RaportException{
     stanMagazynuFactory.printStanMag(d);
   }


   public void saveStanMagazynu(Dzien d)throws RaportException{
      stanMagazynuFactory.saveStanMagazynu(d, null);
   }

   public void printZZ(Dzien d) throws RaportException{
      zzFactory.printZZ(d);
   }

   public void saveZZ(Dzien d) throws RaportException{
      zzFactory.saveZZ(d);
   }

   public void saveKartoteki() throws RaportException {kartotekaRaportFactory.saveKartoteki();}

   public void saveDokumentacja() throws RaportException{
      stanMagazynuFactory.cleanStanMagazynuFolder();
      zzFactory.cleanZZFolder();
      List<Dzien> dni = JPATransaction.execute(manager, new JPAReturnableTransaction<List<Dzien>>() {
         @Override
         public List<Dzien> doTransaction(EntityManager entityManager) {
            return entityManager.createQuery("SELECT d FROM Dzien d").getResultList();
         }
      });
      for(final Dzien d : dni){
         try {
            JPATransaction.execute(manager, new JPATransaction(){
               @Override
               public void doTransaction(EntityManager entityManager) throws RaportException
               {
                  Dzien d2 = entityManager.find(Dzien.class, d.getId());
                  stanMagazynuFactory.saveStanMagazynu(d2, null);
                  saveZZ(d2);
               }
            });
         } catch (TransactionException e) {
            throw (RaportException) e.getCause();
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
