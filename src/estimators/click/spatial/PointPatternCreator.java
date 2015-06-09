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
package estimators.click.spatial;

import main.passwords.ClickPassword;

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
