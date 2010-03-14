package cx.ath.jbzdak.zarlok.entities;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.*;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.zarlok.entities.listeners.PartiaSearchCacheUpdater;
import cx.ath.jbzdak.zarlok.entities.xml.adapters.BatchAdapter;

import static cx.ath.jbzdak.jpaGui.Utils.getRelativeDate;


/**
 * Batch produktu.
 */
@Entity
@XmlType
@Table(name="BATCH")
//@NamedQueries({
//        @NamedQuery(
//                name="getPartiaJednostka",
//                query = "SELECT DISTINCT p.unit FROM Batch p WHERE p.unit LIKE CONCAT(CONCAT('%', :unit), '%')"
//        ),
//        @NamedQuery(
//                name = "getPartiaSpecyfikator",
//                query = "SELECT DISTINCT p.specifier FROM Batch p WHERE LOWER(p.specifier) LIKE LOWER(CONCAT('%',CONCAT(:specifier,'%')))"
//        ),
//        @NamedQuery(
//                name = "getBatches",
//                query = "SELECT new cx.ath.jbzdak.zarlok.entities.SameBathes(p.product, p.specifier, p.unit, AVG(p.price*p.currentQty)/SUM(p.currentQty), SUM(p.currentQty))  FROM Batch p GROUP BY p.product, p.specifier, p.unit"
//        ),
//        @NamedQuery(
//                name = "filterPartie",
//                query = "SELECT p FROM Batch p"
//        ),
//        @NamedQuery(
//                name = "getPartieByProdukt",
//                query = "SELECT new cx.ath.jbzdak.zarlok.entities.SameBathes(p.product, p.specifier, p.unit, AVG(p.price), SUM(p.currentQty))" +
//                        " FROM Batch p WHERE p.product = :product GROUP BY p.product, p.specifier, p.unit"
//        ),
//        @NamedQuery(
//                name = "getParieForWydanie",
//                query = "SELECT p FROM Batch p " +
//                        "WHERE ((:name IS NULL) OR  LOWER(p.product.name) LIKE LOWER('%' || :name || '%')) AND" +
//                        "((:specifier IS NULL) OR LOWER(p.specifier) LIKE LOWER('%' || :specifier || '%')) AND " +
//                        "((:unit IS NULL) OR LOWER(p.unit) LIKE LOWER('%' || :unit || '%'))"
//        ),
//        @NamedQuery(
//                name = "getPartieDoWydaniaNaPosilek",
//                query = "SELECT p FROM Batch p " +
//                        "WHERE " +
//                        "(:name IS NULL OR p.product.name = :name) AND " +
//                        "(:specifier IS NULL OR p.specifier = :specifier ) AND " +
//                        "(:unit IS NULL OR p.unit = :unit) AND " +
//                        "(p.expiryDate IS NULL OR p.expiryDate > :dzien) AND " +
//                        "p.bookingDate <= :dzien AND " +
//                        "p.currentQty > 0 " +
//                        "ORDER BY p.expiryDate ASC, " +
//                        "p.bookingDate DESC, " +
//                        "p.currentQty ASC"
//        )
//})
@NamedQueries({
        @NamedQuery(
                name = "getSpecifiers",
                query = "SELECT DISTINCT b.specifier FROM Batch b"
        ),
        @NamedQuery(
                name = "getUnits",
                query = "SELECT DISTINCT b.unit FROM Batch b"
        )
})
@EntityListeners({PartiaSearchCacheUpdater.class})
public class Batch implements IProductSearchCache {

   @XmlTransient @Transient
   private final PropertyChangeSupport support = new PropertyChangeSupport(this);

   @Id
   @GeneratedValue
   @Column(name = "ID")
   private Long id;

   @ManyToOne(fetch= FetchType.EAGER, optional=false)
   @JoinColumn(name = "PRODUCT_ID")
   private Product product;

   /**
    * Coś co odróżnia partie produktu. Na przylkład w
    * szynce lukusowej luksusowa to specifier
    */
   @Nonnull
   @NotEmpty
   @Column(name="SPECIFIER")
   private String specifier;

   @Column(name="PRICE", precision=16, scale=6)
   @Nonnull
   @NotNull
   private BigDecimal price;

   @Column(name="START_QTY", precision=12, scale=2)
   @Nonnull
   @NotNull
   private BigDecimal startQty;

