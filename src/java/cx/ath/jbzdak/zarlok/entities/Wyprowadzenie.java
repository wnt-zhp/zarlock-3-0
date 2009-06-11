package cx.ath.jbzdak.zarlok.entities;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.jpaGui.db.dao.annotations.LifecycleListener;
import cx.ath.jbzdak.jpaGui.db.dao.annotations.LifecyclePhase;
import javax.persistence.*;
import org.apache.commons.collections.functors.CloneTransformer;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.NotNull;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;

/**
 * Jedno wyprowadzenie z magazynu, przypisane do jednej partii.
 * @author jb
 *
 */
@Entity
@Table(name = "WYPROWADZENIE")
public class Wyprowadzenie implements Cloneable, ProductSeachCacheSearchable{

   private static final Logger LOGGER = Utils.makeLogger();

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@ManyToOne(optional=false, fetch = FetchType.EAGER)
	private Partia partia;

	@NotNull
	@Column(name="ILOSC_JEDNOSTEK")
	private BigDecimal iloscJednostek;

	@NotNull
	@Column(name="DATA_WYPROWADZENIA")
	private Date dataWyprowadzenia;

	@NotNull
	@Column(name="TYTULEM")
	private String tytulem;

	@NotNull
	@Column(name="DATA_UTWORZENIA")
	private Date dataUtworzenia;

   @SuppressWarnings({"WeakerAccess"})
   public Danie getDanie() {
      return danie;
   }

   public void setDanie(Danie danie) {
      this.danie = danie;
   }

   @ManyToOne(optional = true)
   private Danie danie;

	@PrePersist
	public void prePersist(){
		dataUtworzenia = new Date();
		recalculatePartia();
	}

	@SuppressWarnings({"WeakerAccess"})
   @PreUpdate @PreRemove
	public void recalculatePartia(){
        iloscJednostek = Utils.round(iloscJednostek, 2);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Partia getPartia() {
		return partia;
	}

	public void setPartia(Partia partia) {
		this.partia = partia;
//		if(this.partia != partia){
////			if(this.partia!=null){
////				this.partia.getPartie().remove(this);
////			}
//
////			if(partia!=null){
////				partia.getPartie().add(this);
////				partia.recalculateIloscTeraz();
////			}
//		}
	}

	public BigDecimal getIloscJednostek() {
		return iloscJednostek;
	}

	public void setIloscJednostek(BigDecimal iloscJednostek) {
		this.iloscJednostek = iloscJednostek;
	}

	public Date getDataWyprowadzenia() {
		return dataWyprowadzenia;
	}

	public void setDataWyprowadzenia(Date dataWyprowadzenia) {
		this.dataWyprowadzenia = dataWyprowadzenia;
	}

	public String getTytulem() {
		return tytulem;
	}

	public void setTytulem(String tytulem) {
		this.tytulem = tytulem;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Date getDataUtworzenia() {
		return dataUtworzenia;
	}

	public void setDataUtworzenia(Date dataUtworzenia) {
		this.dataUtworzenia = dataUtworzenia;
	}

	public Wyprowadzenie() {
		super();
	}

	public Wyprowadzenie(Partia partia) {
		super();
		this.partia = partia;
	}

    @Transient
    public BigDecimal getWartosc(){
        return Utils.round(getPartia().getCena().multiply(getIloscJednostek(), MathContext.DECIMAL32),2);
    }

	@Override
	public Wyprowadzenie clone()  {
		Wyprowadzenie result;
		try {
			result = (Wyprowadzenie) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		result.id = id;
		result.partia = partia;
		result.dataUtworzenia = (Date) CloneTransformer.INSTANCE.transform(dataUtworzenia);
		result.dataWyprowadzenia = (Date) CloneTransformer.INSTANCE.transform(dataWyprowadzenia);
		result.iloscJednostek = iloscJednostek;
		result.tytulem = tytulem;
		return result;
	}

    @Override
    public String getNazwaProduktu() {
        return getPartia().getNazwaProduktu();
    }

    public String getJednostka() {
        return getPartia().getJednostka();
    }

    public String getSpecyfikator() {
        return getPartia().getSpecyfikator();
    }

   @LifecycleListener({LifecyclePhase.PostRemove, LifecyclePhase.PostUpdate, LifecyclePhase.PostPersist})
   public void updateKosztDania(EntityManager entityManager){
      LOGGER.debug("updating koszt dania");
      if(getDanie()==null){
         return;
      }
      Query q = entityManager.createQuery("SELECT SUM(w.iloscJednostek) FROM Danie d, IN(d.wyprowadzenia) w WHERE " +
              "d = :danie ");
      q.setParameter("danie", getDanie());
      Query update = entityManager.createQuery("UPDATE Danie d SET d.koszt = :koszt WHERE d = :danie");
      update.setParameter("danie", getDanie());
      update.setParameter("koszt", q.getSingleResult());
      update.executeUpdate();
      LOGGER.info("Koszt dania updated");
   }

}