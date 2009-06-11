package cx.ath.jbzdak.zarlok.entities;


public class TakieSamePartie implements ProductSeachCacheSearchable{

	private Produkt produkt;

	private String specyfikator;

	private String jednostka;

	private Number cena;

	private Number iloscTeraz;

	public TakieSamePartie(Produkt produkt, String specyfikator,
			String jednostka, Number cena, Number iloscTeraz) {
		super();
		this.produkt = produkt;
		this.specyfikator = specyfikator;
		this.jednostka = jednostka;
		this.cena = cena;
		this.iloscTeraz = iloscTeraz;
	}

	@SuppressWarnings({"WeakerAccess"})
   public Produkt getProdukt() {
		return produkt;
	}

	public String getSpecyfikator() {
		return specyfikator;
	}

	public String getJednostka() {
		return jednostka;
	}

	public Number getCena() {
		return cena;
	}

	public void setProdukt(Produkt produkt) {
		this.produkt = produkt;
	}

	public void setSpecyfikator(String specyfikator) {
		this.specyfikator = specyfikator;
	}

	public void setJednostka(String jednostka) {
		this.jednostka = jednostka;
	}

	public void setCena(Number cena) {
		this.cena = cena;
	}

	public Number getIloscTeraz() {
		return iloscTeraz;
	}

	public void setIloscTeraz(Number iloscTeraz) {
		this.iloscTeraz = iloscTeraz;
	}

   @Override
   public String getNazwaProduktu() {
      return getProdukt().getNazwa();
   }
}
