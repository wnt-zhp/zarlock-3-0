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

public class PartieUtils {

   private static boolean isWazne(Partia p){
      return p.getDataWaznosci()==null || (p.getDataWaznosci().getTime() - System.currentTimeMillis()) < 0;
   }

   private static String formatDataWaznosci(Partia p, DateFormat dateFormat){
      return p.getDataWaznosci()==null?"nieistotna":dateFormat.format(p.getDataWaznosci());
   }

   //TODO przerobić kiedyś na cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter.
   public static String getHtmlFormattedPartiaDesc(Partia p){
      DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
      StringBuilder builder = new StringBuilder();
      builder.append("<html>");
      builder.append("<large><strong>");
      builder.append(p.getFullName());
      builder.append("</large></strong>");
      builder.append("<br/><br/>");
      builder.append("<strong>cena</strong>: ");
      builder.append(MathUtils.round(p.getCena().doubleValue(), 2));
      builder.append(" zł.");
      builder.append("<br/>");
      builder.append("<strong>ilość</strong>: ");
      builder.append(p.getIloscTeraz());
      builder.append("<br/>");
      builder.append("<strong>data ważności</strong>: ");
      if(!isWazne(p)){
         builder.append("<font color=\"RED\">");
      }
      builder.append(formatDataWaznosci(p, format));
      if(!isWazne(p)){
         builder.append("</font>");
      }
      builder.append("<br/>");
      builder.append("<strong>data księgowania:</strong>: ");
      builder.append(format.format(p.getDataKsiegowania()));
      builder.append("<br/>");
      builder.append("<strong>data wprowadzenia do programu:</strong>: ");
      builder.append(format.format(p.getDataWprowadzenia()));
      builder.append("<br/>");
      builder.append("<strong>faktura:</strong>: ");
      builder.append(p.getNumerFaktury());
      if(p.getNumerLinii() != null){
         builder.append("<strong>(numer linii</strong>: ");
         builder.append(p.getDataWprowadzenia());
         builder.append(")");
      }
      builder.append("<br/>");
      if(p.getOpis()!=null){
         builder.append("<strong>opis:</strong>: ");
         builder.append(StringUtils.defaultString(p.getOpis()));
         builder.append("<br/>");
      }
      builder.append("</html>");
      return builder.toString();
   }

   public static List<Wyprowadzenie> wydajPoKolei(List<Partia> partie, BigDecimal ileWydać){
      BigDecimal currentQuantity = ileWydać;
      List<Wyprowadzenie> result = new ArrayList();
      for (int ii = 0; ii < partie.size() && currentQuantity.compareTo(BigDecimal.ZERO)>0; ii++) {
         Partia partia = partie.get(ii);
         Wyprowadzenie w = new Wyprowadzenie(partia);
         BigDecimal iloscJednostek = NumberUtils.min(partia.getIloscTeraz(), currentQuantity);
         w.setIloscJednostek(iloscJednostek);
         currentQuantity = Utils.round(currentQuantity.subtract(iloscJednostek),2);
         result.add(w);
         if(currentQuantity.compareTo(BigDecimal.ZERO) <=0){
            break;
         }
      }
      return result;
   }

   public static BigDecimal getIloscTeraz(Partia p){
      BigDecimal iloscTeraz = p.getIloscPocz();
      for(Wyprowadzenie w : p.getWyprowadzenia()){
         if(w.getIloscJednostek()!=null){
            iloscTeraz = iloscTeraz.subtract(w.getIloscJednostek());
         }
      }
      return iloscTeraz;
   }

   public static BigDecimal getIloscForDay(Partia p, Date dzien){
      BigDecimal iloscTeraz = p.getIloscPocz();
      for(Wyprowadzenie w : p.getWyprowadzenia()){
         if(w.getIloscJednostek()!=null && w.getDataWyprowadzenia().before(dzien)){
            iloscTeraz = iloscTeraz.subtract(w.getIloscJednostek());
         }
      }
      return iloscTeraz;
   }


}
