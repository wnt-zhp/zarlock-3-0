package test;

import cx.ath.jbzdak.zarlok.db.ZarlockDBManager;

import java.util.HashMap;
import java.util.Map;

public class MemManager extends ZarlockDBManager {

	public MemManager(){
		//DBSetup setup = new DBSetup();
		Map<String,String> map = new HashMap<String, String>();
		map.put("hibernate.connection.url", "jdbc:hsqldb:mem");
		setPropertiesOverride(map);
	}


}
