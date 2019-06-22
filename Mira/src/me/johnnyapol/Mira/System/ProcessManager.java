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
package me.johnnyapol.Mira.System;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import me.johnnyapol.Mira.Utils.Tuple;

public class ProcessManager {

	private Map<Integer, WrappedProcess> processes = new HashMap<Integer, WrappedProcess>();

	private int process_counter = 0;
	private static final Logger logger = Logger.getLogger("Mira");

	public ProcessManager() {
	}

	public WrappedProcess getProcess(int id) {
		if (!this.processes.containsKey(id)) {
			throw new IllegalArgumentException("Process " + id + " not found!");
		}
		return this.processes.get(id);
	}

	public Tuple<Integer, WrappedProcess> createProcess(ProcessBuilder process_info) throws IOException {
		logger.info("[ProcessManager] Creating new process " + this.process_counter + ": " + process_info.toString());

		WrappedProcess process = new WrappedProcess(process_info, this.process_counter, this);
		process.start();
		this.processes.put(this.process_counter, process);
		this.process_counter++;
		return new Tuple<Integer, WrappedProcess>(this.process_counter - 1, process);
	}

	public void stopProcess(int id) {
		if (!this.processes.containsKey(id)) {
			throw new IllegalArgumentException("Process " + id + " was not found!");
		}

		logger.info("[ProcessManager] Stopping process " + id);
		this.processes.get(id).stop();
	}

	public void startProcess(int id) throws IOException {
		if (!this.processes.containsKey(id)) {
			throw new IllegalArgumentException("Process " + id + " was not found!");
		}

		logger.info("[ProcessManager] Starting process " + id);
		this.processes.get(id).start();
	}

	public void restartProcess(int id) throws IOException {
		if (!this.processes.containsKey(id)) {
			throw new IllegalArgumentException("Process " + id + " was not found!");
		}

		logger.info("[ProcessManager] Stopping process " + id);
		this.processes.get(id).restart();
	}

	public void destroyProcess(int id) {
		if (!this.processes.containsKey(id)) {
			throw new IllegalArgumentException("Process " + id + " was not found!");
		}

		logger.info("[ProcessManager] Destroying process " + id);
		WrappedProcess process = this.processes.get(id);
		if (process.isRunning()) {
			process.stop();
		}

		this.processes.remove(id);
	}
}
