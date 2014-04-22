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
package estimators;

/**
 * This class provides functions for all estimators, regardless
 * of the underlying metric.
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
public class CommonHelper {

	/**
	 * Calculates log2(x)
	 * 
	 * @param x x
	 * @return log2(x)
	 */
	public static double log2(double x) {
		return Math.log(x)/Math.log(2);
	}

	/**
	 * Calculates the total sum of all elements in an array 
	 * 
	 * @param a The array
	 * @return Sum of all elements in a
	 */
	public static int getTotal(int[] a) {
		int total=0;
		
		for (int i : a) {
			total += i;
		}
		
		return total;
	}

	/**
	 * Calculates the total sum of all elements in an array 
	 * 
	 * @param a The array
	 * @return Sum of all elements in a
	 */
	public static double getTotal(double[] a) {
		double total=0;
		
		for (double i : a) {
			total += i;
		}
		
		return total;
	}

}
