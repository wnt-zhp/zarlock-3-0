package cx.ath.jbzdak.zarlok.entities.listeners;

import cx.ath.jbzdak.zarlok.entities.Wyprowadzenie;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

@Deprecated
public class WyprowadzenieListener {

	@PrePersist @PreUpdate @PreRemove
	public void setupPartia(Wyprowadzenie wyprowadzenie){
		//wyprowadzenie.getPartia().recalculateIloscTeraz();
	}
}
