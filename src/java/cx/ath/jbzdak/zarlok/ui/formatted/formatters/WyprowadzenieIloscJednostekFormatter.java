package cx.ath.jbzdak.zarlok.ui.formatted.formatters;

import cx.ath.jbzdak.jpaGui.ui.formatted.FormattingException;
import cx.ath.jbzdak.jpaGui.ui.formatted.MyFormatter;
import cx.ath.jbzdak.jpaGui.ui.formatted.ParsingException;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.BigDecimalFormatter;
import cx.ath.jbzdak.zarlok.entities.Partia;

import java.math.BigDecimal;

public class WyprowadzenieIloscJednostekFormatter implements MyFormatter{

	private final BigDecimalFormatter bigDecimalFormatter = new BigDecimalFormatter();

	private Partia partia;

	@Override
	public String formatValue(Object value) throws FormattingException {
		return bigDecimalFormatter.formatValue(value);
	}
	@Override
	public Object parseValue(String text) throws Exception {
		BigDecimal result = bigDecimalFormatter.parseValue(text);
		if(partia.getIloscTeraz().compareTo(result) < 0){
			throw new ParsingException("Maksymalna możliwa do wydania ilość to: "
					+ partia.getIloscTeraz() + " próbowano wydać " + result);
		}
		return result;
	}

	public Partia getPartia() {
		return partia;
	}

	public void setPartia(Partia partia) {
		this.partia = partia;
	}

}
