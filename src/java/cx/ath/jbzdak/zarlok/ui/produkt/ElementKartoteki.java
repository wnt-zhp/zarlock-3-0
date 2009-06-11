package cx.ath.jbzdak.zarlok.ui.produkt;

import static cx.ath.jbzdak.jpaGui.Utils.round;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.Wyprowadzenie;
import org.apache.commons.math.util.MathUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author jb
 *
 */
public class ElementKartoteki {

	private static final BigDecimal MINUS_ONE = new BigDecimal(-1);

	Object sourceObject;

	public ElementKartoteki(Partia p) {
		nazwa = p.getFullName();
		cena = round(p.getCena(),2);
		ilosc = p.getIloscPocz();
		jednostka = p.getJednostka();
		tytulem = "Faktura nr. " + p.getNumerFaktury();
		date = p.getDataKsiegowania();
		wartosc = new BigDecimal(MathUtils.round(cena.multiply(ilosc).doubleValue(),2));
		this.sourceObject = p;
	}

	public ElementKartoteki(Wyprowadzenie w) {
		nazwa = w.getPartia().getFullName();
		cena = round(w.getPartia().getCena(),2);
		ilosc = w.getIloscJednostek();
		jednostka = w.getPartia().getJednostka();
		tytulem = w.getTytulem();
		date = w.getDataWyprowadzenia();
		wartosc = new BigDecimal(MathUtils.round(cena.multiply(ilosc).doubleValue(),2));
		this.sourceObject = w;
	}

	String nazwa;

	BigDecimal cena;

	BigDecimal ilosc;

	BigDecimal wartosc;

	String jednostka;

	String tytulem;

	Date date;

	public String getNazwa() {
		return nazwa;
	}

	public BigDecimal getCena() {
		return cena;
	}

	public BigDecimal getIlosc() {
		return ilosc;
	}

	public BigDecimal getWartosc() {
		return wartosc;
	}

	public String getTytulem() {
		return tytulem;
	}

	public String getJednostka() {
		return jednostka;
	}

	public void setWartosc(BigDecimal wartosc) {
		if(wartosc.compareTo(BigDecimal.ZERO)<0){
			this.wartosc = BigDecimal.ZERO;
		}else{
			this.wartosc = wartosc;
		}
	}

	public static BigDecimal getMINUS_ONE() {
		return MINUS_ONE;
	}

	public Object getSourceObject() {
		return sourceObject;
	}

	public Date getDate() {
		return date;
	}
}
