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
package parsing;

import java.util.ArrayList;
import java.util.List;

import main.passwords.ClickPassword;
import main.passwords.PasswordType;
import parsing.PasswordFileParser.PasswordFileParserI;

/**
 * This class provides functions for parsing password files of graphical 
 * click-based passwords.The password file is expected to be UTF-8 encoded 
 * and to contain one password in each line, where the format is: 
 * "x1,y2;x2,y2;...;xn,yn" (without quotation marks) for n click-points. 
 * Currently only integer values are supported for the coordinates. The 
 * first line has to contain three values, the maximum value for the 
 * x-coordinates, the maximum value for the y-coordinates and the tolerance 
 * margin, where the format is: "xmax,ymax,tolerance" (without quotation 
 * marks).
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
public class GraphicalClickPasswordFileParser implements PasswordFileParserI<ClickPassword> {

	/**
	 * The parameters as read from the password file.
	 * 
	 *  0: x-max
	 *  1: y-max
	 *  2: tolerance
	 */
	private int[] parameters;
	
	@Override
	public PasswordType getPasswordType() {
		return PasswordType.GRAPHICAL_CLICK;
	}

	@Override
	public boolean checkWhetherOfThisType(List<String> passwordFile) {

		//We always need to look at the first line of the file
		String firstLine=passwordFile.get(0);

		/*
		 * Explicit declaration: If the file was explicitly tagged we can assume soundness (as the naive little fellows we are)
		 */
		if ( firstLine.equalsIgnoreCase(PasswordFileParser.PWT_PREFIX+PasswordType.GRAPHICAL_CLICK.toString()) ) {
			return true;
		}

		/*
		 * Heuristic determination: Graphical click passwords need in the 
		 * first line specified the parameters xmax, ymax and tolerance. All
		 * of these values need to be present (and no others) and grater 
		 * than 0. The tolerance margin can not be greater than the canvas.
		 * The passwords need to be parsable by "parseClickPassword" and they
		 * must lie within the canvas. Currently only the first password is
		 * checked at this point.
		 */
		try {
			//if the type is actually correct we can store the parameters directly, if not we will set them to null
			this.parameters=this.arrayStringToInt(firstLine.split(","));

			//do the parameters by themselves make sense?
			if ( this.parameters.length != 3 ||
					this.parameters[0] <= 0 ||
					this.parameters[1] <= 0 ||
					this.parameters[2] <= 0 ||
					this.parameters[2] > this.parameters[0] ||
					this.parameters[2] > this.parameters[1] ) {
				this.parameters=null;
				return false;
			}

			//do the parameters make sense in context of the passwords?
			//(only first password is actually checked, possibility to move whole parsing here left open)
			for ( int i=1; i<2; i++) {
				ClickPassword pw=this.parseClickPassword(passwordFile.get(i));

				//iteration over all click-points
				for ( int j=0; j<pw.length(); j++ ) {
					int[] cp=pw.getClickPoint(j);
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

		//Seems the password file is not for graphical click passwords
		return false;
	}

	@Override
	public List<ClickPassword> getPasswords(List<String> passwordFile) {
		
		List<ClickPassword> pwds=new ArrayList<ClickPassword>();
		
		//We start at 1 because we need to skip the parameters
		int startIndex=1;
		
		//If there is a tag at the beginning of the file, we need to skip that as well
		if ( passwordFile.get(0).equalsIgnoreCase(PasswordFileParser.PWT_PREFIX+PasswordType.GRAPHICAL_CLICK.toString()) ) {
			startIndex=2;
		}
		
		for ( int i=startIndex; i<passwordFile.size(); i++ ) {
			pwds.add(parseClickPassword(passwordFile.get(i)));
		}
		
		return pwds;
	}
	
	@Override
	public int[] getParameters(List<String> passwordFile) {
		/* 
		 * What we do here is not exactly best practice, but we extracted the parameters
		 * during the validity check and just return the cached values. Luckily PasswordFileParser
		 * assures that this is sound. 
		 */
		return this.parameters;
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

	/**
	 * Parses one line of the password file to extract the password information
	 * 
	 * @param pwd The password as specified in the file
	 * @return The password in a representation as needed for the entropy estimation
	 */
	private ClickPassword parseClickPassword(String pwd) {
		
		ClickPassword cpw=new ClickPassword();
		
		for ( String cp : pwd.split(";") ) {
			cpw.addClickPoint(arrayStringToInt(cp.split(",")));
		}
		
		return cpw;	
	}
	
}