   /**
    * Jednostka produktu.
    */
   @Column(name = "UNIT")
   @Nonnull
   @NotNull
   private String unit;

   @Column(name = "CURRENT_QTY", precision=12, scale=2)
   private BigDecimal currentQty = BigDecimal.ZERO;

   @Temporal(TemporalType.DATE)
   @Column(name="BOOKING_DATE")
   @Nonnull
   @NotNull
   private Date bookingDate;

   @Temporal(TemporalType.DATE)
   @Column(name="EXPIRY_DATE")
   @Nullable
   private Date expiryDate;

   /**
    * Moment zapisania encji w bazie danych.
    */
   @Temporal(TemporalType.DATE)
   @Column(name="CREATE_DATE")
   private Date createDate;

   /**
    * Losowy ciąg znaków wprowadzany, lub nie przez użyszkodnika
    */
   @Length(min=0, max=1000)
   @Column(name="DESCRIPTION")
   @Nullable
   private String description;

   @Length(min=1, max=100)
   @Column(name = "FAKTURA_NO")
   @Nonnull
   @NotNull
   private String fakturaNo;

   @OneToMany(mappedBy="batch")
   private List<Expenditure> expenditures = new ArrayList<Expenditure>();

   @Column(name = "LINE_NO")
   @Nullable private Integer lineNo;

   public Batch() {
      super();
   }

   public Batch(Product product, ProductSearchCache searchCache) {
      super();
      this.product = product;
      setSpecifier(searchCache.specifier);
      setUnit(searchCache.getUnit());
      if(product.getExpiryDate() == -1){
         setExpiryDate(null);
      }else{
         setExpiryDate(getRelativeDate(product.getExpiryDate()));
      }
   }

   public void recalculateCurrentQty(){
      setCurrentQty(BatchUtils.getIloscTeraz(this));
   }

   @Transient
   public String getFullName(){
      return product.getName() + " " + getSpecifier();
   }

   @Transient
   public String getBasicData(){
      return product.getName() + " " + getSpecifier() + " " + getCurrentQty() + " " + getUnit();
   }

   @XmlID
   @XmlJavaTypeAdapter(BatchAdapter.class)
   public Long getId() {
      return id;
   }


   public void setId(Long id) {
      this.id = id;
   }

   @XmlIDREF
   public Product getProduct() {
      return product;
   }

   public void setProduct(Product product) {
      this.product = product;
   }

   @XmlAttribute(required = true)
   @Override
   public String getSpecifier() {
      return specifier;
   }

   public void setSpecifier(String specifier) {
      this.specifier = specifier;
   }

   @XmlAttribute
   public BigDecimal getPrice() {
      return price;
   }

   public void setPrice(BigDecimal price) {
      BigDecimal oldPrice = this.price;
      this.price = price;
      support.firePropertyChange("price", oldPrice, this.price);
   }

   @XmlAttribute(required = true)
   public BigDecimal getStartQty() {
      return startQty;
   }

   public void setStartQty(BigDecimal startQty) {
      BigDecimal oldStartQty = this.startQty;
      this.startQty = startQty;
      support.firePropertyChange("startQty", oldStartQty, this.startQty);
   }

   @XmlAttribute(required = true)
   public Date getBookingDate() {
      return bookingDate;
   }

   public void setBookingDate(Date bookingDate) {
      this.bookingDate = bookingDate;
   }

   @XmlAttribute(required = true)
   public Date getExpiryDate() {
      return expiryDate;
   }

   public void setExpiryDate(Date expiryDate) {
      Date oldExpiryDate = this.expiryDate;
      this.expiryDate = expiryDate;
      support.firePropertyChange("expiryDate", oldExpiryDate, this.expiryDate);
   }

