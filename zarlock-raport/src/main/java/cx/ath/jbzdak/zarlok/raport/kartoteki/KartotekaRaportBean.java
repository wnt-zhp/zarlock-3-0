package cx.ath.jbzdak.zarlok.raport.kartoteki;

import cx.ath.jbzdak.zarlok.entities.Batch;
import cx.ath.jbzdak.zarlok.entities.Expenditure;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: May 6, 2009
 */
public class KartotekaRaportBean {

   final String nazwa;

   final String specyfikator;

   final BigDecimal cena;

   List<KartotekaEntryBean> zawartoscKartoteki;

   public KartotekaRaportBean(String nazwa, String specyfikator,BigDecimal cena) {
      this.nazwa =  nazwa;
      this.specyfikator = specyfikator;
      this.cena = cena;
   }

   public String getNazwa() {return nazwa;}

   public String getSpecyfikator() {
      return specyfikator;
   }

   public BigDecimal getCena() {
      return cena;
   }

   public void setPartie(List<Batch> partie) {
      zawartoscKartoteki = new ArrayList<KartotekaEntryBean>();
      for(Batch p : partie){
         zawartoscKartoteki.add(KartotekaEntryBean.createPrzychodBean(p));
         for(Expenditure w : p.getWyprowadzenia()){
            zawartoscKartoteki.add(KartotekaEntryBean.createRozchodBean(w));
         }
      }
      Collections.sort(zawartoscKartoteki);
      BigDecimal ilosc=BigDecimal.ZERO;
      for(KartotekaEntryBean kartotekaEntryBean : zawartoscKartoteki){
         if(kartotekaEntryBean.getPrzychodIlosc()!=null){
            ilosc = ilosc.add(kartotekaEntryBean.getPrzychodIlosc());
         }else{
            ilosc = ilosc.subtract(kartotekaEntryBean.getRozchodIlosc());
         }
         kartotekaEntryBean.setStanIlosc(ilosc);
         kartotekaEntryBean.setStanWartosc(cena.multiply(ilosc));
      }
   }

   public List<KartotekaEntryBean> getZawartoscKartoteki() {
      return zawartoscKartoteki;
   }
}
