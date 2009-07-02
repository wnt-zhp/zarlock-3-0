package cx.ath.jbzdak.zarlok.ui.formatted.formatters;

import cx.ath.jbzdak.jpaGui.ui.formatted.FormattingException;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.DateFormatter;

import java.util.Date;
import java.util.regex.Pattern;

public class DataWaznosciFormatter extends DateFormatter{

	public static final Pattern nieistotnaPat
		= Pattern.compile("\\s*(?:nieist(?:otna)?)|(?:olej)|(?:n\\.?\\s?dot\\.?)\\s*");

	@Override
	public Date parseValue(String text) throws Exception {
		if(nieistotnaPat.matcher(text).matches()){
			return null;
		}
		return super.parseValue(text);
	}

	@Override
	public String formatValue(Object value) throws FormattingException {
		if(value==null){
			return "nieistotna";
		}
		return super.formatValue(value);
	}




}
