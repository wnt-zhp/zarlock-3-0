package cx.ath.jbzdak.zarlok.raport.stany;

import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.Produkt;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-05-04
 */
@SuppressWarnings({"WeakerAccess"})
public class StanMagazynuEntryBean {

   private final Produkt produkt;

   private final String  specyfikator;

   private final String jednostka;

   private  BigDecimal iloscJednostek;

   public StanMagazynuEntryBean(Partia p, BigDecimal iloscWyprowadzona) {
      this.produkt = p.getProdukt();
      this.specyfikator = p.getSpecyfikator();
      this.jednostka = p.getJednostka();
      iloscWyprowadzona=iloscWyprowadzona!=null?iloscWyprowadzona:BigDecimal.ZERO;
      this.iloscJednostek =  p.getIloscPocz().subtract(iloscWyprowadzona, MathContext.DECIMAL32);
   }

    public StanMagazynuEntryBean(Partia p) {
      this.produkt = p.getProdukt();
      this.specyfikator = p.getSpecyfikator();
      this.jednostka = p.getJednostka();
      this.iloscJednostek =  p.getIloscPocz();
   }

   public Produkt getProdukt() {
      return produkt;
   }

   public String getSpecyfikator() {
      return specyfikator;
   }

   public BigDecimal getIloscJednostek() {
      return iloscJednostek;
   }

   public String getJednostka() {
      return jednostka;
   }

   public void setIloscJednostek(BigDecimal iloscJednostek) {
      this.iloscJednostek = iloscJednostek;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof StanMagazynuEntryBean)) return false;

      StanMagazynuEntryBean that = (StanMagazynuEntryBean) o;

      if (!jednostka.equals(that.jednostka)) return false;
      if (!produkt.getNazwa().equals(that.produkt.getNazwa())) return false;
      return specyfikator.equals(that.specyfikator);

   }

   @Override
   public int hashCode() {
      int result = produkt.getNazwa().hashCode();
      result = 31 * result + specyfikator.hashCode();
      result = 31 * result + jednostka.hashCode();
      return result;
   }
}
