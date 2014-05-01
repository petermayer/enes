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
package estimators.click.guesswork;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import main.passwords.ClickPassword;
import main.passwords.PasswordType;
import estimators.GuessworkHelper;
import estimators.MetricEstimatorI;

/**
 * This provides an estimator for the alpha-guesswork metric according to 
 * Bonneau [1]. This estimator assumes independence between the click-points
 * in each password. Thus it is suitable for schemes such as PCCP, but for
 * schemes such as PassPoints.
 * The metric is calculated for alpha between 0.01 and 1.00 in steps of 0.01.
 * 
 * <table border="0">
 * <tr>
 * <td valign="top">[1]</td>
 * <td>J. Bonneau, The Science of Guessing: Analyzing an Anonymized Corpus of 70 Million Passwords, 2012 IEEE Symposium on Security and Privacy (SP), pp. 538-552, 2012.</td>
 * </tr>
 * </table>
 * 
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
public class ClickAlphaGuessworkEstimator extends MetricEstimatorI<ClickPassword> {

	/**
	 * In this array the alpha-guesswork values are stored 
	 */
	private double[][] results;
	
	@Override
	public Object calculateMetric(List<ClickPassword> passwords, int[] parameters) {

		int maxLength=ClickPassword.getMaxLength(passwords);
		
		/*
		 * Initialize buckets: One bucket of length (x/margin)*(y/margin) for each click point
		 */
		double[][] buckets=new double[maxLength][(int)Math.ceil(parameters[0]/parameters[2]) * (int)Math.ceil(parameters[1]/parameters[2])];

		
		/*
		 * Process each click point position
		 */
		for ( int pos=0; pos<maxLength; pos++ ) {

			//for each password sort into bucket
			for ( int pwd=0; pwd<passwords.size(); pwd++ ) {
				buckets[pos][(int)(passwords.get(pwd).getClickPoint(pos)[0]/parameters[2])*(int)Math.ceil(parameters[1]/parameters[2]) + (int)(passwords.get(pwd).getClickPoint(pos)[1]/parameters[2])]++;
			}

		}

		//Sort all the buckets
		for ( int z=0; z<maxLength; z++ ) {
			Arrays.sort(buckets[z]);
			//Now the first element is in last position, so we to reverse the array
			
			//But first we calculate the total, so we can get the probabilities in the same step
			double sum=0; for ( double d : buckets[z]) sum += d;
			for ( int i=0; i<Math.floor(buckets[z].length/2); i++ ) {
				double temp=buckets[z][i]/sum;
				buckets[z][i]=buckets[z][buckets[z].length-1-i]/sum;
				buckets[z][buckets[z].length-1-i]=temp;
			}

		}

		/*
		 * Actual alpha-guesswork calculation
		 */
		this.results=new double[maxLength][100];
		int alpha=1;
		while ( alpha < 100 ) {
			
			//for each of the click-point positions
			for ( int i=0; i<this.results.length; i++ ) {
				this.results[i][(int)(alpha)]=GuessworkHelper.alphaGuessworkBits(buckets[i], ((double)alpha)/100);
			}
			
			//Increase alpha by 1%
			alpha += 1;
		}

		return this.results;
	}

	@Override
	public void verbosePrintResult(Writer outWriter) throws IOException {

		// prevent printing before calculation
		if ( this.results==null ) return;
		
		/*
		 * Print each click point after the other. This produces a lot of output.
		 */
		for ( int i=1; i<this.results.length; i++ ) {
			outWriter.write("-------------------------------\n");
			outWriter.write("Click-point: "+i+"\n");
			
			for ( int j=0; j<this.results[i].length; j++ ) {
				outWriter.write(this.results[i][j]+"\n");
			}
		}
		outWriter.close();

	}

	@Override
	public PasswordType getPasswordType() {
		return PasswordType.GRAPHICAL_CLICK;
	}

	@Override
	public void shortPrintRestult(Writer outWriter) throws IOException {
		
		/*
		 * For each click-point print the range from min to max. As the metric is monotonically 
		 * increasing, we can simply take the first an the last element.
		 * 
		 */
		for( int i=0; i<this.results.length; i++ ) {
			outWriter.write("Click-point "+i+": "+this.results[i][1]+" - "+this.results[i][this.results[i].length-1]+"\n");
		}
		outWriter.close();
	}

}
