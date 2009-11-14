package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.db.dao.annotations.LifecycleListener;
import cx.ath.jbzdak.jpaGui.db.dao.annotations.LifecyclePhase;
import org.hibernate.validator.NotNull;
import org.slf4j.Logger;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;

/**
 * Jedno wyprowadzenie z magazynu, przypisane do jednej partii.
 * @author jb
 *
 */
@Entity
@Table(name = "EXPENDITURE")
public class Expenditure implements Cloneable, IProductSearchCache {

   private static final Logger LOGGER = Utils.makeLogger();

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

   @JoinColumn(name = "BATCH_ID")
	@ManyToOne(optional=false, fetch = FetchType.EAGER)
	private Batch batch;

	@NotNull
	@Column(name="QUANTITY")
	private BigDecimal quantity;

	@NotNull
	@Column(name="EXPENDITURE_DATE")
	private Date expenditureDate;

	@NotNull
	@Column(name="TYTULEM")
	private String tytulem;

	@NotNull
	@Column(name="CREATE_DATE")
	private Date createDate;

   @JoinColumn(name = "COURSE_ID")
   @ManyToOne(optional = true, fetch = FetchType.LAZY)
   private Course course;


   public Expenditure() {
		super();
	}

	public Expenditure(Batch batch) {
		super();
		this.batch = batch;
	}

   @SuppressWarnings({"WeakerAccess"})
   public Course getDanie() {
      return course;
   }

   public void setDanie(Course course) {
      this.course = course;
   }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Date getExpenditureDate() {
		return expenditureDate;
	}

	public void setExpenditureDate(Date expenditureDate) {
		this.expenditureDate = expenditureDate;
	}

	public String getTytulem() {
		return tytulem;
	}

	public void setTytulem(String tytulem) {
		this.tytulem = tytulem;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

   public Batch getBatch() {
      return batch;
   }

   public void setBatch(Batch batch) {
      this.batch = batch;
   }

    @Transient
    public BigDecimal getValue(){
        return Utils.round(getBatch().getPrice().multiply(getQuantity(), MathContext.DECIMAL32),2);
    }

   @Override
   public String getUnit() {
      return getBatch().getUnit();
   }

   @Override
   public String getProductName() {
      return getBatch().getProductName();
   }

   @Override
   public String getSpecifier() {
      return getBatch().getSpecifier();
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder();
      sb.append("Expenditure");
      sb.append("{id=").append(id);
      sb.append(", quantity=").append(quantity);
      sb.append(", expenditureDate=").append(expenditureDate);
      sb.append(", tytulem='").append(tytulem).append('\'');
      sb.append(", createDate=").append(createDate);
      //sb.append(", course=").append(course);
      sb.append('}');
      return sb.toString();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Expenditure)) return false;

      Expenditure that = (Expenditure) o;

      if (course != null ? !course.equals(that.course) : that.course != null) return false;
      if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null)
         return false;
      if (expenditureDate != null ? !expenditureDate.equals(that.expenditureDate) : that.expenditureDate != null)
         return false;
      if (id != null ? !id.equals(that.id) : that.id != null) return false;
      if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null)
         return false;
      if (batch != null ? !batch.equals(that.batch) : that.batch != null) return false;
      if (tytulem != null ? !tytulem.equals(that.tytulem) : that.tytulem != null) return false;

      return true;
   }

   @Override
   public int hashCode() {
      int result = id != null ? id.hashCode() : 0;
      result = 31 * result + (batch != null ? batch.hashCode() : 0);
      result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
      result = 31 * result + (expenditureDate != null ? expenditureDate.hashCode() : 0);
      result = 31 * result + (tytulem != null ? tytulem.hashCode() : 0);
      result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
      result = 31 * result + (course != null ? course.hashCode() : 0);
      return result;
   }
}
