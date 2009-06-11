package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.zarlok.ui.danie.PlanowaneTable;
import javax.persistence.*;
import static org.apache.commons.lang.StringUtils.isEmpty;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PlanowaneWyprowadzenie implements Cloneable, ProductSeachCacheSearchable{

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@ManyToOne(optional=false, fetch = FetchType.EAGER)
	private Produkt produkt;

	private String specyfikator;

	@NotEmpty
	private String jednostka;

	@NotNull
	@Column(name="ILOSC_JEDNOSTEK")
	private BigDecimal iloscJednostek;

   @ManyToOne
   private Danie danie;

	@ManyToMany
	private List<Wyprowadzenie> wyprowadzenia = new ArrayList<Wyprowadzenie>();

	@Transient
	private final PropertyChangeSupport support = new PropertyChangeSupport(this);

	public Long getId() {
		return id;
	}

	public Produkt getProdukt() {
		return produkt;
	}

	public String getSpecyfikator() {
		return specyfikator;
	}

	public String getJednostka() {
		return jednostka;
	}

	public BigDecimal getIloscJednostek() {
		return iloscJednostek;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setProdukt(Produkt produkt) {
		this.produkt = produkt;
	}

	public void setContents(ProductSearchCache productSearchCache) {
		if(productSearchCache==null){
			this.produkt = null;
			return;
		}
		this.produkt = productSearchCache.getProduct();
		if(isEmpty(getSpecyfikator())){
			setSpecyfikator(productSearchCache.getSpecyfikator());
		}
		if(isEmpty(getJednostka())){
			setJednostka(productSearchCache.getJednostka());
		}
	}

	/**
	 * Używane przez {@link PlanowaneTable}
    * @return .
	 */
	@Transient
	public ProductSearchCache getContents(){
		ProductSearchCache cache = new ProductSearchCache();
		cache.setProduct(getProdukt());
		return cache;
	}

	@SuppressWarnings({"WeakerAccess"})
   public void setSpecyfikator(String specyfikator) {
		String oldVal = this.specyfikator;
		this.specyfikator = specyfikator;
		support.firePropertyChange("specyfikator", oldVal, this.specyfikator);
	}

	@SuppressWarnings({"WeakerAccess"})
   public void setJednostka(String jednostka) {
		String oldval = this.jednostka;
		this.jednostka = jednostka;
		support.firePropertyChange("jednostka", oldval, this.jednostka);
	}

	public void setIloscJednostek(BigDecimal iloscJednostek) {
		this.iloscJednostek = iloscJednostek;
	}


	protected List<Wyprowadzenie> getWyprowadzenia() {
		return wyprowadzenia;
	}

	protected void setWyprowadzenia(List<Wyprowadzenie> wyprowadzenia) {
		this.wyprowadzenia = wyprowadzenia;
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
    public String getNazwaProduktu() {
        return getProdukt().getNazwa();
    }

   public String getSearchFormat(){
      return ProductSearchCacheUtils.format(this);
   }


   public Danie getDanie() {
      return danie;
   }

   public void setDanie(Danie danie) {
      this.danie = danie;
   }

}
