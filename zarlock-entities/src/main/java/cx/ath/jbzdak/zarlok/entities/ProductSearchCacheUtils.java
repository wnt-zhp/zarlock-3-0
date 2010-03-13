package cx.ath.jbzdak.zarlok.entities;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cx.ath.jbzdak.jpaGui.ParsingException;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-04-20
 */
public class ProductSearchCacheUtils {

   @SuppressWarnings({"WeakerAccess"})
   public static final Pattern PRODUCT_SEARCH_CACHE_PATTERN = Pattern.compile("([\\p{L}\\s]+)(?:-([\\p{L}\\s]+)?)?(?:\\[([\\p{L}\\s]+)\\]?)?");

   public static String format(IProductSearchCache bean){
      StringBuilder sbr = new StringBuilder();
      sbr.append(bean.getProductName());
      if(!StringUtils.isEmpty(bean.getSpecifier())){
         sbr.append(" - ");
         sbr.append(bean.getSpecifier());
      }
      if(!StringUtils.isEmpty(bean.getUnit())){
         sbr.append("[");
         sbr.append(bean.getUnit());
         sbr.append("]");
      }
     return sbr.toString();
   }

   public static ProductSearchCache parse(String text) throws ParsingException {
      if(StringUtils.isEmpty(text)){
			return new ProductSearchCache("","","",null);
		}
		Matcher matcher = PRODUCT_SEARCH_CACHE_PATTERN.matcher(text);
		if(!matcher.matches()){
			throw new ParsingException("Błędny format produktu");
		}
		ProductSearchCache cache = new ProductSearchCache();
		cache.setProductName(matcher.group(1));
		cache.setSpecifier(matcher.group(2));
		cache.setUnit(matcher.group(3));
		return cache;
   }
}
