package cx.ath.jbzdak.zarlok.raport.stany;

import cx.ath.jbzdak.zarlok.entities.Dzien;

import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-05-04
 */
@SuppressWarnings({"WeakerAccess"})
public class StanMagazynuRaportBean {

   @SuppressWarnings({"WeakerAccess"})
   private final Dzien dzien;

   @SuppressWarnings({"WeakerAccess"})
   private final List<StanMagazynuEntryBean> partie;

   public StanMagazynuRaportBean(Dzien dzien, List<StanMagazynuEntryBean> partie) {
      this.dzien = dzien;
      this.partie = partie;
   }

   public Dzien getDzien() {
      return dzien;
   }

   public List<StanMagazynuEntryBean> getPartie() {
      return partie;
   }
}
