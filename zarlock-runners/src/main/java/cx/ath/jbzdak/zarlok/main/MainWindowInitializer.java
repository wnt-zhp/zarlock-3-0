package cx.ath.jbzdak.zarlok.main;

import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.jpaGui.task.TasksExecutor;
import cx.ath.jbzdak.zarlok.main.initTasks.*;

/**
 * Inicjalizuje {@link MainWindowModel}. Żeby dodać taska inicjalizajyjnego
 * trzeba dopisać odpowiednią linię w {@link #initPreDB()} lub {@link #initPostDB()}
 * @author jb
 *
 */
public class MainWindowInitializer {

	private final MainWindowModel model;

	private TasksExecutor<MainWindowModel> te;

	public static void initialize(MainWindowModel model) {
		MainWindowInitializer mwi = new MainWindowInitializer(model);
		mwi.initPreDB();
		mwi.initPostDB();
	}

	public MainWindowInitializer(MainWindowModel model) {
		super();
		this.model = model;
	}

	void initPreDB() {
		te = model.getPreDBStartupTasks();
		addTask(new SetupLF());
		addTask(new AddShutdownHook());
		addTask(new SetIconAliases());
      addTask(new GetDesktopSupport());
	}

	void initPostDB() {
		te = model.getPostDBStartupTasks();
		addTask(new SetupMainFrame());
      addTask(new Checkpoint());
	}

	void addTask(Task<? super MainWindowModel> task) {
		te.addTask(task);
	}
}
