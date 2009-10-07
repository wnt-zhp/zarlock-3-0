package cx.ath.jbzdak.zarlok.importer;

import cx.ath.jbzdak.zarlok.entities.Partia;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class ZZProduct {

	@Id
	Long id;

	String name;

	@Temporal(TemporalType.DATE)
	Date bookingDate;

	@Temporal(TemporalType.DATE)
	Date expiryDate;

	String packaging;

	BigDecimal price;

	BigDecimal sQuantity = BigDecimal.ZERO;

	String facturaNo;

	String description;

	String unit;

	@OneToMany(mappedBy="product")
	List<ZZExpenditure> expenditures = new ArrayList<ZZExpenditure>();

	@OneToOne(optional=true)
	Partia partia;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public String getPackaging() {
		return packaging;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getSQuantity() {
		return sQuantity;
	}

	public String getFacturaNo() {
		return facturaNo;
	}

	public String getDescription() {
		return description;
	}

	public List<ZZExpenditure> getExpenditures() {
		return expenditures;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setSQuantity(BigDecimal quantity) {
		sQuantity = quantity;
	}

	public void setFacturaNo(String facturaNo) {
		this.facturaNo = facturaNo;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setExpenditures(List<ZZExpenditure> expenditures) {
		this.expenditures = expenditures;
	}

	public Partia getPartia() {
		return partia;
	}

	public void setPartia(Partia partia) {
		this.partia = partia;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
