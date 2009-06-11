package cx.ath.jbzdak.zarlok.entities.listeners;

import cx.ath.jbzdak.zarlok.entities.ConfigEntry;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Listener blokujący zmianę niemodyfikowalnej
 * {@link ConfigEntry}.
 * @author jb
 *
 */
public class BlockConfigEntryUpdate {

	private static final Logger LOGGER = LoggerFactory.getLogger(BlockConfigEntryUpdate.class);

   @PreUpdate @PreRemove
	public void blockUpdate(Object entry){
		if (entry instanceof ConfigEntry) {
			ConfigEntry ce = (ConfigEntry) entry;
			if(ce.getEditable()!=null && !ce.getEditable()){
				throw new UnsupportedOperationException();
			}
		}else{
			LOGGER.error("Object of wrong class was passed to BlockConfigEntryUpdate"
					+ " expected ConfigEntry got {}", entry.getClass());

		}
	}

}
