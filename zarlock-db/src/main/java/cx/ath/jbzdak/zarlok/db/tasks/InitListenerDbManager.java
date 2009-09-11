package cx.ath.jbzdak.zarlok.db.tasks;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.db.ZarlockDBManager;

public class InitListenerDbManager extends Task<ZarlockDBManager>{

	private final Class<?> clazz;
	

	public InitListenerDbManager(Class<?> clazz) {
		super(0, "Initializing ZarlockDBManager for listener: '" + clazz.getSimpleName() + "'");
		this.clazz = clazz;
	}


	@Override
	public void doTask(ZarlockDBManager t, Object... o) throws Exception {
		clazz.getMethod("setManager", DBManager.class).invoke(null, t);		
	}

}
