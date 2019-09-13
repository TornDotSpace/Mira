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

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.johnnyapol.Mira.System.ProcessManager;
import me.johnnyapol.Mira.System.WrappedProcess;
import me.johnnyapol.Mira.Tasks.Task;
import me.johnnyapol.Mira.Tasks.TaskManager;
import me.johnnyapol.Mira.Utils.GitUtils;
import me.johnnyapol.Mira.Utils.Properties;
import me.johnnyapol.Mira.Utils.Tuple;

public class Main {

	private static Logger logger = Logger.getLogger("Mira");
	
	public static Properties parseCmdLineArgs(String[] args) {
		Properties props = new Properties();
		for (int i = 1; i < args.length; ++i) {
			String[] split = args[i].split("=");
			if (split.length < 2) {
				logger.log(Level.SEVERE, "Core: Invalid argument: " + args[i]);
				continue;
			}
			props.setString(split[0], split[1]);
		}
		return props;
	}
	
	public static void main(String[] args) throws Exception {
		logger.info("--- Starting Mira version git-" + GitUtils.getBuildCommit() + "-" + GitUtils.getBranch() + "-"
				+ GitUtils.getBuildVersion() + "-" + GitUtils.getBuildTime() + " ---");
		
		Properties cmdLine = parseCmdLineArgs(args);
		
		TaskManager taskMgr = new TaskManager();
		ProcessManager processMgr = new ProcessManager();
		
		File log_folder = new File(cmdLine.getString("log_folder", "logs"));
		
		if (!log_folder.exists()) {
			logger.info("Core: Creating log directory at '" + log_folder.getAbsolutePath() + "'");
			if (!log_folder.mkdirs()) {
				logger.severe("Core: Failed to create directory '" + log_folder.getAbsolutePath() + "'. Aborting...");
				return;
			}
		}
		
		// Try to load configuration
		File config_folder = new File(cmdLine.getString("cfg_folder", "config"));
		
		if (!config_folder.exists()) {
			logger.info("Config: Creating configuration directory at '" + config_folder.getAbsolutePath() + "'");
			if (!config_folder.mkdirs()) {
				logger.severe("Config: Failed to create directory '" + config_folder.getAbsolutePath() + "'. Aborting...");
				return; 
			}
		}
		
		ConfigFile cfg = new ConfigFile(config_folder);
		
		List<Properties> processes = cfg.getProcesses();
		
		for (Properties proc : processes) {
			logger.info("[mira-start] Registering process : " + proc.getString("mira.process_name"));
			
			ProcessBuilder builder = new ProcessBuilder();
			
			String[] cmd = proc.getString("proc.exec").split(" ");
			builder = builder.command(cmd);
			
			Tuple<Integer, WrappedProcess> mira_proc = processMgr.createProcess(builder, proc);
			
			
			// Load tasks
			
			String[] tasks = proc.getString("proc.tasks").split(";");
			
			for (String task : tasks) {
				Class<? extends Task> clazz = (Class<? extends Task>) Class.forName(task);
				
				Task the_task = clazz.newInstance();
				taskMgr.scheduleTask(the_task, mira_proc.getSecond());
			}
		}
		
		while (true) {
			Thread.sleep(1000);
			taskMgr.executeTasks();
		}
	}
}
