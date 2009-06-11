package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.jpaGui.Utils;
import static cx.ath.jbzdak.jpaGui.Utils.getRelativeDate;
import cx.ath.jbzdak.zarlok.entities.listeners.PartiaSearchCacheUpdater;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.*;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Partia produktu.
 */
@Entity
@Table(name="PARTIA")
@NamedQueries({
	@NamedQuery(
			name="getPartiaJednostka",
			query = "SELECT DISTINCT p.jednostka FROM Partia p WHERE p.jednostka LIKE CONCAT(CONCAT('%', :jednostka), '%')"
	),
	@NamedQuery(
			name = "getPartiaSpecyfikator",
			query = "SELECT DISTINCT p.specyfikator FROM Partia p WHERE LOWER(p.specyfikator) LIKE LOWER('%' || :specyfikator || '%')"
	),
	@NamedQuery(
			name = "getPartie",
			query = "SELECT new cx.ath.jbzdak.zarlok.entities.TakieSamePartie(p.produkt, p.specyfikator, p.jednostka, AVG(p.cena*p.iloscTeraz)/SUM(p.iloscTeraz), SUM(p.iloscTeraz))  FROM Partia p GROUP BY p.produkt, p.specyfikator, p.jednostka"
	),
	@NamedQuery(
		name = "filterPartie",
		query = "SELECT p FROM Partia p"
	),
	@NamedQuery(
			name = "getPartieByProdukt",
			query = "SELECT new cx.ath.jbzdak.zarlok.entities.TakieSamePartie(p.produkt, p.specyfikator, p.jednostka, AVG(p.cena), SUM(p.iloscTeraz))" +
					" FROM Partia p WHERE p.produkt = :produkt GROUP BY p.produkt, p.specyfikator, p.jednostka"
	),
	@NamedQuery(
			name = "getParieForWydanie",
			query = "SELECT p FROM Partia p " +
					"WHERE ((:nazwa IS NULL) OR  LOWER(p.produkt.nazwa) LIKE LOWER('%' || :nazwa || '%')) AND" +
					"((:specyfikator IS NULL) OR LOWER(p.specyfikator) LIKE LOWER('%' || :specyfikator || '%')) AND " +
					"((:jednostka IS NULL) OR LOWER(p.jednostka) LIKE LOWER('%' || :jednostka || '%'))"
	),
   @NamedQuery(
           name = "getPartieDoWydaniaNaPosilek",
           query =   "SELECT p FROM Partia p " +
                   "WHERE " +
                   "(:nazwa IS NULL OR p.produkt.nazwa = :nazwa) AND " +
                   "(:specyfikator IS NULL OR p.specyfikator = :specyfikator ) AND " +
                   "(:jednostka IS NULL OR p.jednostka = :jednostka) AND " +
                   "(p.dataWaznosci IS NULL OR p.dataWaznosci > :dzien) AND " +
                   "p.dataKsiegowania <= :dzien AND " +
                   "p.iloscTeraz > 0 " +
                   "ORDER BY p.dataWaznosci ASC, " +
                   "p.dataKsiegowania DESC, " +
                   "p.iloscTeraz ASC"
   ),
   @NamedQuery(
           name = "getKartotekiContents",
           query = "SELECT new cx.ath.jbzdak.zarlok.raport.kartoteki.KartotekaRaportBean(" +
                   "p.produkt.nazwa, " +
                   "p.specyfikator," +
                   "p.cena) " +
                   "FROM Partia p " +
                   "GROUP BY p.produkt.nazwa, p.jednostka, p.specyfikator, p.cena "
   ),
   @NamedQuery(
           name = "getKartotekaContentsStage2",
                   query = "SELECT DISTINCT (p) " +
                   "FROM Partia p " +
                   "LEFT JOIN FETCH p.wyprowadzenia " +
                   "WHERE p.produkt.nazwa = :nazwa  AND p.specyfikator = :specyfikator AND p.cena = :cena"

   )
})

@EntityListeners({PartiaSearchCacheUpdater.class})
public class Partia implements ProductSeachCacheSearchable{

   @Id
   @GeneratedValue
   @Column(name = "ID")
   private Long id;

	@ManyToOne(fetch= FetchType.EAGER, optional=false)
	@JoinColumn(name = "PRODUKT_ID")
   private Produkt produkt;

	/**
	 * Coś co odróżnia partie produktu. Na przylkład w
	 * szynce lukusowej luksusowa to specyfikator
	 */
	@Nonnull
	@NotNull
	@Column(name="Specyfikator")
   private String specyfikator;

	@Column(name="CENA", precision=16, scale=6)
	@Nonnull
	@NotNull
   private BigDecimal cena;

	@Column(name="ILOSC_POCZ", precision=12, scale=2)
	@Nonnull
	@NotNull
   private BigDecimal iloscPocz;

	/**
	 * Jednostka produktu.
	 */
	@Column(name = "JEDNOSTKA")
	@Nonnull
	@NotNull
   private String jednostka;

