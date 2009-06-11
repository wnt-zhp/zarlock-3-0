package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.jpaGui.ui.formatted.ParsingException;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-04-20
 */
public class ProductSearchCacheUtils {

   @SuppressWarnings({"WeakerAccess"})
   public static final Pattern PROUCT_SEARCH_CACHE_PATTERN = Pattern.compile("([\\p{L}\\s]+)(?:-([\\p{L}\\s]+)?)?(?:\\[([\\p{L}\\s]+)\\]?)?");

   public static final String format(ProductSeachCacheSearchable bean){
      StringBuilder sbr = new StringBuilder();
      sbr.append(bean.getNazwaProduktu());
      if(!StringUtils.isEmpty(bean.getSpecyfikator())){
         sbr.append(" - ");
         sbr.append(bean.getSpecyfikator());
      }
      if(!StringUtils.isEmpty(bean.getJednostka())){
         sbr.append("[");
         sbr.append(bean.getJednostka());
         sbr.append("]");
      }
     return sbr.toString();
   }

   public static final ProductSearchCache parse(String text) throws ParsingException {
      if(StringUtils.isEmpty(text)){
			return new ProductSearchCache("","","",null);
		}
		Matcher matcher = PROUCT_SEARCH_CACHE_PATTERN.matcher(text);
		if(!matcher.matches()){
			throw new ParsingException("Błędny format produktu");
		}
		ProductSearchCache cache = new ProductSearchCache();
		cache.setNazwaProduktu(matcher.group(1));
		cache.setSpecyfikator(matcher.group(2));
		cache.setJednostka(matcher.group(3));
		return cache;
   }
}
