package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.jpaGui.Utils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess"})
@Entity
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

	@OneToMany(cascade= {CascadeType.PERSIST, CascadeType.REMOVE} )
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

   @PrePersist @PreUpdate
	public void recalculateCost(){
		BigDecimal cost = BigDecimal.ZERO;
		boolean costStrict = true;
		if(dania.isEmpty()){
			setCostStrict(false);
			return;
		}
		for(Course d : dania){
			cost = cost.add(d.getCost()!=null?d.getCost():BigDecimal.ZERO, MathContext.DECIMAL32);
			costStrict&=d.getCost()!=null;
			costStrict&=d.getPlannedExpenditures().isEmpty();
		}
      setCostStrict(costStrict);
      getDzien().recalculateDayCost();
		setCost(Utils.round(cost, 2));
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Boolean getAdditional() {
		return additional;
	}

	public PeopleNo getIloscOsob() {
		return peopleNo;
	}

	public List<Course> getDania() {
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

	public void setIloscOsob(PeopleNo peopleNo) {
		this.peopleNo = peopleNo;
	}

	public void setDania(List<Course> dania) {
		this.dania = dania;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public Boolean getCostStrict() {

		return costStrict;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public void setCostStrict(Boolean costStrict) {
		this.costStrict = costStrict;
	}

	public Day getDzien() {
		return day;
	}

	public void setDzien(Day day) {
		this.day = day;
	}


}
