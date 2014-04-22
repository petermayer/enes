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
package estimators.click.dirik;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import main.PasswordType;
import estimators.MetricEstimatorI;
import estimators.click.ClickPassword;
/**
 * This class provides a wrapper for the estimation method implemented in DirikEstimator
 * for passwords with independent click-points (i.e. created on different images). Such
 * passwords are used for example in the PCCP system.
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 * @see DirikEstimator
 * @see DirikEstimatorDep
 */
public class DirikEstimatorIndep extends MetricEstimatorI<ClickPassword> {

	/**
	 * The actual estimator all calculations are delegated to
	 */
	private DirikEstimator estimator=new DirikEstimator();
	
	@Override
	public double calculateMetric(List<ClickPassword> passwords, int[] parameters) {
		return this.estimator.calculateEstimate(passwords, parameters, false);
	}

	@Override
	public void printResult(Writer outWriter) throws IOException {
		this.estimator.printResult(outWriter, false);
	}

	@Override
	public PasswordType getPasswordType() {
		return PasswordType.GRAPHICAL_CLICK;
	}

}
