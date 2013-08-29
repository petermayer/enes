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

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Interface for the different entropy estimators. This class also contains static
 * functions for use with all estimators, such as functions to calculate Shanon 
 * entropy values from one-dimensional and two-dimensional frequency arrays.
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
public abstract class EntropyEstimatorI {
	
	/**
	 * Calculates an entropy estimate for the specified passwords
	 * 
	 * @param passwords The passwords to consider
	 * @return The entropy estimate
	 */
	public abstract double calculateEntropy(List<String> passwords);
	
	/**
	 * Prints the result of the entropy estimation
	 * 
	 * @param outWriter The sink to write the results to
	 */
	public abstract void printResult(Writer outWriter) throws IOException;
	
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
	 * Calculates the probabilities from frequencies
	 * 
	 * @param frequencies The frequencies
	 * @return The probabilities
	 */
	private static double[] getProbabilities(int[] frequencies) {
		
		int amountElements=EntropyEstimatorI.getTotal(frequencies);
		
		double[] probs=new double[frequencies.length];
		
		for (int i=0; i<frequencies.length; i++) {
			probs[i]= ((double)frequencies[i]) / ((double)amountElements);
		}
		
		return probs;
	}
	
	/**
	 * Generic function to calculate the Shanon entropy from the frequencies
	 * 
	 * @param frequencies The frequencies
	 * @return Shanon entropy
	 */
	public static double getEntropy(int[] frequencies) {
		
		double[] probs=EntropyEstimatorI.getProbabilities(frequencies);
		
		double entropy=0;
		
		for (double prob : probs) {
			if (prob==0) continue;
			entropy += prob*EntropyEstimatorI.log2(prob);
		}
		
		return -1 * entropy;
	}
	
	/**
	 * Generic function to calculate the Shanon entropy from a two-dimensional frequency array
	 * 
	 * @param frequencies The frequencies
	 * @return The entropy estimate
	 */
	public static double get2dimEntropy(int[][] frequencies) {
		
		double[][] probs=EntropyEstimatorI.getProbabilities(frequencies);
		
		double entropy=0;
		
		for (int i=0; i<probs.length; i++) {
			for ( int j=0; j<probs[i].length; j++) {
				if (probs[i][j]==0) continue;
				entropy += probs[i][j]*EntropyEstimatorI.log2(probs[i][j]);
			}
		}
		
		return -1 * entropy;
		
	}
	
	/**
	 * Calculates probabilities from two-dimensional frequencies
	 * 
	 * @param frequencies the frequencies
	 * @return The probabilities
	 */
	private static double[][] getProbabilities(int[][] frequencies) {
		
		double amountElements=get2dimTotal(frequencies);
		
		double[][] probs=new double[frequencies.length][frequencies[0].length];
		
		for ( int i=0; i<frequencies.length; i++ ) {
			for ( int j=0; j<frequencies[i].length; j++ ) {
				probs[i][j]=((double)frequencies[i][j])/(amountElements);
			}
		}
		
		return probs;
	}
	
	/**
	 * Calculates the total sum of the elements of a two-dimensional array
	 * 
	 * @param a The array to sum up
	 * @return The total sum
	 */
	private static int get2dimTotal(int[][] a) {
		
		int total=0;
		
		for ( int i=0; i<a.length; i++ ) {
			for ( int j=0; j<a[i].length; j++ ) {
				total += a[i][j];
			}
		}
		
		return total;
	}
}
