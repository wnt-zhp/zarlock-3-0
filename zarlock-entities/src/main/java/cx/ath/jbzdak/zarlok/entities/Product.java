package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.zarlok.entities.xml.adapters.ProductAdapter;
import org.hibernate.validator.Length;
import org.hibernate.validator.Range;

import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(
                name="getProductIdByName",
                query = "SELECT p.id FROM Product p WHERE p.name = :name"
        ),
        @NamedQuery(
                name = "getProductNamesByName",
                query = "SELECT p.name FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', CONCAT(:name, '%')))"
        ),
        @NamedQuery(
                name = "getStockLevelsForProduct",
                query = "SELECT new cx.ath.jbzdak.zarlok.entities.misc.StockLevelBean(b.specifier, b.unit, SUM(b.currentQty)) FROM Product p, IN(p.batches) b where p.id = :id GROUP BY b.specifier, b.unit ORDER BY b.specifier, b.unit "
        ),
        @NamedQuery(
                name = "getDistinctSpecifiersAndUnits",
                query = "SELECT DISTINCT b.specifier, b.unit FROM Product p, IN(p.batches) b WHERE p.id = :id"
        ),
        @NamedQuery(
                name = "getProductByName",
                query = "SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', CONCAT(:name, '%')))"
        )
})
@Entity
@XmlType
@Table(name="PRODUCT")
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

   @XmlID
   @XmlJavaTypeAdapter(ProductAdapter.class)
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   @XmlAttribute(name = "name", required = true)
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @XmlAttribute(name = "unit", required = false)
   public String getUnit() {
      return unit;
   }

   public void setUnit(String unit) {
      this.unit = unit;
   }

   @XmlAttribute(name = "expiryDate", required = false)
   public Integer getExpiryDate() {
      return expiryDate;
   }

   public void setExpiryDate(Integer expiryDate) {
      this.expiryDate = expiryDate;
   }

   @XmlTransient
   public List<Batch> getBatches() {
      return batches;
   }

   public void setBatches(List<Batch> batches) {
      this.batches = batches;
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

}
