package cx.ath.jbzdak.zarlok.ui.posilek;

import cx.ath.jbzdak.zarlok.entities.Posilek;

public class PosilekPanelCache {
	
	public static final PosilekPanel getPosilekPanel(Posilek p){
		return new PosilekPanel();
	}

}
