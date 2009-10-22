package cx.ath.jbzdak.zarlok.entities;

import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
//        @NamedQuery(
//            name = "fetchDaniaKoszt",
//            query = "SELECT d.id, " +
//                    "(" +
//                    " ( SELECT SUM(w.partia.cena * w.iloscJednostek) FROM Wyprowadzenie w WHERE w MEMBER OF d.wyprowadzenia )" +
////                    "   /(SELECT (p.iloscOsob.iloscInnych + p.iloscOsob.iloscKadry + p.iloscOsob.iloscUczestnikow) FROM Posilek p, IN(p.dania ) d2 WHERE d2.id =d.id )" +
//                      //" /(d.posilek.iloscOsob.iloscInnych + d.posilek.iloscOsob.iloscKadry + d.posilek.iloscOsob.iloscUczestnikow)" +
//                    ") FROM Danie d, IN(d.wyprowadzenia) w "
////                    "GROUP BY " +
////                    "   d.id"//, d.posilek.iloscOsob, d.posilek "
//        ),
        @NamedQuery(
                name = "updateDanieKoszt",
               query = "UPDATE Danie d SET d.koszt = :koszt WHERE d.id = :id")
})
public class Danie {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	Long id;

	@NotNull
	@ManyToOne
   @JoinColumn(name = "POSILEK_ID")
	Posilek posilek;

	@NotEmpty
	@Column(name = "NAZWA")
	String nazwa;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "danie")
	List<Wyprowadzenie> wyprowadzenia = new ArrayList<Wyprowadzenie>();

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "danie")
	List<PlanowaneWyprowadzenie> planowaneWyprowadzenia = new ArrayList<PlanowaneWyprowadzenie>();

	@CheckForNull
   @Column(name = "KOSZT")
	BigDecimal koszt;

	@Nonnull
	Boolean costStrict;

	public Long getId() {
		return id;
	}

	public String getNazwa() {
		return nazwa;
	}

	public List<Wyprowadzenie> getWyprowadzenia() {
		return wyprowadzenia;
	}

	public List<PlanowaneWyprowadzenie> getPlanowaneWyprowadzenia() {
		return planowaneWyprowadzenia;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public void setWyprowadzenia(List<Wyprowadzenie> wyprowadzenie) {
		this.wyprowadzenia = wyprowadzenie;
	}

	public void setPlanowaneWyprowadzenia(List<PlanowaneWyprowadzenie> planowaneWyprowadzenia) {
		this.planowaneWyprowadzenia = planowaneWyprowadzenia;
	}

	public BigDecimal getKoszt() {
		return koszt;
	}

	public void setKoszt(BigDecimal koszt) {
		this.koszt = koszt;
	}


	public Posilek getPosilek() {
		return posilek;
	}

	public void setPosilek(Posilek posilek) {
		this.posilek = posilek;
	}

	public Boolean getCostStrict() {
		return costStrict;
	}

	public void setCostStrict(Boolean costStrict) {
		this.costStrict = costStrict;
	}
}
