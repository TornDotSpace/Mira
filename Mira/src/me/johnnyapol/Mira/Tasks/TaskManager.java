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

public class TaskManager {
	private Queue<Tuple<Task,WrappedProcess>> task_queue = new LinkedList<Tuple<Task, WrappedProcess>>();
	
	private final static Logger logger = Logger.getLogger("Mira");
	
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
	
	private Queue<Tuple<Long, Tuple<Task,WrappedProcess>>> schedule_queue = new PriorityQueue<Tuple<Long, Tuple<Task, WrappedProcess>>>(0, taskComparator);
	
	public void executeTasks() {
		// Handle scheduled tasks 
		while (!schedule_queue.isEmpty()) {
			if (schedule_queue.peek().getFirst() > System.currentTimeMillis()) {
				break;
			}
			
			task_queue.add(schedule_queue.poll().getSecond());
		}
		
		while (!task_queue.isEmpty()) {
			Tuple<Task, WrappedProcess> task = task_queue.poll();
			try {
				task.getFirst().execute(task.getSecond());
				// Handle re-inserting a repeated task into the schedule queue
				if (task.getFirst().isRepeatedTask()) {
					long newTime = System.currentTimeMillis() + task.getFirst().getRepeatInterval();
					schedule_queue.add(new Tuple<Long, Tuple<Task, WrappedProcess>>(newTime, task));
				}
			} catch (Throwable e) {
				logger.log(Level.SEVERE, "[TaskManager] Throwable while executing task " + task.getFirst().getClass().getName() + " on process ID: " + task.getSecond().getId(), e);
			}
		}
	}
}