	@Column(name = "ILOSC_TERAZ", precision=12, scale=2)
	@Nonnull
	@NotNull
   private BigDecimal iloscTeraz;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_KSIEGOWANIA")
	@Nonnull
	@NotNull
   private Date dataKsiegowania;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_WAZNOSCI")
	@Nullable
   private Date dataWaznosci;

	/**
	 * Moment zapisania encji w bazie danych.
	 */
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_WPROWADZENIA")
   private Date dataWprowadzenia;

	/**
	 * Losowy ciąg znaków wprowadzany, lub nie przez użyszkodnika
	 */
	@Length(min=0, max=1000)
	@Column(name="OPIS")
	@Nullable
   private String opis;

	@Length(min=1, max=100)
	@Column(name = "NUMER_FAKTURY")
	@Nonnull
	@NotNull
   private String numerFaktury;

	@OneToMany(mappedBy="partia")
   private List<Wyprowadzenie> wyprowadzenia = new ArrayList<Wyprowadzenie>();

	@Column(name = "NUMER_LINII")
	@Nullable private Integer numerLinii;

	public Partia() {
		super();
	}

	public Partia(Produkt produkt, ProductSearchCache searchCache) {
		super();
		this.produkt = produkt;
		setSpecyfikator(searchCache.specyfikator);
		setJednostka(searchCache.getJednostka());
		if(produkt.getDataWaznosci() == -1){
			setDataWaznosci(null);
		}else{
			setDataWaznosci(getRelativeDate(produkt.getDataWaznosci()));
		}
	}

	@PrePersist
	public void prePersist(){
		dataWprowadzenia = new Date();
		recalculateIloscTeraz();
      cena = Utils.round(cena, 2);
	}

	@PreUpdate
	public void preUpdate(){
		recalculateIloscTeraz();
      cena = Utils.round(cena, 2);
	}


	public void recalculateIloscTeraz(){
		BigDecimal iloscTeraz = iloscPocz;
		for(Wyprowadzenie w : wyprowadzenia){
			if(w.getIloscJednostek()!=null){
				iloscTeraz = iloscTeraz.subtract(w.getIloscJednostek());
			}
		}
		setIloscTeraz(iloscTeraz);
	}

	@Transient
	public String getFullName(){
		return produkt.getNazwa() + " " + getSpecyfikator();
	}

	@Transient
	public String getBasicData(){
		return produkt.getNazwa() + " " + getSpecyfikator() + " " + getIloscTeraz() + " " + getJednostka();
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Produkt getProdukt() {
		return produkt;
	}

	public void setProdukt(Produkt produkt) {
		this.produkt = produkt;
	}

	public String getSpecyfikator() {
		return specyfikator;
	}

	public void setSpecyfikator(String specyfikator) {
		this.specyfikator = specyfikator;
	}

	public BigDecimal getCena() {
		return cena;
	}

	public void setCena(BigDecimal cena) {
		this.cena = cena;
	}

	public BigDecimal getIloscPocz() {
		return iloscPocz;
	}

	public void setIloscPocz(BigDecimal iloscPocz) {
		this.iloscPocz = iloscPocz;
	}

	public Date getDataKsiegowania() {
		return dataKsiegowania;
	}

	public void setDataKsiegowania(Date dataKsiegowania) {
		this.dataKsiegowania = dataKsiegowania;
	}

	public Date getDataWaznosci() {
		return dataWaznosci;
	}

	public void setDataWaznosci(Date dataWaznosci) {
		this.dataWaznosci = dataWaznosci;
	}

	public Date getDataWprowadzenia() {
		return dataWprowadzenia;
	}

	public void setDataWprowadzenia(Date dataWprowadzenia) {
		this.dataWprowadzenia = dataWprowadzenia;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public String getJednostka() {
		return jednostka;
	}

	public void setJednostka(String jednostka) {
		this.jednostka = jednostka;
	}

	public BigDecimal getIloscTeraz() {
		return iloscTeraz;
	}


	public String getNumerFaktury() {
		return numerFaktury;
	}

	public void setNumerFaktury(String numerFaktury) {
		this.numerFaktury = numerFaktury;
	}

	public List<Wyprowadzenie> getWyprowadzenia() {
		return wyprowadzenia;
	}

	public Integer getNumerLinii() {
		return numerLinii;
	}

	public void setWyprowadzenia(List<Wyprowadzenie> wyprowadzenia) {
		this.wyprowadzenia = wyprowadzenia;
	}

	public void setNumerLinii(Integer numerLinii) {
		this.numerLinii = numerLinii;
	}

	@SuppressWarnings({"WeakerAccess"})
   public void setIloscTeraz(BigDecimal iloscTeraz) {
		this.iloscTeraz = iloscTeraz;
	}

	@Override
	public String toString() {
		return getSearchFormat();
	}

	@Transient
	public String getSearchFormat(){
		return ProductSearchCacheUtils.format(this);
	}

    @Override
    public String getNazwaProduktu() {
        return getProdukt().getNazwa();
    }
}
