package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.jpaGui.NumberUtils;
import cx.ath.jbzdak.jpaGui.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math.util.MathUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BatchUtils {

   private static boolean hasNotExpired(Batch p){
      return p.getExpiryDate()==null || (p.getExpiryDate().getTime() - System.currentTimeMillis()) < 0;
   }

   private static String formatDataWaznosci(Batch p, DateFormat dateFormat){
      return p.getExpiryDate()==null?"nieistotna":dateFormat.format(p.getExpiryDate());
   }

   //TODO przerobić kiedyś na cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter.
   public static String getHtmlFormattedBatchDescription(Batch p){
      DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
      StringBuilder builder = new StringBuilder();
      builder.append("<html>");
      builder.append("<large><strong>");
      builder.append(p.getFullName());
      builder.append("</large></strong>");
      builder.append("<br/><br/>");
      builder.append("<strong>cena</strong>: ");
      builder.append(MathUtils.round(p.getPrice().doubleValue(), 2));
      builder.append(" zł.");
      builder.append("<br/>");
      builder.append("<strong>ilość</strong>: ");
      builder.append(p.getCurrentQty());
      builder.append("<br/>");
      builder.append("<strong>data ważności</strong>: ");
      if(!hasNotExpired(p)){
         builder.append("<font color=\"RED\">");
      }
      builder.append(formatDataWaznosci(p, format));
      if(!hasNotExpired(p)){
         builder.append("</font>");
      }
      builder.append("<br/>");
      builder.append("<strong>data księgowania:</strong>: ");
      builder.append(format.format(p.getBookingDate()));
      builder.append("<br/>");
      builder.append("<strong>data wprowadzenia do programu:</strong>: ");
      builder.append(format.format(p.getCreateDate()));
      builder.append("<br/>");
      builder.append("<strong>faktura:</strong>: ");
      builder.append(p.getFakturaNo());
      if(p.getLineNo() != null){
         builder.append("<strong>(numer linii</strong>: ");
         builder.append(p.getCreateDate());
         builder.append(")");
      }
      builder.append("<br/>");
      if(p.getDescription()!=null){
         builder.append("<strong>opis:</strong>: ");
         builder.append(StringUtils.defaultString(p.getDescription()));
         builder.append("<br/>");
      }
      builder.append("</html>");
      return builder.toString();
   }

   public static List<Expenditure> wydajPoKolei(List<Batch> partie, BigDecimal ileWydać){
      BigDecimal currentQuantity = ileWydać;
      List<Expenditure> result = new ArrayList();
      for (int ii = 0; ii < partie.size() && currentQuantity.compareTo(BigDecimal.ZERO)>0; ii++) {
         Batch batch = partie.get(ii);
         Expenditure w = new Expenditure(batch);
         BigDecimal iloscJednostek = NumberUtils.min(batch.getCurrentQty(), currentQuantity);
         w.setQuantity(iloscJednostek);
         currentQuantity = Utils.round(currentQuantity.subtract(iloscJednostek),2);
         result.add(w);
         if(currentQuantity.compareTo(BigDecimal.ZERO) <=0){
            break;
         }
      }
      return result;
   }

   public static BigDecimal getIloscTeraz(Batch p){
      BigDecimal iloscTeraz = p.getStartQty();
      for(Expenditure w : p.getExpenditures()){
         if(w.getQuantity()!=null){
            iloscTeraz = iloscTeraz.subtract(w.getQuantity());
         }
      }
      iloscTeraz = Utils.round(iloscTeraz, 2);
      return iloscTeraz;
   }

   public static BigDecimal getIloscForDay(Batch p, Date dzien){
      BigDecimal iloscTeraz = p.getStartQty();
      for(Expenditure w : p.getExpenditures()){
         if(w.getQuantity()!=null && w.getExpenditureDate().before(dzien)){
            iloscTeraz = iloscTeraz.subtract(w.getQuantity());
         }
      }
      return iloscTeraz;
   }


}
