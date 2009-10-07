package cx.ath.jbzdak.zarlok.raport.kartoteki;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.Wyprowadzenie;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: May 6, 2009
 */
public class KartotekaEntryBean implements Comparable<KartotekaEntryBean> {

   private final Date data;

   private final String nrDowodu;

   private BigDecimal stanIlosc;

   private BigDecimal stanWartosc;

   private final BigDecimal przychodIlosc;

   private final BigDecimal przychodWartosc;

   private final BigDecimal rozchodIlosc;

   private final BigDecimal rozchodWartosc;

   public static KartotekaEntryBean createPrzychodBean(Partia partia){
      return new KartotekaEntryBean(partia.getDataKsiegowania(),
                                    partia.getNumerFaktury(),
                                    partia.getIloscPocz(),
                                    partia.getIloscPocz().multiply(partia.getCena(), MathContext.DECIMAL32),
                                    null, null);
   }

   public static  KartotekaEntryBean createRozchodBean(Wyprowadzenie wyprowadzenie){
      return new KartotekaEntryBean(wyprowadzenie.getDataWyprowadzenia(),
                                    wyprowadzenie.getTytulem(),
                                    null,
                                    null,
                                    wyprowadzenie.getIloscJednostek(), wyprowadzenie.getWartosc());
   }

   private KartotekaEntryBean(Date data, String nrDowodu, BigDecimal przychodIlosc, BigDecimal przychodWartosc, BigDecimal rozchodIlosc, BigDecimal rozchodWartosc) {
      this.data = data;
      this.nrDowodu = nrDowodu;
//      this.stanIlosc = stanIlosc;
//      this.stanWartosc = stanWartosc;
      this.przychodIlosc = przychodIlosc;
      this.przychodWartosc = przychodWartosc;
      this.rozchodIlosc = rozchodIlosc;
      this.rozchodWartosc = rozchodWartosc;
   }

   public Date getData() {
      return data;
   }

   public String getNrDowodu() {
      return nrDowodu;
   }

   public BigDecimal getStanIlosc() {
      return stanIlosc;
   }

   public BigDecimal getStanWartosc() {
      return stanWartosc;
   }

   public BigDecimal getPrzychodIlosc() {
      return przychodIlosc;
   }

   public BigDecimal getPrzychodWartosc() {
      return przychodWartosc;
   }

   public BigDecimal getRozchodIlosc() {
      return rozchodIlosc;
   }

   public BigDecimal getRozchodWartosc() {
      return rozchodWartosc;
   }

   public void setStanIlosc(BigDecimal stanIlosc) {
      this.stanIlosc = stanIlosc;
   }

   public void setStanWartosc(BigDecimal stanWartosc) {
      this.stanWartosc = stanWartosc;
   }

   @Override
   public int compareTo(KartotekaEntryBean o) {
      int result =data.compareTo(o.getData());
      if(result == 0 && !Utils.compareNullity(przychodIlosc, o.przychodIlosc)){
         if(this.przychodIlosc!=null){
            return -1;
         }
         return 1;
      }
      return result;
   }
}
