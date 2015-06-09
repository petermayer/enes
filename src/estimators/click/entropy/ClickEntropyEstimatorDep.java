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
package estimators.click.entropy;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import main.passwords.ClickPassword;
import main.passwords.PasswordType;
import estimators.MetricEstimatorI;
/**
 * This class provides a wrapper for the estimation method implemented in DirikEstimator
 * for passwords with dependent click-points (i.e. created all on the same image). Such
 * passwords are used for example in the PassPoints system.
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 * @see ClickEntropyEstimator
 * @see ClickEntropyEstimatorIndep
 */
public class ClickEntropyEstimatorDep extends MetricEstimatorI<ClickPassword> {

	/**
	 * The actual estimator all calculations are delegated to
	 */
	private ClickEntropyEstimator estimator=new ClickEntropyEstimator();
	
	@Override
	public Object calculateMetric(List<ClickPassword> passwords, int[] parameters) {
		return this.estimator.calculateEstimate(passwords, parameters, true);
	}

	@Override
	public void verbosePrintResult(Writer outWriter) throws IOException {
		this.estimator.printResult(outWriter, true);
	}

	@Override
	public PasswordType getPasswordType() {
		return PasswordType.GRAPHICAL_CLICK;
	}

	@Override
	public void shortPrintRestult(Writer outWriter) throws IOException {
		this.estimator.printSummary(outWriter, true);
	}

}
