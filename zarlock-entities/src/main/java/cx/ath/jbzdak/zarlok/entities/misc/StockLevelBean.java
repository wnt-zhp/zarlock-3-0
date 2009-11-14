package cx.ath.jbzdak.zarlok.entities.misc;

import java.math.BigDecimal;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class StockLevelBean {

   String specifier;

   String unit;

   BigDecimal quantity;

   public StockLevelBean() {
   }

   public StockLevelBean(String specifier, String unit) {
      this.specifier = specifier;
      this.unit = unit;
   }

   public StockLevelBean(String specifier, String unit, BigDecimal quantity) {
      this.specifier = specifier;
      this.unit = unit;
      this.quantity = quantity;
   }

   public String getSpecifier() {
      return specifier;
   }

   public void setSpecifier(String specifier) {
      this.specifier = specifier;
   }

   public String getUnit() {
      return unit;
   }

   public void setUnit(String unit) {
      this.unit = unit;
   }

   public BigDecimal getQuantity() {
      return quantity;
   }

   public void setQuantity(BigDecimal quantity) {
      this.quantity = quantity;
   }
}
