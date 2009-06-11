package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.jpaGui.Utils;
import javax.persistence.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "DZIEN")
@NamedQueries({
       @NamedQuery(
               name = "fetchDzienKoszt",
               query = "SELECT d.id, SUM(danie.koszt) FROM Dzien d, IN(d.posilki) p, IN(p.dania ) danie GROUP BY d.id"
       ),@NamedQuery(
                name = "updateDzienKoszt",
                query = "UPDATE Dzien SET stawkaDzienna = :koszt WHERE id = :id"
        )
}
)
public class Dzien{

	@Id
	@GeneratedValue
	@Column(name = "ID")
   private
   Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA", unique = true)
   private
   Date data;

	@OneToMany(cascade = CascadeType.ALL, mappedBy="dzien")
   private
   List<Posilek> posilki = new ArrayList<Posilek>();

	@Embedded
   private
   IloscOsob iloscOsob = new  IloscOsob();

   private BigDecimal stawkaDzienna;

   @PreUpdate @PrePersist
   public void recalculateDzienKoszt(){
      stawkaDzienna = BigDecimal.ZERO;
      for (Posilek posilek : posilki) {
         stawkaDzienna =  stawkaDzienna.add(posilek.getKoszt(), MathContext.DECIMAL32);
      }
      stawkaDzienna = Utils.round(stawkaDzienna, 2);
   }

	public Long getId() {
		return id;
	}

	public Date getData() {
		return data;
	}

	public List<Posilek> getPosilki() {
		return posilki;
	}

	public IloscOsob getIloscOsob() {
		return iloscOsob;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setPosilki(List<Posilek> posilki) {
		this.posilki = posilki;
	}

	public void setIloscOsob(IloscOsob iloscOsob) {
		this.iloscOsob = iloscOsob;
	}

   public BigDecimal getStawkaDzienna() {
      return stawkaDzienna;
   }

   public void setStawkaDzienna(BigDecimal stawkaDzienna) {
      this.stawkaDzienna = stawkaDzienna;
   }
}
