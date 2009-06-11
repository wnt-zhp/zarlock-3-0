package cx.ath.jbzdak.zarlok.entities.listeners;

import cx.ath.jbzdak.zarlok.entities.Partia;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class PartiaIloscTerazUpdater {

	@PrePersist @PreUpdate @PreRemove
	public void recaclulateIloscTeraz(Partia partia){
		if(partia.getIloscTeraz()==null){
			//partia.recalculateIloscTeraz();
		}
	}
}
