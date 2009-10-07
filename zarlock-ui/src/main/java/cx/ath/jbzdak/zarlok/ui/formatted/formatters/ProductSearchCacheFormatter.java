package cx.ath.jbzdak.zarlok.ui.formatted.formatters;

import cx.ath.jbzdak.jpaGui.ui.formatted.FormattingException;
import cx.ath.jbzdak.jpaGui.ui.formatted.MyFormatter;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCache;
import cx.ath.jbzdak.zarlok.entities.ProductSearchCacheUtils;

public class ProductSearchCacheFormatter implements MyFormatter {

	@Override
	public String formatValue(Object value) throws FormattingException {
		return ProductSearchCacheUtils.format((ProductSearchCache)value);
	}

	@Override
	public ProductSearchCache parseValue(String text) throws Exception {
		return ProductSearchCacheUtils.parse(text);
	}

}
