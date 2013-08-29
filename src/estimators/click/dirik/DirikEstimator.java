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
package estimators.click.dirik;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import estimators.EntropyEstimatorI;
import estimators.click.ClickPassword;

/**
 * This class provides an estimator for the entropy among a password set
 * according to the methodology of Dirik et al. [1]. It calculates the
 * Shanon entropy of the click-points by dividing the complete coordinate
 * space into grids with the size of the tolerance margin of the scheme
 * and then calculates the probability of each such grid cell. This class
 * is not intended to be used directly. Instead the classes DirikEstimatorDep
 * and DirikEstimatorIndep provide the public interface for dependent and
 * independent click-point data respectively.
 * <br>
 * The password file is expected to be UTF-8 encoded and to contain one
 * password in each line, where the format is: "x1,y2;x2,y2;...;xn,yn" 
 * (without quotation marks) for n click-points. Currently only integer 
 * values are supported for the coordinates. The first line has to contain
 * three values, the maximum value for the x-coordinates, the maximum value
 * for the y-coordinates and the tolerance margin, where the format is:
 * "xmax,ymax,tolerance" (without quotation marks).
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
class DirikEstimator {

	/**
	 * Whether the calculation has finished
	 */
	private boolean calculated=false;
	
	/**
	 * The parameters as read from the password file.
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
	public double calculateEntropy(List<String> passwords,final boolean dependent) {
		
		/*
		 * 1. Create new password list
		 */
		ClickPassword[] pwds=new ClickPassword[passwords.size()-1];
		
		/*
		 * 2. Parse parameters
		 */
		this.parameters=this.arrayStringToInt(passwords.get(0).split(","));
		passwords.remove(0);
		
		/*
		 * 3. Parse passwords
		 */
		for ( int i=passwords.size()-1; i>=0; i-- ) {
			pwds[i]=this.parsePassword(passwords.get(i));
			passwords.remove(i);
		}
		
		/*
		 * 4. Some more configurations
		 */
		this.maxLength=ClickPassword.getMaxLength(pwds);
		if (!dependent) this.results=new double[this.maxLength];
		else this.results=new double[this.maxLength*2];
		
		/*
		 * 5. Create buckets
		 */
		int[][] buckets=new int[0][0];
		int[][] depbuckets=new int[0][0];
		if (dependent) depbuckets=new int[(int)Math.ceil((double)this.parameters[0]/this.parameters[2])][(int)Math.ceil((double)this.parameters[1]/this.parameters[2])];
		
		/*
		 * 6. Iterate over all click-points
		 */
		for ( int i=0; i<this.maxLength; i++ ) {
			
			buckets=new int[(int)Math.ceil((double)this.parameters[0]/this.parameters[2])][(int)Math.ceil((double)this.parameters[1]/this.parameters[2])];
			
			for ( ClickPassword cpw : pwds ) {
				int x=(int)Math.ceil((double)cpw.getClickPoint(i)[0]/this.parameters[2]);
				int y=(int)Math.ceil((double)cpw.getClickPoint(i)[1]/this.parameters[2]);
				
				buckets[x][y]++;
				if (dependent) depbuckets[x][y]++;
			}
			
			this.results[i]=EntropyEstimatorI.get2dimEntropy(buckets);
			if (dependent) this.results[this.maxLength+i]=EntropyEstimatorI.get2dimEntropy(depbuckets);
			
		}
		
		this.calculated=true;
		
		if (dependent) return this.results[this.results.length-1];
		else return EntropyEstimatorI.getTotal(this.results);
	}

	/**
	 * Parses one line of the password file to extract the password information
	 * 
	 * @param pwd The password as specified in the file
	 * @return The password in a representation as needed for the entropy estimation
	 */
	private ClickPassword parsePassword(String pwd) {
		
		ClickPassword cpw=new ClickPassword();
		
		for ( String cp : pwd.split(";") ) {
			cpw.addClickPoint(this.arrayStringToInt(cp.split(",")));
		}
		
		return cpw;
		
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
			for ( int i=0; i<(this.results.length/(int)Math.pow(2, (dependent?1:0))); i++) {
				outWriter.write("Click-point: "+(i+1)+"\n");
				outWriter.write("Click-point entropy: "+this.results[i]+"\n");
				if ( dependent ) outWriter.write("Overall entropy: "+this.results[i+this.maxLength]+"\n");
				outWriter.write("-------------------------------\n");
			}
			outWriter.close();
		} catch (IOException e) {
			System.err.println("Could not write to target output.");
		}
		
		
		
	}
	
	/**
	 * Converts an array of Strings into an array of Integer primitives
	 * 
	 * @param a The array to convert
	 * @return The new array with the converted values
	 */
	private int[] arrayStringToInt(String[] a) {
		
		int[] b=new int[a.length];
		
		for ( int i=0; i<a.length; i++ ) {
			b[i]=Integer.parseInt(a[i]);
		}
		
		return b;
	}

}
