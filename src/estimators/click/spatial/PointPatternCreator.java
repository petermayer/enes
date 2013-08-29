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
package estimators.click.spatial;

import estimators.click.ClickPassword;

/**
 * This class provides a factory of the point pattern type ppp for use with the R package
 * spatstat. At instantiation time a password set is loaded into the factory and 
 * from this set PointPatterns for any position in the password or all positions can 
 * be created
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 *
 */
public class PointPatternCreator {
	
	/**
	 * The maximum x value
	 */
	private final int x;
	
	/**
	 * The maximum y value
	 */
	private final int y;
	
	/**
	 * The passwords to create the pattern from
	 */
	private final ClickPassword[] pwds;
	
	/**
	 * Creates a new factory instance
	 * 
	 * @param passwords The password set
	 * @param x The maximum value for x
	 * @param y The maximum value for y
	 */
	public PointPatternCreator(ClickPassword[] passwords, int x, int y) {
		
		this.x=x;
		this.y=y;
		this.pwds=passwords;
		
	}
	
	/**
	 * Creates a ppp type from the password set for the specified position or for all positions
	 * if 0 is specified as position.
	 * 
	 * @param position The position to create the ppp
	 * @return The ppp as String
	 */
	public String getPointPattern(final int position) {
		
		String x="";
		String y="";
		
		for ( ClickPassword pwd : this.pwds ) {
			for ( int i=(position==0)?0:position; i<((position==0)?pwd.length():(position+1)); i++ ) {
				x += pwd.getClickPoint(i)[0];
				y += pwd.getClickPoint(i)[1];
				
				if ( i<((position==0)?(pwd.length()-1):position) ) {
					x += ",";
					y += ",";
				}
			}
		}
		
		return "ppp(c("+x+"), c("+y+"), c(1,"+this.x+"), c(1,"+this.y+"))";
	}

}
