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
 * for passwords with dependent click-points (i.e. created all on the same image). Such
 * passwords are used for example in the PassPoints system.
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 * @see DirikEstimator
 * @see DirikEstimatorIndep
 */
public class DirikEstimatorDep extends MetricEstimatorI<ClickPassword> {

	/**
	 * The actual estimator all calculations are delegated to
	 */
	private DirikEstimator estimator=new DirikEstimator();
	
	@Override
	public double calculateMetric(List<ClickPassword> passwords, int[] parameters) {
		return this.estimator.calculateEstimate(passwords, parameters, true);
	}

	@Override
	public void printResult(Writer outWriter) throws IOException {
		this.estimator.printResult(outWriter, true);
	}

	@Override
	public PasswordType getPasswordType() {
		return PasswordType.GRAPHICAL_CLICK;
	}

}
