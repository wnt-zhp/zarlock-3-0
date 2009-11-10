package cx.ath.jbzdak.zarlok.entities;

import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class PeopleNo implements Cloneable{

	@NotNull
	//@Range(min=0)
	@Column(name = "UCZESTNICY_NO")
	private Integer uczestnicyNo;

	@NotNull
	//@Range(min=0)
	@Column(name = "KADRA_NO")
	private Integer kadraNo;

	@NotNull
	//@Range(min=0)
	@Column(name = "INNI_NO")
	private Integer inniNo;

   public PeopleNo() {
		super();
	}

	public PeopleNo(Integer uczestnicyNo, Integer kadraNo,
			Integer inniNo) {
		super();
		this.uczestnicyNo = uczestnicyNo;
		this.kadraNo = kadraNo;
		this.inniNo = inniNo;
	}

   public Integer getUczestnicyNo() {
		return uczestnicyNo;
	}

	public Integer getKadraNo() {
		return kadraNo;
	}

	public Integer getInniNo() {
		return inniNo;
	}

	public void setUczestnicyNo(Integer uczestnicyNo) {
		this.uczestnicyNo = uczestnicyNo;
	}

	public void setKadraNo(Integer kadraNo) {
		this.kadraNo = kadraNo;
	}

	public void setInniNo(Integer inniNo) {
		this.inniNo = inniNo;
	}

	@Transient
	public Integer getSum(){
		int result = 0;
		if(getUczestnicyNo()==null || getKadraNo()== null || getInniNo()==null){
			return null;
		}
		result += getUczestnicyNo();
		result += getInniNo();
		result += getKadraNo();
		return result;
	}

   @Override
   public final PeopleNo clone() {
      try {
         return (PeopleNo) super.clone();
      } catch (CloneNotSupportedException e) {
         throw  new RuntimeException(e);
      }
   }
}
