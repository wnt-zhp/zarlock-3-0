package cx.ath.jbzdak.zarlock.ui.product;

import cx.ath.jbzdak.zarlok.entities.Expenditure;
import cx.ath.jbzdak.zarlok.entities.Batch;
import cx.ath.jbzdak.zarlok.entities.PlannedExpenditure;
import static cx.ath.jbzdak.zarlok.ZarlockBoundle.*;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class KartotekaBean implements Comparable<KartotekaBean>{

   final BigDecimal addedToStock;

   final BigDecimal price;

   final BigDecimal value;

   BigDecimal stockInWarehouse;

   BigDecimal valueInWarehouse;

   @Nonnull final Date date;

   final String tytulem;

   final KartotekaType kartotekaType;

   public KartotekaBean(Expenditure expenditure) {
      addedToStock = expenditure.getQuantity().negate();
      price = expenditure.getBatch().getPrice();
      tytulem = expenditure.getTytulem();
      value = addedToStock.multiply(price).setScale(2, BigDecimal.ROUND_HALF_EVEN);
      date = expenditure.getExpenditureDate();
      kartotekaType = KartotekaType.EXPENDITURE;
   }

   public KartotekaBean(Batch batch) {
      addedToStock = batch.getStartQty();
      price = batch.getPrice();
      tytulem = getString("kartoteka.batch.tytulem", batch.getFakturaNo());
      value = addedToStock.multiply(price).setScale(2, BigDecimal.ROUND_HALF_EVEN);
      date = batch.getBookingDate();
      kartotekaType = KartotekaType.BATCH;
   }

   public KartotekaBean(PlannedExpenditure expenditure) {
      if(expenditure.isSpent()){
         throw new IllegalArgumentException();
      }
      addedToStock = expenditure.getQuantity().negate();
      price = null;
      value = null;
      tytulem = getString("kartoteka.plannedExpenditure.tytulem",expenditure.getCourse().getMeal().getDay().getDate(),
              expenditure.getCourse().getMeal().getName());
      date = expenditure.getCourse().getMeal().getDay().getDate();
      kartotekaType = KartotekaType.PLANNED;
   }

   public BigDecimal getAddedToStock() {
      return addedToStock;
   }

   public BigDecimal getPrice() {
      return price;
   }

   public BigDecimal getValue() {
      return value;
   }

   public BigDecimal getStockInWarehouse() {
      return stockInWarehouse;
   }

   public BigDecimal getValueInWarehouse() {
      return valueInWarehouse;
   }

   public String getTytulem() {
      return tytulem;
   }

   public KartotekaType getKartotekaType() {
      return kartotekaType;
   }

   @Override
   public int compareTo(KartotekaBean o) {
      return date.compareTo(o.date);
   }
}
