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
 * It's like a wrapper around a Christmas present 
 * but there's a system process inside!
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
	
	public WrappedProcess(ProcessBuilder _builder, int _id, ProcessManager _manager) {
		this.builder = _builder;
		this.id = _id;
		this.manager = _manager;
	}
	
	protected void start() throws IOException {
		if (this.is_running) throw new IllegalStateException("Process is already running!");
		this.is_running = true;
		
		// Temporary until we get a better way of managing IO
		builder.inheritIO();
		process = builder.start();
	}
	
	protected void restart() throws IOException {
		if (this.is_running) {
			this.stop();
		}
		
		this.start();
	}
	
	protected void stop() {
		if (!this.is_running) throw new IllegalStateException("Process is not running!");
		this.is_running = false;
		this.process.destroy();
	}
	
	public InputStream getInputStream() {
		if (!this.is_running) throw new IllegalStateException("Process is not running!");
		return this.process.getInputStream();
	}
	
	public OutputStream getOutputStream() {
		if (!this.is_running) throw new IllegalStateException("Process is not running!");
		return this.process.getOutputStream();
	}
	
	public boolean isRunning() {
		// Check process stats
		if (this.is_running) {
			this.is_running = this.process.isAlive();
			return this.process.isAlive();
		}
		
		return false;
	}
	
	public int getId() {
		return this.id;
	}
	
	public ProcessManager getProcessManager() {
		return this.manager;
	}
}