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

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import parsing.PasswordFileParser;
import estimators.MetricEstimatorI;
import estimators.click.entropy.ClickEntropyEstimatorDep;
import estimators.click.entropy.ClickEntropyEstimatorIndep;
import estimators.click.guesswork.AlphaGuessworkClickEstimator;
import estimators.text.entropy.TextEntropyEstimator;

/**
 * This application provides multiple methods to analyze the password space 
 * defined through password sets of different types. Different metrics can be
 * estimated for text passwords and graphical click-based passwords. Currently,
 * the following methods are available:
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
public final class EnEs {
	
	/**
	 * There should never be an instance of this class
	 */
	private EnEs(){};
	
	/**
	 * The main function, processing the arguments and starting the entropy calculation 
	 * 
	 * @param args The arguments as outlined in function printHelp 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" }) // Unfortunately sees problems with the generics in the parser, where there are none
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
		 * 2. Check whether valid estimation method
		 */
		MetricEstimatorI estimator=EnEs.checkMethod((String)os.valueOf("m"));
		if ( estimator == null ) {
			System.err.println("Invalid estimation method: "+os.valueOf("m"));
			System.exit(1);
		}
		
		/*
		 * 3. Read password file, this actually takes place in the PasswordFileParser
		 * class. The fourth step is included in the try-block for simplicity's sake.
		 */
		PasswordFileParser parser;
		Object estimate=null;
		try {
			parser=new PasswordFileParser((String)os.valueOf("i"));
			
			/*
			 * 4. Check whether the parsed file is sensible for the chosen estimator 
			 * and if yes then calculate estimate
			 */
			if ( !parser.setPasswordType(estimator.getPasswordType()) ) {
				System.err.println("Malformatted password file or incompatible estimator choice!");
				System.exit(1);
			}
			estimate=estimator.calculateMetric(parser.getPasswords(),parser.getParameters());
			
		//This catch-blocks are necessary due to the operations in the constructor of PasswordFileParser	
		} catch (FileNotFoundException e) {
			System.err.println("File not found: "+os.valueOf("i"));
			System.exit(1);
		} catch (IOException e) {
			System.err.println("File cloud not be read: "+os.valueOf("i"));
			System.exit(1);
		}
		
		/*
		 * 5. Create output
		 */
		//This should never apply, but for safety we check whether the estimation actually happened
		if ( estimate == null ) {
			System.err.println("Whoops, well this is embarrassing... It seems you managed to outwit the parser and the estimator! Please contact the developer with information on how you managed to do this.");
			System.exit(1);
		}
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
		if ( os.has("v") ) estimator.verbosePrintResult(new BufferedWriter(writer));
		else estimator.shortPrintRestult(new BufferedWriter(writer));
		
	}
	
	/**
	 * Checks whether the specified estimation method is valid. All new methods have
	 * to be added here to be available.
	 * 
	 * @param methodArg The specified method
	 * @return If method is valid: estimator object; if not: null
	 */
	private static MetricEstimatorI<?> checkMethod(String methodArg) {
		
		if ( methodArg.equalsIgnoreCase("text_entropy" ) ) {
			return new TextEntropyEstimator();
		} else if ( methodArg.equalsIgnoreCase("gp_click_entropy_dep" ) ) {
			return new ClickEntropyEstimatorDep();
		} else if ( methodArg.equalsIgnoreCase("gp_click_entropy_indep" ) ) {
			return new ClickEntropyEstimatorIndep();
		} else if ( methodArg.equalsIgnoreCase("gp_click_guesswork" ) ) {
			return new AlphaGuessworkClickEstimator();
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
		System.out.println("   * text_entropy");
		System.out.println("   * gp_click_entropy_dep");
		System.out.println("   * gp_click_entropy_indep");
		System.out.println("   * gp_click_guesswork");
		System.out.println("-i Path to password file");
		System.out.println("-o Path to output file (optional)");
		System.out.println("-v Enable verbose output (optional, default: print overall entropy estimate only)");
	}

}
