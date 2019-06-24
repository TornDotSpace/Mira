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
package me.johnnyapol.Mira;

import java.util.logging.Logger;

import me.johnnyapol.Mira.System.ProcessManager;
import me.johnnyapol.Mira.System.WrappedProcess;
import me.johnnyapol.Mira.Tasks.ProcessKeepAliveTask;
import me.johnnyapol.Mira.Tasks.TaskManager;
import me.johnnyapol.Mira.Utils.GitUtils;
import me.johnnyapol.Mira.Utils.Tuple;

public class Main {

	private static Logger logger = Logger.getLogger("Mira");

	public static void main(String[] args) throws Exception {
		logger.info("--- Starting Mira version git-" + GitUtils.getBuildCommit() + "-" + GitUtils.getBranch() + "-"
				+ GitUtils.getBuildVersion() + "-" + GitUtils.getBuildTime() + " ---");
		TaskManager taskMgr = new TaskManager();
		ProcessManager processMgr = new ProcessManager();

		ProcessBuilder builder = new ProcessBuilder();
		builder.command("node", "app.js", "8887");

		ProcessBuilder nginx = new ProcessBuilder();
		nginx.command("nginx", "-g", "daemon off;");

		Tuple<Integer, WrappedProcess> test = processMgr.createProcess(builder);
		Tuple<Integer, WrappedProcess> nginx_tuple = processMgr.createProcess(nginx);

		ProcessKeepAliveTask task = new ProcessKeepAliveTask();

		taskMgr.scheduleTask(task, test.getSecond());
		taskMgr.scheduleTask(task, nginx_tuple.getSecond());

		while (true) {
			Thread.sleep(5000);
			taskMgr.executeTasks();
		}
	}
}
