package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.zarlok.entities.listeners.ProductSearchCacheUpdater;
import org.hibernate.validator.Length;
import org.hibernate.validator.Range;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="PRODUKT")
@NamedQueries({
	@NamedQuery(
		name="getProduktNazwa",
		query = "SELECT DISTINCT p.nazwa FROM Produkt p WHERE p.nazwa LIKE CONCAT(CONCAT('%', :nazwa), '%')"
	),
	@NamedQuery(
		name="getProduktByNazwa",
		query = "SELECT p FROM Produkt p WHERE p.nazwa LIKE CONCAT(CONCAT('%', :nazwa), '%')"
	),
	@NamedQuery(
			name="getProduktJednostka",
			query = "SELECT DISTINCT p.jednostka FROM Produkt p WHERE LOWER(p.jednostka) LIKE LOWER(CONCAT(CONCAT('%', :jednostka), '%'))"
	),
	@NamedQuery(
			name="countProduktNazwa",
			query = "SELECT COUNT(p) FROM Produkt p WHERE p.nazwa = :nazwa"
   )
//,
//   @NamedQuery(
//           name = "getStanMagazynu",
//           query = "SELECT new cx.ath.jbzdak.zarlok.raport.stany.StanMagazynuEntryBean(\n" +
//                   " p,\n " +
//                   " SUM(w.iloscJednostek)" +
//                   ") FROM Partia p LEFT OUTER JOIN p.wyprowadzenia w\n" +
//                   "WHERE p.dataKsiegowania <= :data AND (p.dataWaznosci IS NULL OR p.dataWaznosci > :data)\n " +
//                   "AND (w IS NULL OR (w.dataWyprowadzenia < :data))\n " +
//                   "GROUP BY p, p.produkt.nazwa \n" +
//                   "ORDER BY p.produkt.nazwa\n"
//
//   )
        ,
   @NamedQuery(
           name = "getStanMagazynu",
           query = "SELECT new cx.ath.jbzdak.zarlok.raport.stany.StanMagazynuEntryBean(\n" +
                   " p,\n " +
                   " (SELECT SUM(w.iloscJednostek) FROM Wyprowadzenie w WHERE w.partia = p and (w.dataWyprowadzenia <= :data))" +
                   ") FROM Partia p LEFT OUTER JOIN p.wyprowadzenia w\n" +
                   "WHERE p.dataKsiegowania <= :data AND (p.dataWaznosci IS NULL OR p.dataWaznosci > :data)\n " +
                   //"AND (w IS NULL OR )\n " +
                   "GROUP BY p, p.produkt.nazwa \n" +
                   "ORDER BY p.produkt.nazwa\n"

   )
})
@EntityListeners({ProductSearchCacheUpdater.class})
public class Produkt {

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Length(min=1, max=50)
	@Column(name="NAZWA", unique=true, nullable=false)
	private String nazwa;

	@Length(min=1, max=50)
	@Column(name="JEDNOSTKA")
	private String jednostka;

	/**
	 * Data ważności zero znaczy: nieustalona
	 * -1 nieskończona
	 */
	@Nonnull
	@Range(min=-1)
	@Column(name="DATA_WAZNOSCI", nullable = false)
	private Integer dataWaznosci;

	@OneToMany(mappedBy="produkt")
   private
	List<Partia> partie = new ArrayList<Partia>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public String getJednostka() {
		return jednostka;
	}

	public void setJednostka(String jednostka) {
		this.jednostka = jednostka;
	}

	public Integer getDataWaznosci() {
		return dataWaznosci;
	}

	public void setDataWaznosci(Integer dataWaznosci) {
		this.dataWaznosci = dataWaznosci;
	}

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder();
      sb.append("Produkt");
      sb.append("{id=").append(id);
      sb.append(", nazwa='").append(nazwa).append('\'');
      sb.append(", jednostka='").append(jednostka).append('\'');
      sb.append(", dataWaznosci=").append(dataWaznosci);
//      try{
//         sb.append(", partie=").append(partie);
//      }catch (HibernateException e){
//         sb.append(", partie=").append("BRAK PARTII");
//      }
      sb.append('}');
      return sb.toString();
   }

   public List<Partia> getPartie() {
		return partie;
	}

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Produkt)) return false;

      Produkt produkt = (Produkt) o;

      if (dataWaznosci != null ? !dataWaznosci.equals(produkt.dataWaznosci) : produkt.dataWaznosci != null)
         return false;
      if (id != null ? !id.equals(produkt.id) : produkt.id != null) return false;
      if (jednostka != null ? !jednostka.equals(produkt.jednostka) : produkt.jednostka != null) return false;
      if (nazwa != null ? !nazwa.equals(produkt.nazwa) : produkt.nazwa != null) return false;

      return true;
   }

   @Override
   public int hashCode() {
      int result = id != null ? id.hashCode() : 0;
      result = 31 * result + (nazwa != null ? nazwa.hashCode() : 0);
      result = 31 * result + (jednostka != null ? jednostka.hashCode() : 0);
      result = 31 * result + (dataWaznosci != null ? dataWaznosci.hashCode() : 0);
      return result;
   }

   public void setPartie(List<Partia> partie) {
		this.partie = partie;
	}
}
