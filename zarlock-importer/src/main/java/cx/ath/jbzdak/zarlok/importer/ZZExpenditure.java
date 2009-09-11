package cx.ath.jbzdak.zarlok.importer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class ZZExpenditure {

	@Id @GeneratedValue
	Long id;

	String tytulem;

	Date data;

	BigDecimal ilosc;

	@ManyToOne
	ZZProduct product;

	public Long getId() {
		return id;
	}

	public String getTytulem() {
		return tytulem;
	}

	public Date getData() {
		return data;
	}

	public ZZProduct getProduct() {
		return product;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTytulem(String tytulem) {
		this.tytulem = tytulem;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setProduct(ZZProduct product) {
		this.product = product;
	}

	public BigDecimal getIlosc() {
		return ilosc;
	}

	public void setIlosc(BigDecimal ilosc) {
		this.ilosc = ilosc;
	}

}
