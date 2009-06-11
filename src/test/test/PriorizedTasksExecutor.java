package test;

import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.jpaGui.task.TasksExecutor;
import org.junit.Test;

public class PriorizedTasksExecutor {


	boolean task1Executed;

	@SuppressWarnings("unchecked")
	@Test
	public void testExecute() throws Exception {
		Task t1 = new Task(10, "Task 1"){
			@Override
			public void doTask(Object o, Object... o2) {
				task1Executed = true;
			}
		};
		Task t2 = new Task(0, "Task 2"){
			@Override
			public void doTask(Object o, Object... o2) {
				if(!task1Executed){
					throw new RuntimeException();
				}
			}
		};
		TasksExecutor executor = new TasksExecutor();
		executor.addTask(t1);
		executor.addTask(t2);
		executor.executeThrow(null);
		executor = new TasksExecutor();
		executor.addTask(t2);
		executor.addTask(t1);
		executor.executeThrow(null);
	}

}