   @XmlAttribute(required = true)
   public Date getCreateDate() {
      return createDate;
   }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
   }

   @XmlElement
   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   @Override
   @XmlAttribute
   public String getUnit() {
      return unit;
   }

   public void setUnit(String unit) {
      this.unit = unit;
   }

   @XmlTransient
   public BigDecimal getCurrentQty() {
      return currentQty;
   }

   @XmlAttribute
   public String getFakturaNo() {
      return fakturaNo;
   }

   public void setFakturaNo(String fakturaNo) {
      this.fakturaNo = fakturaNo;
   }

   public List<Expenditure> getExpenditures() {
      return expenditures;
   }

   public Integer getLineNo() {
      return lineNo;
   }

   public void setExpenditures(List<Expenditure> expenditures) {
      this.expenditures = expenditures;
   }

   public void setLineNo(Integer lineNo) {
      this.lineNo = lineNo;
   }

   @SuppressWarnings({"WeakerAccess"})
   public void setCurrentQty(BigDecimal currentQty) {
      this.currentQty = currentQty;
   }

   @Transient
   public String getSearchFormat(){
      return ProductSearchCacheUtils.format(this);
   }

   @Override
   public String getProductName() {
      return getProduct().getName();
   }

   public void addPropertyChangeListener(PropertyChangeListener listener) {
      support.addPropertyChangeListener(listener);
   }

   public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
      support.addPropertyChangeListener(propertyName, listener);
   }

   public boolean hasListeners(String propertyName) {
      return support.hasListeners(propertyName);
   }

   public void removePropertyChangeListener(PropertyChangeListener listener) {
      support.removePropertyChangeListener(listener);
   }

   public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
      support.removePropertyChangeListener(propertyName, listener);
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder();
      sb.append("Batch");
      sb.append("{id=").append(id);
      sb.append(", product=").append(product);
      sb.append(", specifier='").append(specifier).append('\'');
      sb.append(", price=").append(price);
      sb.append(", startQty=").append(startQty);
      sb.append(", unit='").append(unit).append('\'');
      sb.append(", currentQty=").append(currentQty);
      sb.append(", bookingDate=").append(bookingDate);
      sb.append(", expiryDate=").append(expiryDate);
      sb.append(", createDate=").append(createDate);
      try{
         sb.append(", expenditures=").append(getExpenditures());
      }catch (RuntimeException e){
         sb.append(", expenditures=NIE ZAŁADOWANO");
         //Hibernate exception moze polecieć -- olać!
      }
      sb.append(", description='").append(description).append('\'');
      sb.append(", fakturaNo='").append(fakturaNo).append('\'');
      sb.append(", lineNo=").append(lineNo);
      sb.append('}');
      return sb.toString();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Batch)) return false;

      Batch batch = (Batch) o;

      if (price != null ? !price.equals(batch.price) : batch.price != null) return false;
      if (bookingDate != null ? !bookingDate.equals(batch.bookingDate) : batch.bookingDate != null)
         return false;
      if (expiryDate != null ? !expiryDate.equals(batch.expiryDate) : batch.expiryDate != null) return false;
      if (createDate != null ? !createDate.equals(batch.createDate) : batch.createDate != null)
         return false;
      if (id != null ? !id.equals(batch.id) : batch.id != null) return false;
      if (startQty != null ? !startQty.equals(batch.startQty) : batch.startQty != null) return false;
      if (currentQty != null ? !currentQty.equals(batch.currentQty) : batch.currentQty != null) return false;
      if (unit != null ? !unit.equals(batch.unit) : batch.unit != null) return false;
      if (fakturaNo != null ? !fakturaNo.equals(batch.fakturaNo) : batch.fakturaNo != null) return false;
      if (lineNo != null ? !lineNo.equals(batch.lineNo) : batch.lineNo != null) return false;
      if (description != null ? !description.equals(batch.description) : batch.description != null) return false;
      if (product != null ? !product.equals(batch.product) : batch.product != null) return false;
      if (specifier != null ? !specifier.equals(batch.specifier) : batch.specifier != null) return false;

      return true;
   }

   @Override
   public int hashCode() {
      int result = id != null ? id.hashCode() : 0;
      result = 31 * result + (product != null ? product.hashCode() : 0);
      result = 31 * result + (specifier != null ? specifier.hashCode() : 0);
      result = 31 * result + (price != null ? price.hashCode() : 0);
      result = 31 * result + (startQty != null ? startQty.hashCode() : 0);
      result = 31 * result + (unit != null ? unit.hashCode() : 0);
      result = 31 * result + (currentQty != null ? currentQty.hashCode() : 0);
      result = 31 * result + (bookingDate != null ? bookingDate.hashCode() : 0);
      result = 31 * result + (expiryDate != null ? expiryDate.hashCode() : 0);
      result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
      result = 31 * result + (description != null ? description.hashCode() : 0);
      result = 31 * result + (fakturaNo != null ? fakturaNo.hashCode() : 0);
      result = 31 * result + (lineNo != null ? lineNo.hashCode() : 0);
      return result;
   }
}
