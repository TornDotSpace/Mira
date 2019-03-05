/**
    This file is part of Mira.

    Mira is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Mira is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Mira.  If not, see <https://www.gnu.org/licenses/>.
 */
package me.johnnyapol.Mira.Tasks;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.johnnyapol.Mira.System.WrappedProcess;
import me.johnnyapol.Mira.Utils.Tuple;

final class RunnableTaskWrapper implements Task {
	
	private final Task task;
	private final TaskManager taskManager;
	
	private final static Logger logger = Logger.getLogger("Mira");
	
	public RunnableTaskWrapper(Task _task, TaskManager _tskMgr) {
		this.task = _task;
		this.taskManager = _tskMgr;
	}
	
	@Override
	public String getName() {
		return this.task.getName();
	}

	@Override
	public void execute(WrappedProcess process) throws Throwable {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					RunnableTaskWrapper.this.task.execute(process);
				} catch (Throwable e) {
					logger.log(Level.SEVERE, "[TaskManager] Throwable while executing backgrounded task " + RunnableTaskWrapper.this.task.getClass().getName() + " on process ID: " + process.getId(), e);
				}
				// Reschedule task if necessary
				if (RunnableTaskWrapper.this.task.isRepeatedTask()) {
					RunnableTaskWrapper.this.taskManager.scheduleDelayedBackgroundTask(RunnableTaskWrapper.this.task, process, RunnableTaskWrapper.this.task.getRepeatInterval());
				}
			}
		}, "Mira-Background-Task Thread").start();
	}

	@Override
	public boolean isRepeatedTask() {
		return false;
	}

	@Override
	public long getRepeatInterval() {
		return this.getRepeatInterval();
	}
};

public class TaskManager {
	// Holds current list of tasks that need to be processed on the next "tick" of the task manager
	private Queue<Tuple<Task,WrappedProcess>> task_queue = new LinkedList<Tuple<Task, WrappedProcess>>();
	
	private final static Logger logger = Logger.getLogger("Mira");
	
	// Used for our priority queue. Sorts the queue such that the task with the earlier timestamps percolate down the queue 
	private static Comparator<Tuple<Long, Tuple<Task, WrappedProcess>>> taskComparator = new Comparator<Tuple<Long, Tuple<Task, WrappedProcess>>>() {

		@Override
		public int compare(Tuple<Long, Tuple<Task, WrappedProcess>> o1, Tuple<Long, Tuple<Task, WrappedProcess>> o2) {
			if (o1.getFirst() < o2.getFirst()) {
				return -1;
			}
			if (o1.getFirst() == o2.getFirst()) {
				return 0;
			}
			return 1;
		}
	};
	
	// Holds scheduled tasks waiting to be put into the task queue, in sorted order by smallest timestamp
	private Queue<Tuple<Long, Tuple<Task,WrappedProcess>>> schedule_queue = new PriorityQueue<Tuple<Long, Tuple<Task, WrappedProcess>>>(1, taskComparator);
	
	/***
	 * The "heartbeat" call of the TaskManager class. 
	 * Executes all tasks waiting in the task queue and handles putting scheduled tasks into the queue when their time elapses
	 **/
	public void executeTasks() {
		// Handle scheduled tasks 
		while (!schedule_queue.isEmpty()) {
			if (schedule_queue.peek().getFirst() > System.currentTimeMillis()) {
				break;
			}
			
			task_queue.add(schedule_queue.poll().getSecond());
		}
		
		// Handle tasks to be processed
		while (!task_queue.isEmpty()) {
			// Grab the next task from the queue
			Tuple<Task, WrappedProcess> task = task_queue.poll();
			try {
				task.getFirst().execute(task.getSecond());
				// Handle re-inserting a repeated task into the schedule queue
				if (task.getFirst().isRepeatedTask()) {
					// Re-schedule the task
					long newTime = System.currentTimeMillis() + task.getFirst().getRepeatInterval();
					schedule_queue.add(new Tuple<Long, Tuple<Task, WrappedProcess>>(newTime, task));
				}
			} catch (Throwable e) {
				logger.log(Level.SEVERE, "[TaskManager] Throwable while executing task " + task.getFirst().getClass().getName() + " on process ID: " + task.getSecond().getId(), e);
			}
		}
	}
	
	/**
	 * Schedules a task for immediate run on next call of executeTasks() 
	 * @param task The task to be run
	 * @param process The process that the task will run on
	 */
	public void scheduleTask(Task task, WrappedProcess process) {
		this.task_queue.add(new Tuple<Task, WrappedProcess>(task, process));
	}
	
	/**
	 * Schedules a task to be run after a certain delay
	 * @param task The task to be run
	 * @param process The process that the task will run on
	 * @param delay The run delay of the task in milliseconds 
	 */
	public void scheduleDelayedTask(Task task, WrappedProcess process, long delay) {
		this.schedule_queue.add(new Tuple<Long, Tuple<Task, WrappedProcess>>(System.currentTimeMillis() + delay, new Tuple<Task, WrappedProcess>(task, process)));
	}
	
	/**
	 * Schedules a task for immediate run on the next call of executeTasks()
	 * This task will be started on its own Thread to avoid blocking the queue
	 * @param task The task to be run
	 * @param process The process that the task will run on
	 */
	public void scheduleBackgroundTask(Task task, WrappedProcess process) {
		this.scheduleTask(new RunnableTaskWrapper(task, this), process);
	}
	/**
	 * Schedules a task to be run after a certain delay 
	 * This task, when executed, will be started on its own Thread to avoid blocking the queue
	 * @param task The task to be run
	 * @param process The process that the task will run on 
	 * @param delay The run delay of the task in milliseconds
	 */
	public void scheduleDelayedBackgroundTask(Task task, WrappedProcess process, long delay) {
		this.scheduleDelayedTask(new RunnableTaskWrapper(task, this), process, delay);
	}
}
