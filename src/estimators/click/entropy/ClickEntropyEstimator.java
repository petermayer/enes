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
package estimators.click.entropy;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import main.passwords.ClickPassword;
import estimators.CommonHelper;
import estimators.ShannonEntropyHelper;

/**
 * This class provides an estimator for the entropy among a password set
 * according to the methodology of Dirik et al. [1]. It calculates the
 * Shanon entropy of the click-points by dividing the complete coordinate
 * space into grids with the size of the tolerance margin of the scheme
 * and then calculates the probability of each such grid cell. This class
 * is not intended to be used directly. Instead the classes DirikEstimatorDep
 * and DirikEstimatorIndep provide the public interface for dependent and
 * independent click-point data respectively.
 * 
 * <table border="0">
 * <tr>
 * <td valign="top">[1]</td>
 * <td>A. E. Dirik, N. Memon, and J.-C. Birget. Modeling user choice in the PassPoints graphical password scheme. In SOUPS '07: Proceedings of the 3rd Symposium on Usable Privacy and Security, pages 20-28. ACM, 2007.</td>
 * </tr>
 * </table>
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
class ClickEntropyEstimator {

	/**
	 * Whether the calculation has finished
	 */
	private boolean calculated=false;
	
	/**
	 * The parameters as provided from the parser
	 * 
	 *  0: x-max
	 *  1: y-max
	 *  2: tolerance
	 */
	private int[] parameters=new int[3];
	
	/**
	 * The maximum length among the passwords
	 */
	private int maxLength=0;
	
	/**
	 * The results of all calculations
	 * 
	 * 0: entropy cp1
	 * 1: entropy cp2
	 * ...
	 * n-1: entropy cpn
	 * 
	 * if dependent:
	 * n: cumulated entropy cp1-cp1
	 * n+1: cumulated entropy cp1-cp2
	 * ...
	 * 2*n-1: cumulated entropy cp1-cpn
	 */
	private double[] results;
	
	/**
	 * This method executes the actual entropy calculation. It will delete all processed
	 * entries from the original password list to memorize the used memory space. 
	 * 
	 * @param passwords The password list
	 * @param dependent Whether the click-points are dependent
	 * @return Entropy estimate
	 */
	public double calculateEstimate(List<ClickPassword> passwords, int[] parameters, final boolean dependent) {
		
		/*
		 * 1. Make the supplied parameters available for further processing
		 */
		this.parameters=parameters;
		
		/*
		 * 2. Get some more configuration data
		 */
		this.maxLength=ClickPassword.getMaxLength(passwords);
		if (!dependent) this.results=new double[this.maxLength];
		else this.results=new double[this.maxLength*2];
		
		/*
		 * 5. Create buckets
		 */
		int[][] buckets=new int[0][0];
		int[][] depbuckets=new int[0][0];
		
		// The depbuckets need only be instantiated once as all cps are congregated in them
		if (dependent) depbuckets=new int[(int)Math.ceil((double)this.parameters[0]/this.parameters[2])][(int)Math.ceil((double)this.parameters[1]/this.parameters[2])];
		
		/*
		 * 6. Iterate over all click-points
		 */
		for ( int i=0; i<this.maxLength; i++ ) {
			
			//New bucktes for each click point
			buckets=new int[(int)Math.ceil((double)this.parameters[0]/this.parameters[2])][(int)Math.ceil((double)this.parameters[1]/this.parameters[2])];
			
			for ( ClickPassword cpw : passwords ) {
				int x=(int)Math.ceil((double)cpw.getClickPoint(i)[0]/this.parameters[2]);
				int y=(int)Math.ceil((double)cpw.getClickPoint(i)[1]/this.parameters[2]);
				
				buckets[x][y]++;
				if (dependent) depbuckets[x][y]++;
			}
			
			this.results[i]=ShannonEntropyHelper.getEntropy(this.convertToOneDim(buckets));
			if (dependent) this.results[this.maxLength+i]=ShannonEntropyHelper.getEntropy(this.convertToOneDim(depbuckets));
			
		}
		
		this.calculated=true;
		
		if (dependent) return this.results[this.results.length-1];
		else return CommonHelper.getTotal(this.results);
	}

	/**
	 * Prints the result of the entropy estimation
	 * 
	 * @param outWriter The sink to write the results to
	 * @param dependent Whether the click-points are dependent
	 */
	public void printResult(Writer outWriter, boolean dependent) {
		
		if ( !calculated ) {
			System.err.println("Can't print: calculation not finished.");
			return;
		}
		
		try {
			double indepOverall=0;
			for ( int i=0; i<(this.results.length/(int)Math.pow(2, (dependent?1:0))); i++) {
				outWriter.write("Click-point: "+(i+1)+"\n");
				outWriter.write("Click-point entropy: "+this.results[i]+"\n");
				if ( dependent ) outWriter.write("Overall entropy: "+this.results[i+this.maxLength]+"\n");
				else indepOverall += this.results[i];
				outWriter.write("-------------------------------\n");
			}
			if ( !dependent ) outWriter.write("Overall entropy: "+indepOverall+"\n");
			outWriter.close();
		} catch (IOException e) {
			System.err.println("Could not write to target output.");
		}
		
	}
	
	/**
	 * Converts a two-dimensional array into a one-dimensional array. The
	 * new two dimensional array is constructed by appending the arrays 
	 * from the original one, first to last.
	 * 
	 * @param array The two-dimensional array
	 * @return The new one-dimensional array
	 */
	private int[] convertToOneDim(int[][] array) {
		
		int[] newArray=new int[array.length*array[0].length];
		
		
		for ( int i=0; i<array.length; i++ ) {
			for ( int j=0; j<array[0].length; j++ ) {
				newArray[i*array[0].length+j]=array[i][j];
			}
		}
		
		return newArray;
		
	}
	
	public void printSummary(Writer outWriter, boolean dependent) {
		
		if ( !calculated ) {
			System.err.println("Can't print: calculation not finished.");
			return;
		}
		
		try {
			double indepOverall=0;
			for ( int i=0; i<(this.results.length/(int)Math.pow(2, (dependent?1:0))); i++) {
				if ( dependent ) outWriter.write("Overall entropy: "+this.results[i+this.maxLength]+"\n");
				else indepOverall += this.results[i];
			}
			if ( !dependent ) outWriter.write("Overall entropy: "+indepOverall+"\n");
			outWriter.close();
		} catch (IOException e) {
			System.err.println("Could not write to target output.");
		}
		
	}

}
