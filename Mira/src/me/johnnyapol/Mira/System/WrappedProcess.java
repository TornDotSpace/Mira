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
import java.io.InputStream;
import java.io.OutputStream;

/**
 * It's like a wrapper around a Christmas present but there's a system process
 * inside!
 * 
 * @author john
 *
 */

public class WrappedProcess {
	private Process process = null;

	// Internal Data about the Process
	private ProcessBuilder builder;

	private boolean is_running = false;

	private int id = -1;
	private ProcessManager manager;

	/**
	 * Creates a new WrappedPorcess instance
	 * 
	 * @param _builder The Process information
	 * @param _id      The ID of the process
	 * @param _manager The process manager associated with this process
	 */
	protected WrappedProcess(ProcessBuilder _builder, int _id, ProcessManager _manager) {
		this.builder = _builder;
		this.id = _id;
		this.manager = _manager;
	}

	/**
	 * Starts the process
	 * 
	 * @throws IOException
	 */
	protected void start() throws IOException {
		if (this.is_running)
			throw new IllegalStateException("Process is already running!");
		this.is_running = true;

		// Temporary until we get a better way of managing IO
		builder.inheritIO();
		process = builder.start();
	}

	/**
	 * Restarts the process
	 * 
	 * @throws IOException
	 */
	protected void restart() throws IOException {
		if (this.is_running) {
			this.stop();
		}

		this.start();
	}

	/**
	 * Stops the process
	 */
	protected void stop() {
		if (!this.is_running)
			throw new IllegalStateException("Process is not running!");
		this.is_running = false;
		this.process.destroy();
	}

	/**
	 * @return The InputStream associated with the process
	 */
	public InputStream getInputStream() {
		if (!this.is_running)
			throw new IllegalStateException("Process is not running!");
		return this.process.getInputStream();
	}

	/**
	 * @return The OutputStream associated with the process
	 */
	public OutputStream getOutputStream() {
		if (!this.is_running)
			throw new IllegalStateException("Process is not running!");
		return this.process.getOutputStream();
	}

	/**
	 * @return True if the Process is currently alive and false if the process is
	 *         not running.
	 */
	public boolean isRunning() {
		// Check process stats
		if (this.is_running) {
			this.is_running = this.process.isAlive();
			return this.process.isAlive();
		}

		return false;
	}

	/**
	 * Returns the ID associated with this process
	 * 
	 * @return Process ID
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Returns the Process Manager associated with this process
	 * 
	 * @return The Process Manager
	 */
	public ProcessManager getProcessManager() {
		return this.manager;
	}
}