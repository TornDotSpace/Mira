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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public class Properties {
	protected Map<String, String> properties_list = new HashMap<String, String>();

	protected static final Logger logger = Logger.getLogger("Mira");

	private void init(InputStreamReader reader) throws IOException {
		BufferedReader _fileReader = new BufferedReader(reader);

		String line = null;
		while ((line = _fileReader.readLine()) != null) {
			// Skip lines beginning with a '#' (comments)
			if (line.charAt(0) == '#') {
				continue;
			}

			if (!line.contains("=")) {
				logger.warning("[Properties] Invalid line '" + line);
				continue;
			}

			String[] split = line.split("=");
			if (split.length != 2) {
				logger.warning("[Properties] Invalid line '" + line);
				continue;
			}

			properties_list.put(split[0], split[1]);
		}

		// Cleanup
		_fileReader.close();
	}

	public void write(File file) throws IOException {
		BufferedWriter _fileWriter = new BufferedWriter(new FileWriter(file));

		String line = null;
		for (Iterator<String> itr = this.properties_list.keySet().iterator(); itr.hasNext(); line = itr.next()) {
			_fileWriter.write(line + System.lineSeparator());
		}
		// Cleanup
		_fileWriter.close();
	}

	public Properties(File file) throws IOException {
		this.init(new FileReader(file));
	}

	public Properties(InputStream io) throws IOException {
		this.init(new InputStreamReader(io));
	}

	public Properties() {
		// Empty properties
	}

	public boolean contains(String key) {
		return this.properties_list.containsKey(key);
	}

	public String getString(String key, String fill) {
		if (!this.contains(key)) {
			this.properties_list.put(key, fill);
			return fill;
		}

		return this.properties_list.get(key);
	}

	public String getString(String key) {
		if (!this.contains(key)) {
			throw new IllegalArgumentException("Key not found: " + key);
		}

		return this.getString(key, "");
	}

	public Integer getInteger(String key, int fill) {
		if (!this.contains(key)) {
			this.properties_list.put(key, String.valueOf(fill));
			return fill;
		}
		return Integer.parseInt(this.properties_list.get(key));
	}

	public Integer getInteger(String key) {
		if (!this.contains(key)) {
			throw new IllegalArgumentException("Key not found: " + key);
		}

		return this.getInteger(key, 0);
	}

	public Boolean getBoolean(String key, boolean fill) {
		if (!this.contains(key)) {
			this.properties_list.put(key, String.valueOf(fill));
			return fill;
		}

		return Boolean.parseBoolean(this.properties_list.get(key));
	}

	public Boolean getBoolean(String key) {
		if (!this.contains(key)) {
			throw new IllegalArgumentException("Key not found: " + key);
		}
		return this.getBoolean(key, false);
	}

	public Float getFloat(String key, float fill) {
		if (!this.contains(key)) {
			this.properties_list.put(key, String.valueOf(fill));
			return fill;
		}

		return Float.parseFloat(this.properties_list.get(key));
	}

	public Float getFloat(String key) {
		if (!this.contains(key)) {
			throw new IllegalArgumentException("Key not found: " + key);
		}

		return this.getFloat(key, 0);
	}

	public Double getDouble(String key, double fill) {
		if (!this.contains(key)) {
			this.properties_list.put(key, String.valueOf(fill));
			return fill;
		}

		return Double.parseDouble(this.properties_list.get(key));
	}

	public Double getDouble(String key) {
		if (!this.contains(key)) {
			throw new IllegalArgumentException("Key not found: " + key);
		}

		return this.getDouble(key, 0);
	}

	public void setString(String key, String value) {
		this.properties_list.put(key, value);
	}

	public void setInteger(String key, int value) {
		this.properties_list.put(key, String.valueOf(value));
	}

	public void setBoolean(String key, boolean value) {
		this.properties_list.put(key, String.valueOf(value));
	}

	public void setFloat(String key, float value) {
		this.properties_list.put(key, String.valueOf(value));
	}

	public void setDouble(String key, double value) {
		this.properties_list.put(key, String.valueOf(value));
	}

}
