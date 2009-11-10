package cx.ath.jbzdak.zarlok.entities;

import org.hibernate.validator.Length;
import org.hibernate.validator.Range;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="PRODUCT")
//@NamedQueries({
//	@NamedQuery(
//		name="getProduktNazwa",
//		query = "SELECT DISTINCT p.name FROM Product p WHERE p.name LIKE CONCAT(CONCAT('%', :name), '%')"
//	),
//	@NamedQuery(
//		name="getProduktByNazwa",
//		query = "SELECT p FROM Product p WHERE p.name LIKE CONCAT(CONCAT('%', :name), '%')"
//	),
//	@NamedQuery(
//			name="getProduktJednostka",
//			query = "SELECT DISTINCT p.unit FROM Product p WHERE LOWER(p.unit) LIKE LOWER(CONCAT(CONCAT('%', :unit), '%'))"
//	),
//	@NamedQuery(
//			name="countProduktNazwa",
//			query = "SELECT COUNT(p) FROM Product p WHERE p.name = :name"
//   )
//})
public class Product {

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Length(min=1, max=50)
	@Column(name="name", unique=true, nullable=false)
	private String name;

	@Length(min=1, max=50)
	@Column(name="unit")
	private String unit;

	/**
	 * Data ważności zero znaczy: nieustalona
	 * -1 nieskończona
	 */
	@Nonnull
	@Range(min=-1)
	@Column(name="EXPIRY_DATE", nullable = false)
	private Integer expiryDate;

	@OneToMany(mappedBy="product")
   private
	List<Batch> batches = new ArrayList<Batch>();

   public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Integer expiryDate) {
		this.expiryDate = expiryDate;
	}

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder();
      sb.append("Product");
      sb.append("{id=").append(id);
      sb.append(", name='").append(name).append('\'');
      sb.append(", unit='").append(unit).append('\'');
      sb.append(", expiryDate=").append(expiryDate);
      sb.append('}');
      return sb.toString();
   }

   public List<Batch> getBatches() {
		return batches;
	}

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Product)) return false;

      Product product = (Product) o;

      if (expiryDate != null ? !expiryDate.equals(product.expiryDate) : product.expiryDate != null)
         return false;
      if (id != null ? !id.equals(product.id) : product.id != null) return false;
      if (unit != null ? !unit.equals(product.unit) : product.unit != null) return false;
      if (name != null ? !name.equals(product.name) : product.name != null) return false;

      return true;
   }

   @Override
   public int hashCode() {
      int result = id != null ? id.hashCode() : 0;
      result = 31 * result + (name != null ? name.hashCode() : 0);
      result = 31 * result + (unit != null ? unit.hashCode() : 0);
      result = 31 * result + (expiryDate != null ? expiryDate.hashCode() : 0);
      return result;
   }

   public void setBatches(List<Batch> batches) {
		this.batches = batches;
	}
}
