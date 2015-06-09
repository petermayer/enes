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
