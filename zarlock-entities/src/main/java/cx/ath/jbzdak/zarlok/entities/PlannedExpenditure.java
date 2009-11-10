package cx.ath.jbzdak.zarlok.entities;

import static org.apache.commons.lang.StringUtils.isEmpty;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PLANNED_EXPENDITURE")
public class PlannedExpenditure implements Cloneable, IProductSearchCache {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@ManyToOne(optional=false, fetch = FetchType.EAGER)
	private Product product;

	private String specifier;

	@NotEmpty
	private String unit;

	@NotNull
	@Column(name="QUANTITY")
	private BigDecimal quantity;

   @ManyToOne
   private Course course;

	@ManyToMany
	private List<Expenditure> expenditures = new ArrayList<Expenditure>();

	@Transient
	private final PropertyChangeSupport support = new PropertyChangeSupport(this);

   public Long getId() {
		return id;
	}

	public Product getProdukt() {
		return product;
	}

	public String getSpecifier() {
		return specifier;
	}

	public String getUnit() {
		return unit;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setProdukt(Product product) {
		this.product = product;
	}

	public void setContents(ProductSearchCache productSearchCache) {
		if(productSearchCache==null){
			this.product = null;
			return;
		}
		this.product = productSearchCache.getProduct();
		if(isEmpty(getSpecifier())){
			setSpecifier(productSearchCache.getSpecifier());
		}
		if(isEmpty(getUnit())){
			setUnit(productSearchCache.getUnit());
		}
	}

	/**
    * @return .
	 */
	@Transient
	public ProductSearchCache getContents(){
		ProductSearchCache cache = new ProductSearchCache();
		cache.setProduct(getProdukt());
		return cache;
	}

	@SuppressWarnings({"WeakerAccess"})
   public void setSpecifier(String specifier) {
		String oldVal = this.specifier;
		this.specifier = specifier;
		support.firePropertyChange("specifier", oldVal, this.specifier);
	}

	@SuppressWarnings({"WeakerAccess"})
   public void setUnit(String unit) {
		String oldVal = this.unit;
		this.unit = unit;
		support.firePropertyChange("unit", oldVal, this.unit);
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}


	protected List<Expenditure> getExpenditures() {
		return expenditures;
	}

	protected void setExpenditures(List<Expenditure> expenditures) {
		this.expenditures = expenditures;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// wont happen
			throw new RuntimeException(e);
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		support.addPropertyChangeListener(propertyName, listener);
	}

	@Transient
	public PropertyChangeListener[] getPropertyChangeListeners() {
		return support.getPropertyChangeListeners();
	}

	public PropertyChangeListener[] getPropertyChangeListeners(
			String propertyName) {
		return support.getPropertyChangeListeners(propertyName);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		support.removePropertyChangeListener(propertyName, listener);
	}

    @Override
    public String getProductName() {
        return getProdukt().getName();
    }

   public String getSearchFormat(){
      return ProductSearchCacheUtils.format(this);
   }


   public Course getDanie() {
      return course;
   }

   public void setDanie(Course course) {
      this.course = course;
   }

}
