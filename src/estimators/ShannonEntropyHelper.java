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
 * This class provides functions for use with all Shannon entropy estimators.
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
public class ShannonEntropyHelper {

	/**
	 * Calculates the probabilities from frequencies
	 * 
	 * @param frequencies The frequencies
	 * @return The probabilities
	 */
	private static double[] getProbabilities(int[] frequencies) {
		
		int amountElements=CommonHelper.getTotal(frequencies);
		
		double[] probs=new double[frequencies.length];
		
		for (int i=0; i<frequencies.length; i++) {
			probs[i]= ((double)frequencies[i]) / ((double)amountElements);
		}
		
		return probs;
	}

	/**
	 * Generic function to calculate the Shannon entropy from the frequencies
	 * 
	 * @param frequencies The frequencies
	 * @return Shannon entropy
	 */
	public static double getEntropy(int[] frequencies) {
		
		double[] probs=getProbabilities(frequencies);
		
		double entropy=0;
		
		for (double prob : probs) {
			if (prob==0) continue;
			entropy += prob*CommonHelper.log2(prob);
		}
		
		return -1 * entropy;
	}

}
