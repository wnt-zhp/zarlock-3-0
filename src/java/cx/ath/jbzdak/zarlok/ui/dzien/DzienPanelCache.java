package cx.ath.jbzdak.zarlok.ui.dzien;

import cx.ath.jbzdak.zarlok.entities.Dzien;

public class DzienPanelCache {
	
	public static final DzienPanel getDzienPanel(Dzien d){
		return new DzienPanel();
	}

}
