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
package estimators.text.entropy;

import java.io.IOException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.List;

import main.passwords.PasswordType;

import org.apache.commons.lang3.ArrayUtils;

import estimators.CommonHelper;
import estimators.MetricEstimatorI;
import estimators.ShannonEntropyHelper;

/**
 * This class provides an estimator for the entropy among a password list
 * according to the methodology of Shay et al. [1]. It calculates the
 * Shanon entropy originating from the length of the passwords and for
 * each type of character (digits, symbols, upper case letters, lower
 * case letters) the entropy originating from the number of characters
 * of that type, the placement of the characters in the password and the
 * occurrence of each possible character of the type. For lower case 
 * letters, the entropy originating from the number of occurrences and the
 * placement is skipped. This information is known deterministically
 * once the other sources of entropy have been processed; it
 * adds 0 bits of entropy.  
 * <br>
 * The password file is expected to be UTF-8 encoded and to contain one
 * password in each line. No additional lines should be placed at the end
 * or the beginning of the file as to allow empty passwords to be
 * considered.
 * 
 * <table border="0">
 * <tr>
 * <td valign="top">[1]</td>
 * <td>R. Shay, S. Komanduri, P. G. Kelley, P. G. Leon, M. L. Mazurek, L. Bauer, N. Christin, and L. F. Cranor. Encountering Stronger Password Requirements: User Attitudes and Behaviors. In SOUPS '10: Proceedings of the Sixth Symposium on Usable Privacy and Security. ACM, July 2010.</td>
 * </tr>
 * </table>
 * 
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 *
 */
public class TextEntropyEstimator extends MetricEstimatorI<String> {

	/**
	 * Whether the calculation has finished
	 */
	private boolean calculated=false;
	
	/**
	 * The results of all calculations
	 * 
	 * 0:  length
	 * 1:  number of digits
	 * 2:  digit placement
	 * 3:  different digits
	 * 4:  number of symbols
	 * 5:  symbol placement
	 * 6:  different symbols
	 * 7:  number of upper case letters
	 * 8:  upper case letter placement
	 * 9:  different upper case letters
	 * 10: different lower case letters
	 */
	private double[] results=new double[11];
	
	@Override
	public Object calculateMetric(List<String> passwords, int[] parameters) {
		
		int maxLength=this.getMaxLength(passwords);
		
		//1. Entropy in length
		results[0]=this.getLengthEntropy(passwords, maxLength);
		
		//2. Entropy in digits
		results[1]=this.getNumberEntropy(passwords, maxLength, new DigitTypeHandler());
		results[2]=this.getPlacementEntropy(passwords, maxLength, new DigitTypeHandler());
		results[3]=this.getInTypeEntropy(passwords, new DigitTypeHandler());
		
		//3. Entropy in symbols
		results[4]=this.getNumberEntropy(passwords, maxLength, new SymbolTypeHandler());
		results[5]=this.getPlacementEntropy(passwords, maxLength, new SymbolTypeHandler());
		results[6]=this.getInTypeEntropy(passwords, new SymbolTypeHandler());
		
		//4. Entropy in upper case letters
		results[7]=this.getNumberEntropy(passwords, maxLength, new UpperCaseLetterTypeHandler());
		results[8]=this.getPlacementEntropy(passwords, maxLength, new UpperCaseLetterTypeHandler());
		results[9]=this.getInTypeEntropy(passwords, new UpperCaseLetterTypeHandler());
		
		//5. Entropy in lower case letters
		results[10]=this.getInTypeEntropy(passwords, new LowerCaseLetterTypeHandler());
		
		this.calculated=true;
		return CommonHelper.getTotal(results);
	}
	
	/**
	 * Calculates the entropy caused by the different lengths
	 * 
	 * @param pwds The passwords
	 * @param maxLen The maximum length of all passwords
	 * @return Entropy estimate
	 */
	private double getLengthEntropy(List<String> pwds, int maxLen) {
		
		int[] lengths=new int[maxLen+1];
		
		for (String pwd : pwds)	lengths[pwd.length()]++;
		
		return ShannonEntropyHelper.getEntropy(lengths);
	}
	
	/**
	 * Calculates the maximum length of all Strings in a list
	 * 
	 * @param pwds The passwords (String array)
	 * @return The maximum length
	 */
	private int getMaxLength(List<String> pwds) {
		int maxLen=0;
		
		for (String pwd : pwds) {
			maxLen = pwd.length()>maxLen ? pwd.length() : maxLen;
		}
		
		return maxLen;
	}
	
	/**
	 * Calculates the entropy caused by the number of a character type
	 * 
	 * @param pwds The passwords
	 * @param maxLen The maximum length of all passwords
	 * @param type The type of character
	 * @return Entropy estimate
	 */
	private double getNumberEntropy(List<String> pwds, int maxLen, CharacterTypeHandler type) {
		
		int[] number=new int[maxLen+1];
		
		for (String pwd : pwds) {
			
			int pwdNumber=0;
			
			for ( int i=0; i<pwd.length(); i++ ) {
				if ( type.isOfType(pwd.charAt(i)) ) pwdNumber++;
			}
			
			number[pwdNumber]++;
		}
		
		return ShannonEntropyHelper.getEntropy(number);
	}
	
