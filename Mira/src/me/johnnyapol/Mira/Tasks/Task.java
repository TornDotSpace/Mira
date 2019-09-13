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

import me.johnnyapol.Mira.System.WrappedProcess;

public abstract class Task {
	
	private WrappedProcess process;

	public Task(WrappedProcess process) {
		this.process = process;
	}
	
	/**
	 * @return The name of the Task
	 */
	public abstract String getName();

	/**
	 * Called when the TaskManager executes the task. The core logic of should be
	 * executed here Note: This method will block the rest of the queue unless the
	 * task is scheduled as a background task
	 * 
	 * @param process The process the task is running on
	 * @throws Throwable Any exception thrown by an executing task will be caught
	 *                   and logged to avoid crashing the Thread managing the
	 *                   TaskManager queue
	 */
	public abstract void execute() throws Throwable;

	/**
	 * A repeated task will be automatically reinserted into the schedule queue and
	 * run again after a certain delay
	 * 
	 * @return Whether or not this task is a repeated task
	 */
	public abstract boolean isRepeatedTask();

	/**
	 * @return The delay in milliseconds of when the task should be scheduled to run
	 *         again (if the task is a repeated task).
	 */
	public abstract long getRepeatInterval();
	
	public WrappedProcess getProcess() {
		return this.process;
	}
}
