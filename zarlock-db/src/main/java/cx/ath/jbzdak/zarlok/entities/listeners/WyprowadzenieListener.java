package cx.ath.jbzdak.zarlok.entities.listeners;

import cx.ath.jbzdak.zarlok.entities.Expenditure;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

@Deprecated
public class WyprowadzenieListener {

	@PrePersist @PreUpdate @PreRemove
	public void setupPartia(Expenditure wyprowadzenie){
		//wyprowadzenie.getPartia().recalculateIloscTeraz();
	}
}
