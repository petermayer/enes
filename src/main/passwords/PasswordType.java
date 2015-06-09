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
package main.passwords;

/**
 * This enum contains all the different types of passwords, EnEs can handle.
 * These types are used to select parsers during run-time and determine
 * compatibility with the user-specified estimator.
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
public enum PasswordType {
	
	/*
	 * The test password type
	 */
	TEXT,
	
	/*
	 * The type for graphical click-based passwords (as used in e.g. PassPoints)
	 */
	GRAPHICAL_CLICK,
	
	/*
	 * The type for recognition-based graphical passwords which are semantically grouped (as used in e.g. Passfaces)
	 */
	GRAPHICAL_COGNOMETRIC_GROUP,
	
	/*
	 * This type is used by the parser in case a password file can not be parsed correctly
	 */
	MALFORMATTED
}
