package cx.ath.jbzdak.zarlock.ui.product;

import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-12
 */
public class KartotekaEntry {

   final String specifier, unit;

   final List<KartotekaBean> kartotekaEntryList;

   public KartotekaEntry(String specifier, String unit, List<KartotekaBean> kartotekaEntryList) {
      this.specifier = specifier;
      this.unit = unit;
      this.kartotekaEntryList = kartotekaEntryList;
   }

   public String getSpecifier() {
      return specifier;
   }

   public String getUnit() {
      return unit;
   }

   public List<KartotekaBean> getKartotekaEntryList() {
      return kartotekaEntryList;
   }
}
