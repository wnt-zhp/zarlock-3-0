package cx.ath.jbzdak.zarlok.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import javax.persistence.*;


@NamedQueries({
	@NamedQuery(
			name="getProductSearchCache",
			query="SELECT DISTINCT new cx.ath.jbzdak.zarlok.entities.ProductSearchCache (pr.nazwa, pa.specyfikator, pa.jednostka, pr.id) " +
					"FROM Produkt pr, IN(pr.partie) pa"
	),
	@NamedQuery(
			name="getProductSearchCache2",
			query="SELECT DISTINCT new cx.ath.jbzdak.zarlok.entities.ProductSearchCache(pr.nazwa, pr.jednostka, pr.id) " +
					"FROM Produkt pr"
	),
	@NamedQuery(
			name="finishProductSearchCache",
			query="DELETE FROM ProductSearchCache psc WHERE " +
					"psc.specyfikator IS NULL AND " +
					"EXISTS(SELECT pp FROM ProductSearchCache pp WHERE pp.nazwaProduktu = psc.nazwaProduktu AND pp.specyfikator IS NOT NULL)"
	),
	@NamedQuery(
			name="deleteProductSearchCache",
			query="DELETE FROM ProductSearchCache"
	),
	@NamedQuery(
			name="filterProductSearchCache",
			query="SELECT psc FROM ProductSearchCache psc " +
					"WHERE " +
					"(LOWER(nazwaProduktu) like LOWER('%' || :nazwaProduktu || '%')) AND " +
					"(psc.specyfikator IS NULL OR LOWER(psc.specyfikator) like LOWER('%' || :specyfikator || '%')) AND " +
					"(psc.jednostka IS NULL OR LOWER(psc.jednostka) like LOWER('%' || :jednostka || '%'))"
	)
})
@Entity
@Table(name = "PRODUCT_SEARCH_CACHE")
public class ProductSearchCache implements ProductSeachCacheSearchable {

	@Id
	@GeneratedValue
   private
   @Column(name = "ID")
   Long id;

	@NotEmpty
   @Column(name = "NAZWA_PRODUKTU")
   private
   String nazwaProduktu;

   @Column(name = "SPECYFIKATOR")
	String specyfikator;

   @Column(name = "JEDNOSTKA6")
	private String jednostka;

	@NotNull
   @Column(name = "PRODUCT_ID")
   private Long productId;

	@Transient
   private Produkt product;

    public ProductSearchCache() {
		super();
	}


	public ProductSearchCache(Long id, String productName) {
		super();
		this.id = id;
		this.nazwaProduktu = productName;
	}

	public ProductSearchCache(String productName,
			String jednostka, Long productId) {
		super();
		this.nazwaProduktu = productName;
		this.jednostka = jednostka;
		this.productId = productId;
	}


	@SuppressWarnings({"SameParameterValue"})
   public ProductSearchCache(String productName, String specyfikator,
			String jednostka, Long productId) {
		super();
		this.nazwaProduktu = productName;
		this.specyfikator = specyfikator;
		this.jednostka = jednostka;
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

	public String getNazwaProduktu() {
		return nazwaProduktu;
	}

	public void setNazwaProduktu(String nazwaProduktu) {
		this.nazwaProduktu = nazwaProduktu;
	}

	public String getSpecyfikator() {
		return specyfikator;
	}

	public void setSpecyfikator(String specyfikator) {
		this.specyfikator = specyfikator;
	}

	public String getJednostka() {
		return jednostka;
	}

	public void setJednostka(String jednostka) {
		this.jednostka = jednostka;
	}

   @Override
   public String toString() {
      return new ToStringBuilder(this).
              append("id", id).
              append("nazwaProduktu", nazwaProduktu).
              append("specyfikator", specyfikator).
              append("jednostka", jednostka).
              append("productId", productId).
              append("product", product).
              toString();
   }

   public String toSearchFormat(){
		return ProductSearchCacheUtils.format(this);
	}


	public Produkt getProduct() {
		return product;
	}


	public void setProduct(Produkt produkt) {
		this.product = produkt;
	}
}
