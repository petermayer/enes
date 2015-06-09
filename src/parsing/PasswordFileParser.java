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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import main.passwords.PasswordType;

/**
 * This class provides parsing functionality for the different kinds of password
 * lists. This class's only direct responsibility is reading the password file. 
 * The actual parsing of the read information is delegated to specific parsers. 
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
public final class PasswordFileParser {
	
	/**
	 * The prefix that identifies a type tag in the password file. Such  a tag 
	 * needs always to be placed in the first line.
	 */
	static final String PWT_PREFIX="password type:";
	
	/**
	 * In this list, the contents from the supplied password file are stored.
	 */
	private final List<String> passwordFile;
	
	/**
	 * The type of passwords currently being processed (has to be set prior to usage)
	 */
	private PasswordType type=null;
	
	/**
	 * The adequate parser, as determined during setting the password type
	 */
	private PasswordFileParserI<?> parser;
	
	/**
	 * The list of available parsers. All parsers need to added here to be 
	 * automatically used. 
	 */
	private PasswordFileParserI<?>[] parsers=new PasswordFileParserI[]{
			new GraphicalClickPasswordFileParser(),
			new TextPasswordFileParser()};
	
	/**
	 * Constructor of the Parser. The password file is read during instantiation
	 * of this class.
	 * 
	 * @param filePath The path to the password file
	 * @throws IOException Is thrown if the file path is incorrect or the file cannot be accessed.
	 */
	public PasswordFileParser(String filePath) throws IOException {
		this.passwordFile=this.readPasswordFile(filePath);
	}
	
	/**
	 * Delegate method actually responsible for reading the contents of the
	 * password file
	 * 
	 * @param filePath The path to the password file 
	 * @return The contents of the password file as list, where each list item corresponds to one line in the file
	 * @throws IOException Is thrown if the file path is incorrect or the file cannot be accessed.
	 */
	private List<String> readPasswordFile(String filePath) throws IOException {
		List<String> pwds=new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF8"));
		String line;
		while ( (line=reader.readLine()) != null ) {
			pwds.add(line);
		}
		reader.close();

		return pwds;
	}

	/**
	 * This attempts to set the password type for the later processing and 
	 * selects the right parser. This will only succeed if the supplied
	 * file actually is compatible with the specified password type (as
	 * determined by the respective parser).
	 * 
	 * @param type The type of passwords in the supplied password file
	 * @return Whether setting the type was successful
	 */
	public boolean setPasswordType(PasswordType type) {
		
		//check whether the expected type has already been set
		if ( this.type == type ) return true;
		
		//check whether the new expected type conflicts with a previously set type
		if ( this.type != null && this.type != type ) return false;
		
		//check whether the type is sensible for the provided password file
		for ( PasswordFileParserI<?> p : this.parsers ) {
			if ( type == p.getPasswordType() ) {
				
				//if yes, we found the right parser
				if (p.checkWhetherOfThisType(passwordFile)) {
					boolean typeOK=this.setPwType(p.getPasswordType());
					if ( !typeOK ) return false;
					this.parser=p;
					return true;
				}
			}
		}
		
		//Something seriously strange must have happened if we end up here and normally we wont
		return false;
		
	}
	
	/**
	 * Internal method used to set the internal password type field. The
	 * method also checks whether the type was set before and returns
	 * false if a conflict is encountered.
	 * 
	 * @param type The type to be set
	 * @return Whether the type could actually be set
	 */
	private boolean setPwType(PasswordType type) {
		
		if ( this.type != null && this.type != type  )  {
			this.type = PasswordType.MALFORMATTED;
			return false;
		}
		
		this.type = type;
		return true;
	}
	
	/**
	 * This method returns the passwords as provided by the respective 
	 * delegate parser
	 * 
	 * @return The list of passwords
	 */
	public List<?> getPasswords() {
		return this.parser.getPasswords(this.passwordFile);
	}
	
	/**
	 * This method returns the parameters as provided by the respective
	 * delegate parser
	 * 
	 * @return The array of parameters
	 */
	public int[] getParameters() {
		return this.parser.getParameters(passwordFile);
	}
	
	/**
	 * This interface is used to encapsulate the code for parsing specific types
	 * of password files. Each type should implement a class with this interface
	 * and hook it into PasswordListParser.
	 * 
	 * The password file is handed to the parser as a List of Strings, where each
	 * list element represents a line in the file.  
	 * 
	 * @author Peter Mayer | peter.mayer@cased.de
	 */
	interface PasswordFileParserI<E> {

		/**
		 * The parser has to declare the password type it handles per this method.
		 * 
		 * @return The handled password type
		 */
		public PasswordType getPasswordType();
		
		/**
		 * This function is called in order to check whether the  supplied password
		 * file is contains information applicable to this type.
		 * 
		 * @return Whether the password file is of the type specified by the parser
		 */
		public boolean checkWhetherOfThisType(List<String> passwordFile);
		
		
		/**
		 * This function is called in order to actually obtain the list with all
		 * passwords as specified in the file. Only the parsing functionality needs
		 * to be implemented in this file. Sanity checks (per functions above) are
		 * performed in PasswordParser. The returned Objects in the List can be
		 * freely chosen to be appropriate for the estimators of this password type.
		 *  
		 * @return A list with all the passwords in the file 
		 */
		public List<E> getPasswords(List<String> passwordFile);
		
		
		/**
		 * This function should return the configuration parameters specific for
		 * the type of password. Currently only numeric (integer) parameters are 
		 * supported.
		 * 
		 * @return The parameters for the password set 
		 */
		public int[] getParameters(List<String> passwordFile);
		
	}

}
