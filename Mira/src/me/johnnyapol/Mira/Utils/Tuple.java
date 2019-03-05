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

public class Tuple<T extends Object, S extends Object> {
	private T first;
	private S second;
	
	public Tuple(T _first, S _second) {
		this.first = _first;
		this.second = _second;
	}
	
	public T getFirst() {
		return this.first;
	}
	
	public S getSecond() {
		return this.second;
	}
	
	public void set(T _first, S _second) {
		this.first = _first;
		this.second = _second;
	}
}
