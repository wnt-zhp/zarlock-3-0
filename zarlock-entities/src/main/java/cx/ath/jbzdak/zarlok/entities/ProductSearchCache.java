package cx.ath.jbzdak.zarlok.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import javax.persistence.*;


//@NamedQueries({
//	@NamedQuery(
//			name="getProductSearchCache",
//			query="SELECT DISTINCT new cx.ath.jbzdak.zarlok.entities.ProductSearchCache (pr.name, pa.specifier, pa.unit, pr.id) " +
//					"FROM Product pr, IN(pr.partie) pa"
//	),
//	@NamedQuery(
//			name="getProductSearchCache2",
//			query="SELECT DISTINCT new cx.ath.jbzdak.zarlok.entities.ProductSearchCache(pr.name, pr.unit, pr.id) " +
//					"FROM Product pr"
//	),
//	@NamedQuery(
//			name="finishProductSearchCache",
//			query="DELETE FROM ProductSearchCache psc WHERE " +
//					"psc.specifier IS NULL AND " +
//					"EXISTS(SELECT pp FROM ProductSearchCache pp WHERE pp.productName = psc.productName AND pp.specifier IS NOT NULL)"
//	),
//	@NamedQuery(
//			name="deleteProductSearchCache",
//			query="DELETE FROM ProductSearchCache"
//	),
//	@NamedQuery(
//			name="filterProductSearchCache",
//			query="SELECT psc FROM ProductSearchCache psc " +
//					"WHERE " +
//					"(LOWER(productName) like LOWER('%' || :productName || '%')) AND " +
//					"(psc.specifier IS NULL OR LOWER(psc.specifier) like LOWER('%' || :specifier || '%')) AND " +
//					"(psc.unit IS NULL OR LOWER(psc.unit) like LOWER('%' || :unit || '%'))"
//	)
//})
@Entity
@Table(name = "PRODUCT_SEARCH_CACHE")
public class ProductSearchCache implements IProductSearchCache {

	@Id
	@GeneratedValue
   private
   @Column(name = "ID")
   Long id;

	@NotEmpty
   @Column(name = "PRODUCT_NAME")
   private
   String productName;

   @Column(name = "SPECIFIER")
	String specifier;

   @Column(name = "UNIT")
	private String unit;

	@NotNull
   @Column(name = "PRODUCT_ID")
   private Long productId;

	@Transient
   private Product product;

   public ProductSearchCache() {
		super();
	}


	public ProductSearchCache(Long id, String productName) {
		super();
		this.id = id;
		this.productName = productName;
	}

	public ProductSearchCache(String productName,
			String unit, Long productId) {
		super();
		this.productName = productName;
		this.unit = unit;
		this.productId = productId;
	}


	@SuppressWarnings({"SameParameterValue"})
   public ProductSearchCache(String productName, String specifier,
			String unit, Long productId) {
		super();
		this.productName = productName;
		this.specifier = specifier;
		this.unit = unit;
		this.productId = productId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSpecifier() {
		return specifier;
	}

	public void setSpecifier(String specifier) {
		this.specifier = specifier;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

   @Override
   public String toString() {
      return new ToStringBuilder(this).
              append("id", id).
              append("productName", productName).
              append("specifier", specifier).
              append("unit", unit).
              append("productId", productId).
              append("product", product).
              toString();
   }

   public String toSearchFormat(){
		return ProductSearchCacheUtils.format(this);
	}


	public Product getProduct() {
		return product;
	}


	public void setProduct(Product product) {
		this.product = product;
	}
}
