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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import me.johnnyapol.Mira.Utils.Properties;

public class ConfigFile {
	
	private List<Properties> process_configs = new LinkedList<Properties>();
	private Properties mira_config = new Properties();
	
	private final static Logger logger = Logger.getLogger("Mira");
	
	public ConfigFile(File config_folder) throws IOException {
		this.init(config_folder);
	}
	
	private void init(File cfg_folder) throws IOException {
		File cfg = new File(cfg_folder, "mira.cfg");
		BufferedReader fReader = new BufferedReader(new FileReader(cfg));
		
		String line = null;
		
		Properties current_process = null;
		while ((line = fReader.readLine()) != null) {
			line = line.trim();
			if (line.isEmpty()) continue;
			if (line.startsWith("#")) continue; // ignore comments
			
			if (line.startsWith("[")) {
				// Begin a new process 
				
				// End the current one (if it exists)
				if (current_process != null) {
					this.process_configs.add(current_process);
				}
				
				current_process = new Properties();
				// Format: [process_name]
				current_process.setString("mira.process_name", line.substring(line.indexOf('[') + 1, line.indexOf(']')));
				continue;
			}
			
			if (current_process != null) {
				String[] split = line.split("=");
				if (split.length != 2) {
					logger.severe("[Config] Invalid line: " + line);
					continue;
				}
				current_process.setString(split[0], split[1]);
			}
		}
		
		if (current_process != null) { 
			this.process_configs.add(current_process);
		}
		fReader.close();
	}	
	
	
	public Properties getMiraConfig() {
		return this.mira_config;
	}
	
	public List<Properties> getProcesses() {
		return this.process_configs;
	}
}
