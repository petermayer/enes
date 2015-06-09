/*=========================================================================
 * EnEs is a little tool for calculating multiple different metrics to
 * analyze the distribution of password sets.
 * Copyright (C) 2013 Peter Mayer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *=========================================================================*/
package main.passwords;

import java.util.ArrayList;
import java.util.List;

/**
 * A little wrapper class for the click-points belonging to one password. It is
 * basically just an unexposed ArrayList with a fancy name.  
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
public class ClickPassword {

	/**
	 * Where all click-points are stored
	 */
	private ArrayList<int[]> coordinates=new ArrayList<int[]>();

	/**
	 * Adds a click-point to the password
	 * 
	 * @param x The x-coordinate of the click-point
	 * @param y The y-coordinate of the click-point
	 */
	public void addClickPoint(int x, int y) {
		this.coordinates.add(new int[]{x,y});
	}

	/**
	 * Adds a click-point to the password
	 * 
	 * @param cp The click-point in the form [x,y]
	 */
	public void addClickPoint(int[] cp) {
		this.coordinates.add(cp);
	}

	/**
	 * Retrieves a click-point from the password
	 * 
	 * @param index The index of the click-point in the password
	 * @return The click-point in the form [x,y]
	 */
	public int[] getClickPoint(int index) {
		return this.coordinates.get(index);
	}

	/**
	 * Retrieves the overall number of click-points in the password
	 * 
	 * @return The length of the password
	 */
	public int length() {
		return this.coordinates.size();
	}

	/**
	 * Calculates the maximum length of all passwords in an array
	 * 
	 * @param pwds The passwords
	 * @return The maximum length
	 */
	public static int getMaxLength(List<ClickPassword> pwds) {
		int maxLen=0;
		
		for (ClickPassword pwd : pwds) {
			maxLen = pwd.length()>maxLen ? pwd.length() : maxLen;
		}
		
		return maxLen;
	}

}