package cx.ath.jbzdak.zarlok.ui.danie;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.zarlok.entities.Danie;

public class DaniaPanelCache {
	
	private static DBManager manager;

	public static final DaniePanel getDanuePanel(Danie d){
		return new DaniePanel(manager);
	}

	public static DBManager getManager() {
		return manager;
	}

	public static void setManager(DBManager manager) {
		DaniaPanelCache.manager = manager;
	}
}
