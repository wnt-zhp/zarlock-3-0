package cx.ath.jbzdak.zarlok.ui.formatted.formatters;

import cx.ath.jbzdak.jpaGui.ui.formatted.FormattingException;
import cx.ath.jbzdak.jpaGui.ui.formatted.MyFormatter;
import cx.ath.jbzdak.jpaGui.ui.formatted.ParsingException;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.DateFormatter;
import cx.ath.jbzdak.zarlok.entities.Partia;

import java.util.Date;

public class DataWyprowadzeniaFormatter implements MyFormatter {

	DateFormatter dateFormatter = new DateFormatter();

	Partia p;

	@Override
	public String formatValue(Object value) throws FormattingException {
		return dateFormatter.formatValue(value);
	}

	@Override
	public Object parseValue(String text) throws Exception {
		Date result = dateFormatter.parseValue(text);
		if(p.getDataKsiegowania().after(result)){
			throw new ParsingException("Nie można wydać produktu w dniu: '"+dateFormatter.formatDate(result) + "', jest to data przed datą księgowania (" + dateFormatter.formatDate(p.getDataKsiegowania()) + ")");
		}
		return result;
	}

	public Partia getP() {
		return p;
	}

	public void setP(Partia p) {
		this.p = p;
	}

}
