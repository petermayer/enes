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
 * This class serves as representation for cognometric graphical passwords
 * that are grouped. A typical authentication scheme utilizing such passwords
 * is Passfaces, where each grid represents one group. Each element in the
 * password is comprised of two numbers, the first indicating the group's id
 * and the second indicating the id in the group.   
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
public class CognometricGroupedPassword {

	/**
	 * Where all the password elements are stored
	 */
	private ArrayList<int[]> elements=new ArrayList<int[]>();
	
	/**
	 * Adds an element to this password
	 * 
	 * @param groupID The id of the group the password belongs to
	 * @param elementID The id of the element in the group
	 */
	public void addElement(int[] element) {
		this.elements.add(element);
	}
	
	/**
	 * Returns the indexth element of the password or null if index is
	 * out of bounds.
	 * 
	 * @param index The index of the desired element
	 * @return The respective element
	 */
	public int[] getElement(int index) {
		
		if ( this.elements.size()<index || index<0 ) return null;
		
		return this.elements.get(index);
	}
	
	/**
	 * Returns the length of this password (i.e. number of grouped elements)
	 * 
	 * @return the length of this password
	 */
	public int length() {
		return this.elements.size();
	}

	/**
	 * Determines the length of the longest password in a list of cognometric 
	 * grouped passwords
	 * 
	 * @param passwords The list of passwords
	 * @return The length of the longest password in the list
	 */
	public static int getMaxLength(List<CognometricGroupedPassword> passwords) {
		
		int max=0;
		
		for ( CognometricGroupedPassword pw : passwords ) {
			if ( pw.length()>max ) max=pw.length();
		}
		
		return max;
	}
	
}
