package cx.ath.jbzdak.zarlock.ui.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cx.ath.jbzdak.jpaGui.Formatter;
import cx.ath.jbzdak.jpaGui.FormattingException;
import cx.ath.jbzdak.jpaGui.ui.formatted.AbstractFormatter;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCache;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCacheUtils;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 13, 2010
 */
public class ProducSearchCacheFormatter extends AbstractFormatter<ProductSearchCache, ProductSearchCache> {

   private static final Logger LOGGER = LoggerFactory.getLogger(ProducSearchCacheFormatter.class);

   @Override
   public String formatValue(ProductSearchCache value) throws FormattingException {
      LOGGER.debug("XXXXXXX {}", value);
      return ProductSearchCacheUtils.format(value);
   }

   @Override
   public ProductSearchCache parseValue(String text) throws Exception {
      return ProductSearchCacheUtils.parse(text);
   }
}
