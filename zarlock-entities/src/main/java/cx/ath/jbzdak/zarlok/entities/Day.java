package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.jpaGui.Utils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
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
	@Column(name = "DATA", unique = true)
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

	public Long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

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

   public PeopleNo getPeopleNo() {
      return peopleNo;
   }

   public void setPeopleNo(PeopleNo peopleNo) {
      this.peopleNo = peopleNo;
   }

   public BigDecimal getRate() {
      return rate;
   }

   public void setRate(BigDecimal rate) {
      this.rate = rate;
   }
}
