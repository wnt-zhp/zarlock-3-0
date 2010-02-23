package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.zarlok.entities.xml.adapters.DayAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@XmlType
@Table(name = "DAY")
//@NamedQueries({
//       @NamedQuery(
//               name = "fetchDzienKoszt",
//               query = "SELECT d.id, SUM(danie.cost) FROM Day d, IN(d.meals) p, IN(p.dania ) danie GROUP BY d.id"
//       ),@NamedQuery(
//                name = "updateDzienKoszt",
//                query = "UPDATE Day SET rate = :cost WHERE id = :id"
//        )
//}
//)
public class Day {

	@Id
	@GeneratedValue
	@Column(name = "ID")
   private
   Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "DAY", unique = true)
   private
   Date date;

	@OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy="day")
   private
   List<Meal> meals = new ArrayList<Meal>();

	@Embedded
   private
   PeopleNo peopleNo = new PeopleNo();

   /**
    * Stawka dzienna
    */
   private BigDecimal rate;

   @PreUpdate @PrePersist
   public void recalculateDayCost(){
      rate = BigDecimal.ZERO;
      for (Meal meal : meals) {
         rate =  rate.add(meal.getCost(), MathContext.DECIMAL32);
      }
      rate = Utils.round(rate, 2);
   }

   @XmlID
   @XmlJavaTypeAdapter(DayAdapter.class)
	public Long getId() {
		return id;
	}

   @XmlAttribute(required = true)
	public Date getDate() {
		return date;
	}

   @XmlTransient
	public List<Meal> getMeals() {
		return meals;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setMeals(List<Meal> meals) {
		this.meals = meals;
	}

   @XmlElement(required = true)
   public PeopleNo getPeopleNo() {
      return peopleNo;
   }

   public void setPeopleNo(PeopleNo peopleNo) {
      this.peopleNo = peopleNo;
   }

   @XmlAttribute(required = true)
   public BigDecimal getRate() {
      return rate;
   }

   public void setRate(BigDecimal rate) {
      this.rate = rate;
   }
}
