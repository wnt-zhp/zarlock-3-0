package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.jpaGui.Utils;
import static cx.ath.jbzdak.jpaGui.Utils.getRelativeDate;
import cx.ath.jbzdak.zarlok.entities.listeners.PartiaSearchCacheUpdater;
import org.hibernate.HibernateException;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.*;
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
                query = "SELECT p FROM Partia p " +
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
        )//,
//        @NamedQuery(
//                name = "getKartotekiContents",
//                query = "SELECT new cx.ath.jbzdak.zarlok.raport.kartoteki.KartotekaRaportBean(" +
//                        "p.produkt.nazwa, " +
//                        "p.specyfikator," +
//                        "p.cena) " +
//                        "FROM Partia p " +
//                        "GROUP BY p.produkt.nazwa, p.jednostka, p.specyfikator, p.cena "
//        ),
//        @NamedQuery(
//                name = "getKartotekaContentsStage2",
//                query = "SELECT DISTINCT (p) " +
//                        "FROM Partia p " +
//                        "LEFT JOIN FETCH p.wyprowadzenia " +
//                        "WHERE p.produkt.nazwa = :nazwa  AND p.specyfikator = :specyfikator AND p.cena = :cena"
//        )
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
	@NotEmpty
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
      iloscPocz = Utils.round(iloscPocz, 2);

	}

	@PreUpdate
	public void preUpdate(){
		//recalculateIloscTeraz();
      cena = Utils.round(cena, 2);
      iloscPocz = Utils.round(iloscPocz, 2);
      if(iloscTeraz!=null){
         iloscTeraz = Utils.round(iloscTeraz, 2);
      }
	}


	public void recalculateIloscTeraz(){
	   setIloscTeraz(PartieUtils.getIloscTeraz(this));
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

	@Override
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

	@Override
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

	@Transient
	public String getSearchFormat(){
		return ProductSearchCacheUtils.format(this);
	}

    @Override
    public String getNazwaProduktu() {
        return getProdukt().getNazwa();
    }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder();
      sb.append("Partia");
      sb.append("{id=").append(id);
      sb.append(", produkt=").append(produkt);
      sb.append(", specyfikator='").append(specyfikator).append('\'');
      sb.append(", cena=").append(cena);
      sb.append(", iloscPocz=").append(iloscPocz);
      sb.append(", jednostka='").append(jednostka).append('\'');
      sb.append(", iloscTeraz=").append(iloscTeraz);
      sb.append(", dataKsiegowania=").append(dataKsiegowania);
      sb.append(", dataWaznosci=").append(dataWaznosci);
      sb.append(", dataWprowadzenia=").append(dataWprowadzenia);
      try{
         sb.append(", wyprowadzenia=").append(getWyprowadzenia());
      }catch (HibernateException e){
          sb.append(", wyprowadzenia=NIE ZAŁADOWANO");
         //Hibernate exception moze polecieć -- olać!
      }
      sb.append(", opis='").append(opis).append('\'');
      sb.append(", numerFaktury='").append(numerFaktury).append('\'');
      sb.append(", numerLinii=").append(numerLinii);
      sb.append('}');
      return sb.toString();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Partia)) return false;

      Partia partia = (Partia) o;

      if (cena != null ? !cena.equals(partia.cena) : partia.cena != null) return false;
      if (dataKsiegowania != null ? !dataKsiegowania.equals(partia.dataKsiegowania) : partia.dataKsiegowania != null)
         return false;
      if (dataWaznosci != null ? !dataWaznosci.equals(partia.dataWaznosci) : partia.dataWaznosci != null) return false;
      if (dataWprowadzenia != null ? !dataWprowadzenia.equals(partia.dataWprowadzenia) : partia.dataWprowadzenia != null)
         return false;
      if (id != null ? !id.equals(partia.id) : partia.id != null) return false;
      if (iloscPocz != null ? !iloscPocz.equals(partia.iloscPocz) : partia.iloscPocz != null) return false;
      if (iloscTeraz != null ? !iloscTeraz.equals(partia.iloscTeraz) : partia.iloscTeraz != null) return false;
      if (jednostka != null ? !jednostka.equals(partia.jednostka) : partia.jednostka != null) return false;
      if (numerFaktury != null ? !numerFaktury.equals(partia.numerFaktury) : partia.numerFaktury != null) return false;
      if (numerLinii != null ? !numerLinii.equals(partia.numerLinii) : partia.numerLinii != null) return false;
      if (opis != null ? !opis.equals(partia.opis) : partia.opis != null) return false;
      if (produkt != null ? !produkt.equals(partia.produkt) : partia.produkt != null) return false;
      if (specyfikator != null ? !specyfikator.equals(partia.specyfikator) : partia.specyfikator != null) return false;

      return true;
   }

   @Override
   public int hashCode() {
      int result = id != null ? id.hashCode() : 0;
      result = 31 * result + (produkt != null ? produkt.hashCode() : 0);
      result = 31 * result + (specyfikator != null ? specyfikator.hashCode() : 0);
      result = 31 * result + (cena != null ? cena.hashCode() : 0);
      result = 31 * result + (iloscPocz != null ? iloscPocz.hashCode() : 0);
      result = 31 * result + (jednostka != null ? jednostka.hashCode() : 0);
      result = 31 * result + (iloscTeraz != null ? iloscTeraz.hashCode() : 0);
      result = 31 * result + (dataKsiegowania != null ? dataKsiegowania.hashCode() : 0);
      result = 31 * result + (dataWaznosci != null ? dataWaznosci.hashCode() : 0);
      result = 31 * result + (dataWprowadzenia != null ? dataWprowadzenia.hashCode() : 0);
      result = 31 * result + (opis != null ? opis.hashCode() : 0);
      result = 31 * result + (numerFaktury != null ? numerFaktury.hashCode() : 0);
      result = 31 * result + (numerLinii != null ? numerLinii.hashCode() : 0);
      return result;
   }
}
