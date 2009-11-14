package cx.ath.jbzdak.zarlok.entities;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-14
 */
@Entity
@Table(name = "UNIT_CONVERTER")
public class UnitConverter {

   @Id
   @GeneratedValue
   Long id;

   @Column(precision = 12, scale = 6)
   BigDecimal scale;

   String unitFrom;

   String unitTo;

   @Enumerated(EnumType.STRING)
   @Column(name = "CONVERTER_TYPE")
   UnitConverterType converterType;

   @Column(name = "REFLEXIVITY_DEPTH")
   Integer reflexivityDepth;

   @Transient
   UnitConverterState  state = UnitConverterState.NEW;

   @PostLoad @PostPersist
   public void postLoad(){
      state = UnitConverterState.PERSISTED;
   }

   public UnitConverterType getConverterType() {
      return converterType;
   }

   public void setConverterType(UnitConverterType converterType) {
      this.converterType = converterType;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Integer getReflexivityDepth() {
      return reflexivityDepth;
   }

   public void setReflexivityDepth(Integer reflexivityDepth) {
      this.reflexivityDepth = reflexivityDepth;
   }

   public BigDecimal getScale() {
      return scale;
   }

   public void setScale(BigDecimal scale) {
      this.scale = scale;
   }

   public UnitConverterState getState() {
      return state;
   }

   public void setState(UnitConverterState state) {
      this.state = state;
   }

   public String getUnitFrom() {
      return unitFrom;
   }

   public void setUnitFrom(String unitFrom) {
      this.unitFrom = unitFrom;
   }

   public String getUnitTo() {
      return unitTo;
   }

   public void setUnitTo(String unitTo) {
      this.unitTo = unitTo;
   }
}
