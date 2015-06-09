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
package estimators;

/**
 * This class provides functions for all guesswork estimators.
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
public class GuessworkHelper {

	/**
	 * This function calculates the beta-success-rate from a password distribution.
	 * 
	 * @param X The password distribution
	 * @param beta The amount of guesses the attacker is limited to
	 * @return The beta-success-rate
	 */
	public static double betaSuccessRate(double[] X, int beta) {

		double lambda=0;

		for ( int i=0; i<beta; i++ ) lambda += X[i];

		return lambda;
	}

	/**
	 * This function calculates the alpha-work-factor from a password distribution.
	 * 
	 * @param X The password distribution
	 * @param alpha The desired proportion of broken accounts
	 * @return The alpha-work-factor
	 */
	public static int alphaWorkFactor(double[] X, double alpha) {

		for ( int j=0; j<X.length; j++ ) {

			double tempP=0;

			for ( int i=0; i<=j; i++ ) tempP += X[i];

			if ( tempP >= alpha ) return j+1;

		}

		return -1;
	}

	/**
	 * This function calculates the alpha-guesswork metric from a password distribution.
	 * 
	 * @param X The password distribution
	 * @param alpha The desired proportion of broken accounts
	 * @return The alpha-guesswork metric
	 */
	public static double alphaGuesswork(double[] X, double alpha) {

		double first=(1-betaSuccessRate(X, alphaWorkFactor(X, alpha)))*alphaWorkFactor(X, alpha);

		double second=0;

		for ( int i=0; i<=alphaWorkFactor(X, alpha); i++ ) second += (i*X[i]);

		return first+second;

	}

	/**
	 * This function calculates the alpha-guesswork metric from a password distribution
	 * in bits.
	 * 
	 * @param X The password distribution
	 * @param alpha The desired proportion of broken accounts
	 * @return The alpha-guesswork metric in bits
	 */
	public static double alphaGuessworkBits(double[] X, double alpha) {

		return CommonHelper.log2(2*alphaGuesswork(X, alpha)/betaSuccessRate(X, alphaWorkFactor(X, alpha))-1)+CommonHelper.log2(1/(2-betaSuccessRate(X, alphaWorkFactor(X, alpha))));

	}

}
