package cx.ath.jbzdak.zarlok.raport.stany;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.ReturnableTransaction;
import cx.ath.jbzdak.zarlok.config.Preferences;
import cx.ath.jbzdak.zarlok.entities.Dzien;
import cx.ath.jbzdak.zarlok.raport.Raport;
import cx.ath.jbzdak.zarlok.raport.ReportException;
import cx.ath.jbzdak.zarlok.raport.ReportExceptionForUser;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-05-05
 */
public class StanMagazynuFactory {

   private  DBManager manager;

   private final SimpleDateFormat simpleDateFormat =
           new SimpleDateFormat("yyyy-MM-dd");

   public void setManager(DBManager manager) {
      this.manager = manager;
   }

   Raport createRaport(final Dzien dzien){
      return (Raport) manager.executeTransaction(new ReturnableTransaction<EntityManager, Raport>() {
        @Override
         public Raport doTransaction(EntityManager entityManager) {
            Query q = entityManager.createNamedQuery("getStanMagazynu");
            q.setParameter("data", dzien.getData());
            List<StanMagazynuEntryBean> results = new ArrayList<StanMagazynuEntryBean>( q.getResultList());
            List<StanMagazynuEntryBean> toRemove = new ArrayList<StanMagazynuEntryBean>();
            for(StanMagazynuEntryBean result : results){
               if(Math.abs(result.getIloscJednostek().doubleValue()) < 0.01){
                  toRemove.add(result);
               }
            }
            results.removeAll(toRemove);
            Map<StanMagazynuEntryBean, BigDecimal> hashSet = new TreeMap<StanMagazynuEntryBean, BigDecimal>(new Comparator<StanMagazynuEntryBean>() {
               final Collator collator;
               {
                        collator = Collator.getInstance(new Locale("pl"));
                        collator.setStrength(Collator.PRIMARY);
               }
               @Override
               public int compare(StanMagazynuEntryBean o1, StanMagazynuEntryBean o2) {
                  return collator.compare(o1.getProdukt().getNazwa(), o2.getProdukt().getNazwa());
               }
            });
            for(StanMagazynuEntryBean bean : results){
               if(hashSet.containsKey(bean)){
                  hashSet.put(bean, hashSet.get(bean).add(bean.getIloscJednostek(), MathContext.DECIMAL32));
               }else{
                  hashSet.put(bean, bean.getIloscJednostek());
               }
            }
            results.clear();
            for(Map.Entry<StanMagazynuEntryBean, BigDecimal> entry : hashSet.entrySet()){
               entry.getKey().setIloscJednostek(entry.getValue());
               results.add(entry.getKey());
            }
            StanMagazynuRaportBean bean = new StanMagazynuRaportBean(dzien, results);
            StanMagazynuRaport rap = new StanMagazynuRaport();
            rap.setData(bean);
            return rap;
         }
      });
   }

//   private void sort(List<StanMagazynuEntryBean> smeb){
//      final Collator collator = Collator.getInstance(new Locale("pl"));
//      collator.setStrength(Collator.PRIMARY);
//      Collections.sort(smeb, new Comparator<StanMagazynuEntryBean>() {
//         @Override
//         public int compare(StanMagazynuEntryBean o1, StanMagazynuEntryBean o2) {
//            return collator.compare(o1.getProdukt().getNazwa(), o2.getProdukt().getNazwa());
//         }
//      });
//   }

   public void printStanMag(Dzien d) throws ReportException {
      Raport rap = createRaport(d);
      rap.print();
   }

   private File createDefaultFile(Dzien d){
      return new File(Preferences.getStanMagazynuFolder(), "Stan" + simpleDateFormat.format(d.getData()) + ".pdf");
   }

   public void cleanStanMagazynuFolder() throws ReportExceptionForUser {
      for(String filename : Preferences.getStanMagazynuFolder().list()){
         if(!new File(Preferences.getStanMagazynuFolder(), filename).delete()){
            throw new ReportExceptionForUser("Nie udało się usunąć starego pliku ze stanami magazynów, " +
                    "możliwe że jest on otwarty w innym programie. Zamknij go i spróbuj ponownie.\nNazwa pliku to :'"
            +filename + "'");
         }
      }
   }

      public void saveStanMagazynu(@Nonnull Dzien d, @CheckForNull File f) throws ReportException {
     if(f==null){
        f = createDefaultFile(d);
     }
      Raport rap = createRaport(d);
      rap.save(f);
   }

}
