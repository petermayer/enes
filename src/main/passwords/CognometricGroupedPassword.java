/*=========================================================================
 * Copyright 2013 EnEs Authors
 *
 * Licensed under the Attribution-ShareAlike 3.0 Unported license (the 
 * "License"). You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at:
 *
 * https://creativecommons.org/licenses/by-sa/3.0/legalcode
 * 
 * Or have a look at the tldr; version: 
 * 
 * https://creativecommons.org/licenses/by-sa/3.0/deed.en
 *
 * This software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 *=========================================================================*/
package main.passwords;

import java.util.ArrayList;

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
	
}
