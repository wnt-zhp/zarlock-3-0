package cx.ath.jbzdak.zarlok.main.initTasks;

import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.main.MainWindowModel;

/**
 * Dodaje do JVM shutdown hooka zamykającego aplikację. 
 * @author jb
 *
 */
public class AddShutdownHook extends Task<MainWindowModel> {

	public AddShutdownHook() {
		super(0, "Add shutdown hook");
	}

	@Override
	public void doTask(final MainWindowModel t, Object... o) throws Exception {
		Runtime.getRuntime().addShutdownHook(new Thread(){

			@Override
			public void run() {
				try {
					t.stopApp();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			
		});
		
	}

}
