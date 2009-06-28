package cx.ath.jbzdak.zarlok.ui.formatted.formatters;

import cx.ath.jbzdak.jpaGui.ui.form.DAOForm;
import cx.ath.jbzdak.jpaGui.ui.formatted.ParsingException;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.BigDecimalFormatter;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.Wyprowadzenie;

import java.math.BigDecimal;
import java.math.MathContext;


public class IloscPoczFormatter extends BigDecimalFormatter {

	private final CenaFormatter cenaFormatter;

   private boolean watchIloscTeraz = false;

   private DAOForm partiaForm;

   public IloscPoczFormatter(CenaFormatter cenaFormatter) {
		super();
		this.cenaFormatter = cenaFormatter;
	}

	@Override
	public BigDecimal parseValue(String text) throws Exception {
		BigDecimal value =  super.parseValue(text);
		cenaFormatter.setIlośćPoczatkowa(value);
      Partia partia = (Partia) partiaForm.getEntity();
      if(watchIloscTeraz){
         BigDecimal iloscTeraz = value;
         for(Wyprowadzenie w : partia.getWyprowadzenia()){
            iloscTeraz = iloscTeraz.subtract(w.getIloscJednostek(), MathContext.DECIMAL32);
         }
         if(iloscTeraz.compareTo(BigDecimal.ZERO)<0){
            throw new ParsingException("Zmiana ilości początkowej na " + value + "sppowodowałaby że ilość bierząca byłaby ujemna");
         }
      }
		return value;
	}

   public void setWatchIloscTeraz(boolean watchIloscTeraz) {
      this.watchIloscTeraz = watchIloscTeraz;
   }

   public void setPartia(DAOForm partiaForm) {
      this.partiaForm = partiaForm;
   }
}
