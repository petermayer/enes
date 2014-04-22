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

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import main.PasswordType;
import estimators.MetricEstimatorI;
import estimators.click.ClickPassword;

/**
 * 
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
public class ChiassonEstimator extends MetricEstimatorI<ClickPassword> {

	@Override
	public Object calculateMetric(List<ClickPassword> passwords, int[] parameters) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void verbosePrintResult(Writer outWriter) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PasswordType getPasswordType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void shortPrintRestult(Writer outWriter) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
