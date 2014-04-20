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
package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import estimators.EntropyEstimatorI;
import estimators.click.dirik.DirikEstimatorDep;
import estimators.click.dirik.DirikEstimatorIndep;
import estimators.click.spatial.ChiassonEstimator;
import estimators.text.shay.ShayEstimator;

/**
 * This application provides multiple methods to analyze the entropy of password sets
 * of different types. Entropy estimates can be obtained for text passwords and graphical
 * click-based passwords. The following methods are available:
 * 
 * <ul>
 * <li>Text methods
 * <ul>
 * <li>Shanon entropy - Shay et al. [1]</li>
 * </ul>
 * </li>
 * <li>Graphical click-based methods
 * <ul>
 * <li>Shanon entropy - Dirik et al. [2]</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * 
 * <h3>References</h3>
 * <table border="0">
 * <tr>
 * <td valign="top">[1]</td>
 * <td>R. Shay, S. Komanduri, P. G. Kelley, P. G. Leon, M. L. Mazurek, L. Bauer, N. Christin, and L. F. Cranor. Encountering Stronger Password Requirements: User Attitudes and Behaviors. In SOUPS '10: Proceedings of the 6th Symposium on Usable Privacy and Security. ACM, July 2010.</td>
 * </tr>
 * <tr>
 * <td valign="top">[2]</td>
 * <td>A. E. Dirik, N. Memon, and J.-C. Birget. Modeling user choice in the PassPoints graphical password scheme. In SOUPS '07: Proceedings of the 3rd Symposium on Usable Privacy and Security, pages 20-28. ACM, 2007.</td>
 * </tr>
 * </table>
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
public class EnEs {
	
	/**
	 * There should never be an instance of this class
	 */
	private EnEs(){};
	
	/**
	 * The main function, processing the arguments and starting the entropy calculation 
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		
		/*
		 * 1. Check whether arguments present
		 */
		OptionSet os=(new OptionParser("hm::i::vo::")).parse(args);
		
		if ( os.has("h") ) {
			EnEs.printHelp();
			System.exit(0);
		} else if ( !os.has("m") || !os.hasArgument("m") ) {
			System.err.println("No estimation method specified.");
			System.err.flush();
			EnEs.printHelp();
			System.exit(1);
		} else if ( !os.has("i") || !os.hasArgument("i") ) {
			System.err.println("No password file specified.");
			System.err.flush();
			EnEs.printHelp();
			System.exit(1);
		}

		
		/*
		 * 2. Check whether valid method
		 */
		EntropyEstimatorI estimator=EnEs.checkMethod((String)os.valueOf("m"));
		if ( estimator == null ) {
			System.err.println("Invalid estimation method: "+os.valueOf("m"));
			System.exit(1);
		}
		
		/*
		 * 3. Parse password file
		 */
		ArrayList<String> pwds=new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream((String)os.valueOf("i")),"UTF8"));
			String line;
			while ( (line=reader.readLine()) != null ) {
				pwds.add(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found: "+os.valueOf("i"));
			System.exit(1);
		} catch (IOException e) {
			System.err.println("File cloud not be read: "+os.valueOf("i"));
			System.exit(1);
		}
		
		/*
		 * 4. Calculate entropy estimate
		 */
		double estimate=estimator.calculateEntropy(pwds);
		
		
		/*
		 * 5. Create output
		 */
		OutputStreamWriter writer=new OutputStreamWriter(System.out);
		if ( os.has("o") ) {
			if ( !os.hasArgument("o") ) System.err.println("No output file specified: falling back to System.out");
			else {
				try {
					writer=new FileWriter((String)os.valueOf("o"));
				} catch (IOException e) {
					System.err.println("Could not write to file "+os.valueOf("o")+": falling back to System.out");
				}
			}
		}
		if ( os.has("v") ) estimator.printResult(new BufferedWriter(writer));
		else {
			BufferedWriter bw=new BufferedWriter(writer);
			bw.write(""+estimate);
			bw.close();
		}
		
	}
	
	/**
	 * Checks whether the specified estimation method is valid. All new methods have
	 * to be added here to be available.
	 * 
	 * @param methodArg The specified method
	 * @return If method is valid: estimator object; if not: null
	 */
	private static EntropyEstimatorI checkMethod(String methodArg) {
		
		if ( methodArg.equalsIgnoreCase("text_shay" ) ) {
			return new ShayEstimator();
		} else if ( methodArg.equalsIgnoreCase("gp_dirik_dep" ) ) {
			return new DirikEstimatorDep();
		} else if ( methodArg.equalsIgnoreCase("gp_dirik_indep" ) ) {
			return new DirikEstimatorIndep();
		} /*else if ( methodArg.equalsIgnoreCase("gp_chiasson_spatial" ) ) {
			return new ChiassonEstimator();
		} */else return null;
		
	}
	
	/**
	 * Print a short summary of the needed and optional arguments
	 */
	private static void printHelp() {
		System.out.println("USAGE: java -jar enes.jar -m <estimation method> -i <password file> [-o <output file>] [-v]");
		System.out.println("-m Estimation method to use. Available stable methods:");
		System.out.println("   * text_shay");
		System.out.println("   * gp_dirik_dep");
		System.out.println("   * gp_dirik_indep");
		System.out.println("-i Path to password file");
		System.out.println("-o Path to output file (optional)");
		System.out.println("-v Enable verbose output (optional, default: print overall entropy estimate only)");
	}

}
