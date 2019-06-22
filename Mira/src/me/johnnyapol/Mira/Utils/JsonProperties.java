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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonProperties extends Properties {

	public JsonProperties(InputStream resourceAsStream) throws IOException {
		init(new InputStreamReader(resourceAsStream));
	}

	private void init(InputStreamReader reader) throws IOException {
		BufferedReader _fileReader = new BufferedReader(reader);

		String line = null;
		while ((line = _fileReader.readLine()) != null) {
			// Skip lines beginning with a '#' (comments)
			if (line.charAt(0) == '#' || line.charAt(0) == '{' || line.charAt(0) == '}') {
				continue;
			}

			String[] split = line.split(":");
			if (split.length < 2) {
				logger.warning("[Properties] Invalid line '" + line);
				continue;
			}

			String split1 = "";

			for (int i = 1; i < split.length; i++) {
				split1 += split[i];
			}

			if (split1.length() > 3) {
				split1 = split1.trim().substring(1, split1.length() - 3);
			}

			split[0] = split[0].trim();
			split[0] = split[0].substring(1, split[0].length() - 1);

			properties_list.put(split[0], split1);
		}

		// Cleanup
		_fileReader.close();
	}

}
