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
