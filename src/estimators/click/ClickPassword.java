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
package estimators.click;

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