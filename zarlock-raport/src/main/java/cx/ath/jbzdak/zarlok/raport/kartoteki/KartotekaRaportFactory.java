package cx.ath.jbzdak.zarlok.raport.kartoteki;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.JPAReturnableTransaction;
import cx.ath.jbzdak.jpaGui.db.JPATransaction;
import cx.ath.jbzdak.zarlok.config.Preferences;
import cx.ath.jbzdak.zarlok.raport.Raport;
import cx.ath.jbzdak.zarlok.raport.RaportException;
import cx.ath.jbzdak.zarlok.raport.RaportExceptionForUser;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: May 6, 2009
 */
public class KartotekaRaportFactory {

   private DBManager manager;

   private Raport makeRaport(final KartotekaRaportBean kartotekaRaportBean){
      return JPATransaction.execute(manager, new JPAReturnableTransaction<Raport>() {
         @Override
         public Raport doTransaction(EntityManager entityManager) {
            Query q = entityManager.createNamedQuery("getKartotekaContentsStage2");
            q.setParameter("nazwa", kartotekaRaportBean.getNazwa());
            q.setParameter("specyfikator", kartotekaRaportBean.getSpecyfikator());
            q.setParameter("cena", kartotekaRaportBean.getCena());
            kartotekaRaportBean.setPartie(q.getResultList());
            KartotekaRaport raport = new KartotekaRaport();
            raport.setData(kartotekaRaportBean);
            return raport;
         }
      });
   }



   private void saveKartoteka(KartotekaRaportBean kartotekaRaportBean) throws RaportException {
      Raport raport = makeRaport(kartotekaRaportBean);
      try {
         raport.save(getKartotekaFile(kartotekaRaportBean));
      } catch (IOException e) {
         throw new RaportExceptionForUser("Nie można utworzyć pliku z kartotekami, spróbój jeszcze raz", e);
      }
   }

   private File getKartotekaFile(KartotekaRaportBean bean) throws IOException {
      int ordinal = 0;
      File karotekaFile;
      do{
         karotekaFile = getKartotekaFile(bean, ordinal++);
      }while (!karotekaFile.createNewFile());
      return karotekaFile;
   }

   private File getKartotekaFile(KartotekaRaportBean bean, int ordinal){
      return new File(Preferences.getKatrotekaFolder(),
                      Utils.cleanFileName("" + bean.getNazwa() + "_" + bean.getSpecyfikator() + "_" + ordinal) +  ".pdf");
   }

  public void saveKartoteki() throws RaportException {
     for(String filename : Preferences.getKatrotekaFolder().list()){
        File f = new File(Preferences.getKatrotekaFolder(), filename);
        if(!f.delete()){
           throw new RaportExceptionForUser("Nie można usunąć pliku ze starą kartoteką o nazwie: " +
                   "" + f + ". Usuń go ręcznie i spróbuj jeszcze raz");
        }
     }
     List<KartotekaRaportBean> kartoteki = JPATransaction.execute(manager, new JPAReturnableTransaction<List<KartotekaRaportBean>>() {
        @Override
        public List<KartotekaRaportBean> doTransaction(EntityManager entityManager) {
           return entityManager.createNamedQuery("getKartotekiContents").getResultList();
        }
     });
     for(KartotekaRaportBean kartotekaRaportBean : kartoteki){
        saveKartoteka(kartotekaRaportBean);
     }
  }

   public void setManager(DBManager manager) {
      this.manager = manager;
   }
}
