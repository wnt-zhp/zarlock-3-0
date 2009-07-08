package cx.ath.jbzdak.zarlok.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import org.hibernate.validator.NotNull;

@Embeddable
public class IloscOsob implements Cloneable{


	@NotNull
	//@Range(min=0)
	@Column(name = "ILOSC_UCZESTNIKOW")
	private Integer iloscUczestnikow;

	@NotNull
	//@Range(min=0)
	@Column(name = "ILOSC_KADRY")
	private Integer iloscKadry;

	@NotNull
	//@Range(min=0)
	@Column(name = "ILOSC_INNYCH")
	private Integer iloscInnych;

	public IloscOsob() {
		super();
	}

	public IloscOsob(Integer iloscUczestnikow, Integer iloscKadry,
			Integer iloscInnych) {
		super();
		this.iloscUczestnikow = iloscUczestnikow;
		this.iloscKadry = iloscKadry;
		this.iloscInnych = iloscInnych;
	}

	public Integer getIloscUczestnikow() {
		return iloscUczestnikow;
	}

	public Integer getIloscKadry() {
		return iloscKadry;
	}

	public Integer getIloscInnych() {
		return iloscInnych;
	}

	public void setIloscUczestnikow(Integer iloscUczestnikow) {
		this.iloscUczestnikow = iloscUczestnikow;
	}

	public void setIloscKadry(Integer ilosckadry) {
		this.iloscKadry = ilosckadry;
	}

	public void setIloscInnych(Integer iloscInnych) {
		this.iloscInnych = iloscInnych;
	}

	@Transient
	public Integer getSuma(){
		int result = 0;
		if(getIloscUczestnikow()==null || getIloscKadry()== null || getIloscInnych()==null){
			return null;
		}
		result += getIloscUczestnikow();
		result += getIloscInnych();
		result += getIloscKadry();
		return result;
	}

   @Override
   public final IloscOsob clone() {
      try {
         return (IloscOsob) super.clone();
      } catch (CloneNotSupportedException e) {
         throw  new RuntimeException(e);
      }
   }
}
