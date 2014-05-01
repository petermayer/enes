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
