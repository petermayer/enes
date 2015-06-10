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
package parsing;

import java.util.ArrayList;
import java.util.List;

import main.passwords.CognometricGroupedPassword;
import main.passwords.PasswordType;
import parsing.PasswordFileParser.PasswordFileParserI;

/**
 * This class provides functions for parsing password files of graphical 
 * cognometric semantically grouped passwords. The password file is expected
 * to be UTF-8 encoded and to contain one password in each line, where the
 * format is: "g1,e2;g2,e2;...;gn,en" (without quotation marks) for a password
 * of length n. The first line of the file has to contain two values: i) the 
 * number of different groups and the number of elements in one group. If the
 * groups have differ in size, the largest size should be specified. The format
 * for the two parameters is: "#groups,#elements in group" (without quotation 
 * marks).
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
public class GraphicalCognometricFileParser implements PasswordFileParserI<CognometricGroupedPassword> {

	private int[] parameters=new int[0];
	
	@Override
	public PasswordType getPasswordType() {
		return PasswordType.GRAPHICAL_COGNOMETRIC_GROUP;
	}

	@Override
	public boolean checkWhetherOfThisType(List<String> passwordFile) {

		//We always need to look at the first line of the file
		String firstLine=passwordFile.get(0);

		/*
		 * Explicit declaration: If the file was explicitly tagged we can assume soundness (as the naive little fellows we are)
		 */
		if ( firstLine.equalsIgnoreCase(PasswordFileParser.PWT_PREFIX+this.getPasswordType().toString()) ) {
			return true;
		}
		
		/*
		 * Heuristic determination: Graphical cognometric passwords that are 
		 * semantically grouped need two parameters specified in the password  
		 * first line of the password file: the number of groups and the amount
		 * of elements in each group. Both of these values need to be present
		 * (and no others) and they need to be grater than 0. The passwords 
		 * need to be parsable by "parseCognometricGroupedPassword". Currently 
		 * only the first password is checked at this point.
		 */
		try {
			//if the type is actually correct we can store the parameters directly, if not we will set them to null
			this.parameters=this.arrayStringToInt(firstLine.split(","));

			//do the parameters by themselves make sense?
			if ( this.parameters.length != 2 ||
					this.parameters[0] <= 0 ||
					this.parameters[1] <= 0 ||
					this.parameters[2] <= 0 ) {
				this.parameters=null;
				return false;
			}

			//do the parameters make sense in context of the passwords?
			//(only first password is actually checked, possibility to move whole parsing here left open)
			for ( int i=1; i<2; i++) {
				CognometricGroupedPassword pw=this.parseCognometricGroupedPassword(passwordFile.get(i));

				//iteration over all click-points
				for ( int j=0; j<pw.length(); j++ ) {
					int[] cp=pw.getElement(j);
					if ( cp[0]>this.parameters[0] || cp[1]>this.parameters[1] ) {
						this.parameters=null;
						return false;
					}
				}
			}

			//It seems all went well, we can set the type now
			return true;

		} catch (NumberFormatException e) {
			//parsing of parameters failed, nothing to do here
		}
		
		return false;
	}
	
	/**
	 * Converts an array of Strings into an array of Integer primitives
	 * TODO This method is an exact duplicate and needs to be removed
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
	
	/**
	 * Parses a string containing a cognometric grouped password
	 * 
	 * @param pwd The string containing the password
	 * @return The parsed password
	 */
	private CognometricGroupedPassword parseCognometricGroupedPassword(String pwd) {
		
		CognometricGroupedPassword pw=new CognometricGroupedPassword();
		
		for ( String cp : pwd.split(";") ) {
			pw.addElement(arrayStringToInt(cp.split(",")));
		}
		
		return pw;
	}

	@Override
	public List<CognometricGroupedPassword> getPasswords(List<String> passwordFile) {
		
		List<CognometricGroupedPassword> pwds=new ArrayList<CognometricGroupedPassword>();
		
		/*
		 * If the parameters have not yet been read, the password file has not yet been
		 * checked to be valid. In this case we simply return the empty list
		 */
		if( this.parameters.length==0 && !this.checkWhetherOfThisType(passwordFile)) {
			return pwds;
		}
		
		//Skipping the parameter line
		int startIndex=1;

		//If there is a tag at the beginning of the file, we need to skip that as well
		if ( passwordFile.get(0).equalsIgnoreCase(PasswordFileParser.PWT_PREFIX+this.getPasswordType().toString()) ) {
			startIndex=2;
		}
		
		for ( int i=startIndex; i<passwordFile.size(); i++ ) {
			pwds.add(parseCognometricGroupedPassword(passwordFile.get(i)));
		}
		
		return pwds;
	}

	@Override
	public int[] getParameters(List<String> passwordFile) {
		/*
		 * In order to separate the return value in the case that no parameters have
		 * been read in we return null if the array is still of length 0 at this point
		 */
		return this.parameters.length!=0 ? this.parameters : null;
	}

}
