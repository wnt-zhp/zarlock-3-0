package cx.ath.jbzdak.zarlok.ui.formatted.formatters;

import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.BigDecimalFormatter;

import java.math.BigDecimal;


public class IloscPoczFormatter extends BigDecimalFormatter {

	private final CenaFormatter cenaFormatter;


	public IloscPoczFormatter(CenaFormatter cenaFormatter) {
		super();
		this.cenaFormatter = cenaFormatter;
	}

	@Override
	public BigDecimal parseValue(String text) throws Exception {
		BigDecimal value =  super.parseValue(text);
		cenaFormatter.setIlośćPoczatkowa(value);
		return value;
	}

}
