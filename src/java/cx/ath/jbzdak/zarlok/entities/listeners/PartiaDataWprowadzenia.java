package cx.ath.jbzdak.zarlok.entities.listeners;

import cx.ath.jbzdak.zarlok.entities.Partia;
import javax.persistence.PrePersist;

import java.util.Date;

/**
 * Ustawia pole {@link Partia#dataWprowadzenia} przy zapisie do bazy danych.
 * @author jb
 *
 */
public class PartiaDataWprowadzenia {

	@PrePersist
	public void setDataWprowadzenia(Partia partia){
		partia.setDataWprowadzenia(new Date());
	}

}