	/**
	 * Calculates the entropy caused by the placement of a character type
	 * 
	 * @param pwds The passwords
	 * @param maxLen The maximum length of all passwords
	 * @param type The type of character
	 * @return Entropy estimate
	 */
	private double getPlacementEntropy(List<String> pwds, int maxLen, CharacterTypeHandler type) {
		
		int[] placements=new int[maxLen+1];
		
		for (String pwd : pwds) {
			
			for ( int i=0; i<pwd.length(); i++ ) {
				if ( type.isOfType(pwd.charAt(i)) ) {
					placements[i]++;
				}
			}
			
		}
		
		return ShannonEntropyHelper.getEntropy(placements);
	}
	
	/**
	 * Calculates the entropy caused by usage of different characters of a type
	 * 
	 * @param pwds The passwords
	 * @param type The type of character
	 * @return Entropy estimate
	 */
	private double getInTypeEntropy(List<String> pwds, CharacterTypeHandler type) {
		
		Hashtable<Character, Integer> inTypeAmounts=new Hashtable<Character, Integer>();
		
		for ( String pwd : pwds ) {
			
			for ( int i=0; i<pwd.length(); i++ ) {
				
				if ( type.isOfType(pwd.charAt(i)) ) {
					
					Integer amount=inTypeAmounts.get(pwd.charAt(i));
					
					if ( amount == null ) {
						amount = 1;
					} else {
						amount++;
					}
					
					inTypeAmounts.put(pwd.charAt(i), amount);
				}
				
			}
		}
		
		return ShannonEntropyHelper.getEntropy(ArrayUtils.toPrimitive(inTypeAmounts.values().toArray((new Integer[1]))));
		
	}

	@Override
	public void verbosePrintResult(Writer outWriter) throws IOException {
		
		if ( !calculated ) {
			System.err.println("Can't print: calculation not finished.");
			return;
		}
		
		outWriter.write("Length: "+results[0]+"\n");
		
		outWriter.write("-------------------------------\n");
		
		outWriter.write("Number Amount: "+results[1]+"\n");
		
		outWriter.write("Number Placement: "+results[2]+"\n");
		
		outWriter.write("Number Type: "+results[3]+"\n");
		
		outWriter.write("Number Total: "+(results[1]+results[2]+results[3])+"\n");
		
		outWriter.write("-------------------------------\n");
		
		outWriter.write("Symbol Amount: "+results[4]+"\n");
		
		outWriter.write("Symbol Placement: "+results[5]+"\n");
		
		outWriter.write("Symbol Type: "+results[6]+"\n");
		
		outWriter.write("Symbol Total: "+(results[4]+results[5]+results[6])+"\n");
		
		outWriter.write("-------------------------------\n");
		
		outWriter.write("Uppercase Amount: "+results[7]+"\n");
		
		outWriter.write("Uppercase Placement: "+results[8]+"\n");
		
		outWriter.write("Uppercase Type: "+results[9]+"\n");
		
		outWriter.write("Uppercase Total: "+(results[7]+results[8]+results[9])+"\n");
		
		outWriter.write("-------------------------------\n");
		
		outWriter.write("Lowercase Type: "+results[10]+"\n");
		
		outWriter.write("Lowercase Total: "+results[10]+"\n");
		
		outWriter.write("-------------------------------\n");
		
		outWriter.write("Entropy Total: "+CommonHelper.getTotal(results)+"\n");
		
		outWriter.close();
		
	}
	
	/**
	 * Defines a generic interface so the methods for the number of occurrences,
	 * the placement and the different types of characters can be reused. This
	 * interface exists only due to the lack of the function type in current
	 * Java.
	 */
	private interface CharacterTypeHandler {
		
		/**
		 * @return Whether the character is of the wanted type 
		 */
		public boolean isOfType(char c);
		
	}
	
	/**
	 * Handler for the digit type characters
	 */
	private class DigitTypeHandler implements CharacterTypeHandler {
		@Override
		public boolean isOfType(char c) {
			return Character.isDigit(c);
		}
	}
	
	/**
	 * Handler for the symbol type characters
	 */
	private class SymbolTypeHandler implements CharacterTypeHandler {
		@Override
		public boolean isOfType(char c) {
			return (!Character.isLowerCase(c) && !Character.isUpperCase(c) && !Character.isDigit(c));
		}
	}
	
	/**
	 * Handler for the upper case letter characters
	 */
	private class UpperCaseLetterTypeHandler implements CharacterTypeHandler {
		@Override
		public boolean isOfType(char c) {
			return Character.isUpperCase(c);
		}
		
	}
	
	/**
	 * Handler for the lower case letter characters
	 */
	private class LowerCaseLetterTypeHandler implements CharacterTypeHandler {
		@Override
		public boolean isOfType(char c) {
			return Character.isLowerCase(c);
		}
		
	}

	@Override
	public PasswordType getPasswordType() {
		return PasswordType.TEXT;
	}

	@Override
	public void shortPrintRestult(Writer outWriter) throws IOException {
		
		if ( !calculated ) {
			System.err.println("Can't print: calculation not finished.");
			return;
		}
		
		outWriter.write("Entropy Total: "+CommonHelper.getTotal(results)+"\n");
		
		outWriter.close();
		
	}

}
