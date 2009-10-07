package cx.ath.jbzdak.zarlok.ui.dzien;

import cx.ath.jbzdak.zarlok.entities.Dzien;

import java.io.Serializable;
import java.util.Comparator;

public class DzienDateComparator implements Comparator<Dzien>,Serializable{

	private static final long serialVersionUID = 1L;

	@Override
	public int compare(Dzien o1, Dzien o2) {
		return o1.getData().compareTo(o2.getData());
	}

}
