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

import java.util.logging.Logger;

import me.johnnyapol.Mira.System.WrappedProcess;

public class ProcessKeepAliveTask implements Task {
	private final static Logger logger = Logger.getLogger("Mira");

	@Override
	public String getName() {
		return "KeepAlive";
	}

	@Override
	public void execute(WrappedProcess process) throws Throwable {
		if (!process.isRunning()) {
			logger.warning("[KeepAlive] Restarting crashed process: " + process.getId());
			process.getProcessManager().startProcess(process.getId());
		}
	}

	@Override
	public boolean isRepeatedTask() {
		return true;
	}

	@Override
	public long getRepeatInterval() {
		return 1000 * 5;
	}

}
