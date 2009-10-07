package cx.ath.jbzdak.zarlok.ui.formatted.formatters;

import cx.ath.jbzdak.jpaGui.ui.formatted.ParsingException;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.NumberFormatter;

public class IloscOsobFormatter extends NumberFormatter<Integer>{

	@Override
	protected Integer parseResult(String text) {
		return Integer.valueOf(text);
	}

	@Override
	public Integer parseValue(String text) throws Exception {
		Integer result = super.parseValue(text);
		if(result.intValue() <0){
			throw new ParsingException("Ilość osób nie może być ujemna ;)");
		}
		return result;
	}

}
