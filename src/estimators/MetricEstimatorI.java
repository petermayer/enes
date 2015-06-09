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

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import main.passwords.PasswordType;

/**
 * Interface for the different metrics' estimators.
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
public abstract class MetricEstimatorI<E> {
	
	/**
	 * Calculates the metric for the specified passwords. The return value is 
	 * specific for each estimator and serves mainly the purpose to indicate
	 * successful computation (non-null value).
	 * 
	 * @param passwords The passwords to consider
	 * @return The calculated metric
	 */
	public abstract Object calculateMetric(List<E> passwords, int[] parameters);
	
	/**
	 * Prints the result of the metric estimator in verbose mode
	 * 
	 * @param outWriter The sink to write the results to
	 */
	public abstract void verbosePrintResult(Writer outWriter) throws IOException;
	
	/**
	 * Prints the result of the metric estimator as a short summary
	 * 
	 * @param outWriter
	 * @throws IOException
	 */
	public abstract void shortPrintRestult(Writer outWriter) throws IOException;
	
	/**
	 * Returns the type of password this estimator is designed for.
	 * 
	 * @return The PasswordType, this estimator is designed for
	 */
	public abstract PasswordType getPasswordType();
	
}
