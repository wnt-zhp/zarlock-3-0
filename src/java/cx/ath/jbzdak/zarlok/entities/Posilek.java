package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.jpaGui.Utils;
import javax.persistence.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "WeakerAccess"})
@Entity
@NamedQueries({
        @NamedQuery(
            name = "fetchPosilekKoszt",
            query = "SELECT p.id, SUM(d.koszt) FROM Posilek p, IN(p.dania) d GROUP BY p.id"
        ),
        @NamedQuery(
                name = "updatePosilkiKoszt",
                query = "UPDATE Posilek SET koszt = :koszt WHERE id = :id"
        )
})
public class Posilek {

	@Id
	@GeneratedValue
	private Long id;

	private String nazwa;

	private Boolean dodatkowy = Boolean.FALSE;

	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	private Dzien dzien;

	@Embedded
	private IloscOsob iloscOsob;

	@OneToMany(cascade= CascadeType.ALL)
	private List<Danie> dania = new ArrayList<Danie>();

	private BigDecimal koszt = BigDecimal.ZERO;

	private Boolean costStrict = Boolean.FALSE;

	public Posilek() {
		super();
	}

	public Posilek(String nazwa, Dzien d) {
		super();
		this.nazwa = nazwa;
		this.iloscOsob = d.getIloscOsob().clone();
		this.dzien = d;
	}

	@PrePersist @PreUpdate
	public void recalculateCost(){
		BigDecimal koszt = BigDecimal.ZERO;
		boolean costStrict = true;
		if(dania.isEmpty()){
			setCostStrict(false);
			return;
		}
		for(Danie d : dania){
			koszt = koszt.add(d.getKoszt()!=null?d.getKoszt():BigDecimal.ZERO, MathContext.DECIMAL32);
			costStrict&=d.getKoszt()!=null;
			costStrict&=d.getPlanowaneWyprowadzenia().isEmpty();
		}
      setCostStrict(costStrict);
      getDzien().recalculateDzienKoszt();
		setKoszt(Utils.round(koszt, 2));
	}

	public Long getId() {
		return id;
	}

	public String getNazwa() {
		return nazwa;
	}

	public Boolean getDodatkowy() {
		return dodatkowy;
	}

	public IloscOsob getIloscOsob() {
		return iloscOsob;
	}

	public List<Danie> getDania() {
		return dania;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public void setDodatkowy(Boolean dodatkowy) {
		this.dodatkowy = dodatkowy;
	}

	public void setIloscOsob(IloscOsob iloscOsob) {
		this.iloscOsob = iloscOsob;
	}

	public void setDania(List<Danie> dania) {
		this.dania = dania;
	}

	public BigDecimal getKoszt() {
		return koszt;
	}

	public Boolean getCostStrict() {

		return costStrict;
	}

	public void setKoszt(BigDecimal koszt) {
		this.koszt = koszt;
	}

	public void setCostStrict(Boolean costStrict) {
		this.costStrict = costStrict;
	}

	public Dzien getDzien() {
		return dzien;
	}

	public void setDzien(Dzien dzien) {
		this.dzien = dzien;
	}


}
