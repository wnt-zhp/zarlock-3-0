package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.zarlok.entities.xml.adapters.CourseAdapter;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@XmlType
@NamedQueries({
//        @NamedQuery(
//            name = "fetchDaniaKoszt",
//            query = "SELECT d.id, " +
//                    "(" +
//                    " ( SELECT SUM(w.partia.cena * w.iloscJednostek) FROM Expenditure w WHERE w MEMBER OF d.expenditures )" +
////                    "   /(SELECT (p.iloscOsob.iloscInnych + p.iloscOsob.iloscKadry + p.iloscOsob.iloscUczestnikow) FROM Meal p, IN(p.dania ) d2 WHERE d2.id =d.id )" +
//                      //" /(d.meal.iloscOsob.iloscInnych + d.meal.iloscOsob.iloscKadry + d.meal.iloscOsob.iloscUczestnikow)" +
//                    ") FROM Course d, IN(d.expenditures) w "
////                    "GROUP BY " +
////                    "   d.id"//, d.meal.iloscOsob, d.meal "
//        ),
//        @NamedQuery(
//                name = "updateDanieKoszt",
//               query = "UPDATE Course d SET d.cost = :cost WHERE d.id = :id")
})
public class Course {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	Long id;

	@NotNull
	@ManyToOne
   @JoinColumn(name = "MEAL_ID")
   Meal meal;

	@NotEmpty
	@Column(name = "NAME")
	String name;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "course")
	List<Expenditure> expenditures = new ArrayList<Expenditure>();

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "course")
	List<PlannedExpenditure> plannedExpenditures = new ArrayList<PlannedExpenditure>();

	@CheckForNull
   @Column(name = "COST")
	BigDecimal cost;

	@Nonnull
   @Column(name = "COST_STRICT")
	Boolean costStrict;

      @XmlID
   @XmlJavaTypeAdapter(CourseAdapter.class)
   public Long getId() {
		return id;
	}

   @XmlAttribute(required = true)
	public String getName() {
		return name;
	}

   @XmlTransient
	public List<Expenditure> getExpenditures() {
		return expenditures;
	}

   @XmlTransient
	public List<PlannedExpenditure> getPlannedExpenditures() {
		return plannedExpenditures;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setExpenditures(List<Expenditure> expenditure) {
		this.expenditures = expenditure;
	}

	public void setPlannedExpenditures(List<PlannedExpenditure> plannedExpenditures) {
		this.plannedExpenditures = plannedExpenditures;
	}

   @XmlTransient
	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

   @XmlIDREF
   public Meal getMeal() {
      return meal;
   }

   public void setMeal(Meal meal) {
      this.meal = meal;
   }

   @XmlTransient
   public Boolean getCostStrict() {
		return costStrict;
	}

	public void setCostStrict(Boolean costStrict) {
		this.costStrict = costStrict;
	}
}
