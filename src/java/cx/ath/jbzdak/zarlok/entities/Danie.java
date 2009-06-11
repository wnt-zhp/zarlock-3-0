package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.jpaGui.Utils;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.*;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
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
	Posilek posilek;

	@NotEmpty
	@Column(name = "NAZWA")
	String nazwa;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "danie")
	List<Wyprowadzenie> wyprowadzenia = new ArrayList<Wyprowadzenie>();

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "danie")
	List<PlanowaneWyprowadzenie> planowaneWyprowadzenia = new ArrayList<PlanowaneWyprowadzenie>();

	@Nullable
	BigDecimal koszt;

	@Nonnull
	@NotNull
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

	@PrePersist @PreUpdate
	public void updateKoszt(){
		if(getPosilek().getIloscOsob()==null){
			koszt = null;
			costStrict = Boolean.FALSE;
			return;
		}
		BigDecimal koszt = BigDecimal.ZERO;
		for(Wyprowadzenie w : getWyprowadzenia()){
			koszt = koszt.add(w.getWartosc(), MathContext.DECIMAL32);
		}
		costStrict = planowaneWyprowadzenia.isEmpty();
		koszt = koszt.divide(BigDecimal.valueOf(getPosilek().getIloscOsob().getSuma()), MathContext.DECIMAL32);
		this.koszt = Utils.round(koszt,2);
      getPosilek().recalculateCost();
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
