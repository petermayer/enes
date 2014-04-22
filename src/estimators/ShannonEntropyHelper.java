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
