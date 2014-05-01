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

import main.passwords.PasswordType;
import parsing.PasswordFileParser.PasswordFileParserI;

/**
 * This class provides the parser functionality for text passwords.
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
public class TextPasswordFileParser implements PasswordFileParserI<String> {
	
	@Override
	public boolean checkWhetherOfThisType(List<String> passwordFile) {
		
		/*
		 * Explicit declaration: If the file was explicitly tagged we can assume soundness (as the naive little fellows we are)
		 */
		if ( passwordFile.get(0).equalsIgnoreCase(PasswordFileParser.PWT_PREFIX+PasswordType.TEXT.toString()) ) {
			return true;
		}
		
		/*
		 * Heuristic determination: Text passwords can be basically anything.
		 * They do not need any parameters and follow no structure. Imposing 
		 * any restriction of their formatting would be somewhat arbitrary. 
		 * Therefore, we simply nod every file through. While not much of an
		 * actual heuristic, it still seems to be the soundest choice.
		 */
		return true;
		
	}

	@Override
	public List<String> getPasswords(List<String> passwordFile) {
		
		/*
		 * The only thing we possibly need to do is remove the first element if the
		 * file is explicitly tagged.
		 */
		if ( passwordFile.get(0).equalsIgnoreCase(PasswordFileParser.PWT_PREFIX+PasswordType.TEXT.toString()) ) {
			List<String> newList=new ArrayList<String>(passwordFile);
			newList.remove(0);
			return newList;
		}
		
		return passwordFile;
		
	}

	@Override
	public PasswordType getPasswordType() {
		return PasswordType.TEXT;
	}

	@Override
	public int[] getParameters(List<String> passwordFile) {
		//text passwords don't have any parameters, so we return an array with length 0
		return new int[0];
	}

}
