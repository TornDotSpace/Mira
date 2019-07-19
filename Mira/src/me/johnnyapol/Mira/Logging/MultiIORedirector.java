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
package me.johnnyapol.Mira.Logging;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class MultiIORedirector extends OutputStream{
	
	private List<OutputStream> streams; 
	
	public MultiIORedirector(List<OutputStream> _streams) {
		this.streams = _streams;

	
	}
	@Override
	public void write(int arg0) throws IOException {
		for (OutputStream stream : this.streams) {
			stream.write(arg0);
		}
	}
}
