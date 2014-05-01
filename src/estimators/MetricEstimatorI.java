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
