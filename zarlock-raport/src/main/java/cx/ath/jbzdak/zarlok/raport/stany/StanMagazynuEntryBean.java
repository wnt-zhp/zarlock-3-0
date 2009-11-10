package cx.ath.jbzdak.zarlok.raport.stany;

import cx.ath.jbzdak.zarlok.entities.Batch;
import cx.ath.jbzdak.zarlok.entities.Product;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-05-04
 */
@SuppressWarnings({"WeakerAccess"})
public class StanMagazynuEntryBean {

   private final Product product;

   private final String  specyfikator;

   private final String jednostka;

   private  BigDecimal iloscJednostek;

   public StanMagazynuEntryBean(Batch p, BigDecimal iloscWyprowadzona) {
      this.product = p.getProduct();
      this.specyfikator = p.getSpecifier();
      this.jednostka = p.getUnit();
      iloscWyprowadzona=iloscWyprowadzona!=null?iloscWyprowadzona:BigDecimal.ZERO;
      this.iloscJednostek =  p.getStartQty().subtract(iloscWyprowadzona, MathContext.DECIMAL32);
   }

    public StanMagazynuEntryBean(Batch p) {
      this.product = p.getProduct();
      this.specyfikator = p.getSpecifier();
      this.jednostka = p.getUnit();
      this.iloscJednostek =  p.getStartQty();
   }

   public Product getProdukt() {
      return product;
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
      if (!product.getName().equals(that.product.getName())) return false;
      return specyfikator.equals(that.specyfikator);

   }

   @Override
   public int hashCode() {
      int result = product.getName().hashCode();
      result = 31 * result + specyfikator.hashCode();
      result = 31 * result + jednostka.hashCode();
      return result;
   }
}
