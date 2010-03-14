package cx.ath.jbzdak.zarlock.ui.batch;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cx.ath.jbzdak.jpaGui.*;
import cx.ath.jbzdak.jpaGui.ui.formatted.AbstractFormatter;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.DoubleFormatter;
import cx.ath.jbzdak.zarlok.entities.Batch;
import cx.ath.jbzdak.zarlok.ZarlockBoundle;

import static org.apache.commons.lang.StringUtils.isEmpty;
import org.apache.commons.math.util.MathUtils;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Mar 10, 2010
 */
public class PriceFormatter
        extends AbstractFormatter<BigDecimal,BigDecimal>  implements BeanHolderAware<Batch, BeanHolder<Batch>>{
   	private static final Pattern cenaPattern = Pattern.compile("\\s*(\\d++(?:[,.]\\d+)?)\\s*([nb])?\\s*(?:(\\+)?\\s*(\\d+)?([%p])?)\\s*");

	private DoubleFormatter formatter = new DoubleFormatter();

	private List<Integer> stawkiVat = Arrays.asList(0,3,7,22);

   BeanHolder<Batch> beanHolder;

   private Double startQty;

   public PriceFormatter() {
		super();
	}

	@Override
	public String formatValue(BigDecimal value) throws FormattingException {
		return formatter.formatValue(value);
	}

	public BigDecimal parseValue(String text) throws ParsingException {
		Matcher m = cenaPattern.matcher(text);
		if(!m.matches()){
			throw new ParsingException("Niepoprawny format pola '" + text + "'");
		}

		String nettoPart = m.group(1);
      String nettoBrutto = m.group(2);
		String plusSign = m.group(3);
		String tax = m.group(4);
		String percent = m.group(5);

      boolean netto = isNetto(nettoBrutto);

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
      if(!netto){
         if(startQty == null || MathUtils.equals(0, startQty)){
            throw new ParsingException(ZarlockBoundle.getString("batch.exception.bruttoPriceAndNoStartingQty"));
         }
         value/= startQty;
      }
		return new BigDecimal(value);
	}

   private boolean isNetto(String nettoBrutto) throws ParsingException {
      if(nettoBrutto == null){
         return true;
      }
      nettoBrutto = nettoBrutto.trim();
      if("n".equals(nettoBrutto)){
         return true;
      } else if("b".equals(nettoBrutto)){
         return false;
      } else if(nettoBrutto.length()!=0){
         throw new ParsingException("Niepoprwany format pola");
      } else {
         return true;
      }
   }

   public Double getStartQty() {
      if(startQty==null){
         return  beanHolder.getBean().getStartQty().doubleValue();
      }
      return startQty;
   }

   public void setStartQty(Double iloscPoczatkowa) {
      this.startQty = iloscPoczatkowa;
   }

   public void setBeanHolder(BeanHolder<Batch> beanHolder) {
      this.beanHolder = beanHolder;
   }
}
