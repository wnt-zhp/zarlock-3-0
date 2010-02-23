package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.zarlok.entities.xml.adapters.MealAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess"})
@Entity
@XmlType
//@NamedQueries({
//        @NamedQuery(
//            name = "fetchPosilekKoszt",
//            query = "SELECT p.id, SUM(d.cost) FROM Meal p, IN(p.dania) d GROUP BY p.id"
//        ),
//        @NamedQuery(
//                name = "updatePosilkiKoszt",
//                query = "UPDATE Meal SET cost = :cost WHERE id = :id"
//        )
//})
@Table(name = "MEAL")
public class Meal {

	@Id
	@GeneratedValue
   @Column(name = "ID")
	private Long id;

   @Column(name = "NAME")
	private String name;

   @Column(name = "ADDITIONAL")
	private Boolean additional = Boolean.FALSE;

   @JoinColumn(name = "DZIEN_ID")
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	private Day day;

	@Embedded
	private PeopleNo peopleNo;

	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "meal")
	private List<Course> dania = new ArrayList<Course>();

   @Column(name = "COST")
	private BigDecimal cost = BigDecimal.ZERO;

   @Column(name = "COST_STRICT")
	private Boolean costStrict = Boolean.FALSE;

   public Meal() {
		super();
	}

	public Meal(String name, Day d) {
		super();
		this.name = name;
		this.peopleNo = d.getPeopleNo().clone();
		this.day = d;
	}

   @XmlID
   @XmlJavaTypeAdapter(MealAdapter.class)
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

   @XmlAttribute(required = true)
	public Boolean getAdditional() {
		return additional;
	}

   @XmlTransient
	public List<Course> getCourses() {
		return dania;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAdditional(Boolean additional) {
		this.additional = additional;
	}

	public void setDania(List<Course> dania) {
		this.dania = dania;
	}

   @XmlTransient
	public BigDecimal getCost() {
		return cost;
	}

   @XmlTransient
	public Boolean getCostStrict() {
		return costStrict;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public void setCostStrict(Boolean costStrict) {
		this.costStrict = costStrict;
	}

   @XmlIDREF
   public Day getDay() {
      return day;
   }

   public void setDay(Day day) {
      this.day = day;
   }

   public PeopleNo getPeopleNo() {
      return peopleNo;
   }

   public void setPeopleNo(PeopleNo peopleNo) {
      this.peopleNo = peopleNo;
   }
}
