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
package me.johnnyapol.Mira.Utils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GitUtils {
	private static Logger logger = Logger.getLogger("Mira");

	private static Properties git_data = null;

	static {
		try {
			git_data = new JsonProperties(GitUtils.class.getClassLoader().getResourceAsStream("git.properties"));
		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Failed to load git.properties (not built with maven?). Build data will be unavailable", e);
			git_data = new Properties();
		}
	}

	public static String getBuildCommit() {
		return git_data.getString("git.commit.id", "null");
	}

	public static String getBranch() {
		return git_data.getString("git.branch", "nosource");
	}

	public static String getBuildVersion() {
		return git_data.getString("git.build.version", "nogit");
	}

	public static String getBuildTime() {
		return git_data.getString("git.build.time", "1969-12-31");
	}
}
