package cx.ath.jbzdak.zarlok.ui.formatted.formatters;

import cx.ath.jbzdak.jpaGui.ui.formatted.FormattingException;
import cx.ath.jbzdak.jpaGui.ui.formatted.MyFormatter;
import cx.ath.jbzdak.jpaGui.ui.formatted.ParsingException;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.DoubleFormatter;
import static org.apache.commons.lang.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CenaFormatter implements MyFormatter{

	private static final Pattern cenaPattern = Pattern.compile("\\s*(\\d++)\\s*(?:(\\+)?\\s*(\\d+)?([%p])?)\\s*");

	private boolean cenaJednostkowa = true;

	private DoubleFormatter formatter = new DoubleFormatter();

	private List<Integer> stawkiVat = Arrays.asList(0,3,7,22);

	private Number ilośćPoczatkowa;

	public CenaFormatter() {
		super();
	}

	@Override
	public String formatValue(Object value) throws FormattingException {
		return formatter.formatValue(value);
	}

	@Override
	public Object parseValue(String text) throws Exception {
		if(cenaJednostkowa){
			return parsePrice(text);
		}else{
			Double price = parsePrice(text).doubleValue();
			price/=ilośćPoczatkowa.doubleValue();
			return new BigDecimal(price);
		}
	}

	private BigDecimal parsePrice(String text) throws ParsingException{
		Matcher m = cenaPattern.matcher(text);
		if(!m.matches()){
			throw new ParsingException("Niepoprawny format pola '" + text + "'");
		}
		String nettoPart = m.group(1);
		String plusSign = m.group(2);
		String tax = m.group(3);
		String percent = m.group(3);
		if(!isEmpty(tax) && isEmpty(plusSign) && isEmpty(percent)){
			throw new ParsingException("Niepoprawny format pola");
		}
		Double value;
		try {
			value = formatter.parseValue(nettoPart);
		} catch (Exception e) {
			throw new ParsingException("Błąd podczas interpretowania części netto ceny", e);
		}
		if(!isEmpty(tax)){
			Double taxD;
			try {
				taxD = formatter.parseValue(tax);
			} catch (Exception e) {
				throw new ParsingException("Błąd podczas interpretowania części ceny zawierającej podatek",e);
			}
			if(!stawkiVat.contains(taxD.intValue())){
				throw new ParsingException("Nieznana stawka VAT " + taxD.intValue());
			}
			value+=value*taxD/100;
		}
		return new BigDecimal(value);
	}

	public boolean isCenaJednostkowa() {
		return cenaJednostkowa;
	}

	public DoubleFormatter getFormatter() {
		return formatter;
	}

	public void setCenaJednostkowa(boolean cenaJednostkowa) {
		this.cenaJednostkowa = cenaJednostkowa;
	}

	public void setFormatter(DoubleFormatter formatter) {
		this.formatter = formatter;
	}

	public void setIlośćPoczatkowa(Number ilośćPoczatkowa) {
		this.ilośćPoczatkowa = ilośćPoczatkowa;
	}

}
