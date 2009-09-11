
package cx.ath.jbzdak.zarlok.importer;

import cx.ath.jbzdak.zarlok.entities.Posilek;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
public class ZZMeal {

	@Id @GeneratedValue
	Long id;

	String nazwa;

	@Temporal(TemporalType.DATE)
	Date data;

	@OneToOne(optional = true)
	Posilek posilek;

	@OneToMany
	List<ZZExpenditure> expenditures = new ArrayList<ZZExpenditure>();

	public Long getId() {
		return id;
	}

	public String getNazwa() {
		return nazwa;
	}

	public Date getData() {
		return data;
	}

	public List<ZZExpenditure> getExpenditures() {
		return expenditures;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setExpenditures(List<ZZExpenditure> expenditures) {
		this.expenditures = expenditures;
	}

	public Posilek getPosilek() {
		return posilek;
	}

	public void setPosilek(Posilek posilek) {
		this.posilek = posilek;
	}
}
