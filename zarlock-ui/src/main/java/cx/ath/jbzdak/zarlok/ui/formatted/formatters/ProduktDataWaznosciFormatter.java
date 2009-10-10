package cx.ath.jbzdak.zarlok.ui.formatted.formatters;

import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.RelativeDateParser;
import cx.ath.jbzdak.jpaGui.MyFormatter;
import cx.ath.jbzdak.jpaGui.FormattingException;
import cx.ath.jbzdak.jpaGui.ParsingException;
import org.apache.commons.lang.StringUtils;

public class ProduktDataWaznosciFormatter implements MyFormatter {


	private final RelativeDateParser parser = new RelativeDateParser();

	@Override
	public String formatValue(Object value) throws FormattingException {
		if(value == null){
			return "";
		}
		Number n = (Number) value;
		if(n.intValue()==-1){
			return "nieistotna";
		}
		return (n.intValue()>0?"+":"")+value.toString();
	}

	@Override
	public Object parseValue(String text) throws Exception {
		if(StringUtils.isEmpty(text)){
			throw new ParsingException("Pole nie może pozostać puste");
		}
		if(DataWaznosciFormatter.nieistotnaPat.matcher(text).matches()){
			return -1;
		}
		int result = parser.parseDate(text);
		if(result <= 0){
			throw new ParsingException("Musisz podać wartość większą od 0 (np. +5) albo wpisać 'nieistotna'");
		}
		return result;
	}

}
