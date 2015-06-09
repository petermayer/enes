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
package estimators.cognometricgrouped.entropy;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import main.passwords.CognometricGroupedPassword;
import main.passwords.PasswordType;
import estimators.MetricEstimatorI;
import estimators.ShannonEntropyHelper;

/**
 * This class provides an estimator for calculating the Shannon entropy of
 * cognometric grouped password samples. Both, estimates for the group 
 * distribution and the distribution in the groups is calculated. Overall
 * values are however only calculated for the distribution of the password
 * elements (in the groups) since the distribution of the groups does not
 * add to the strength of the password against offline guessing attacks.
 * 
 * @author Peter Mayer | peter.mayer@cased.de
 */
public class CognometricGroupedEntropyEstimator extends MetricEstimatorI<CognometricGroupedPassword> {

	private double[] groupEntropies=new double[0];
	private double[] elementEntropies=new double[0];

	@Override
	public Object calculateMetric(List<CognometricGroupedPassword> passwords, int[] parameters) {

		/*
		 * First we create the buckets for each group and each element in the groups
		 */
		int maxLength=CognometricGroupedPassword.getMaxLength(passwords);
		int[][] groupBuckets=new int[maxLength][parameters[0]];
		int[][] elementBuckets=new int[maxLength][parameters[1]];

		/*
		 * Then we iterate over all the passwords
		 */
		for ( int i=0; i<passwords.size(); i++ ) {
			//and for each password over all elements
			for ( int j=0; j<passwords.get(i).length(); j++ ) {

				//sort in the group
				groupBuckets[j][passwords.get(i).getElement(j)[0]]++;

				//sort in the element
				elementBuckets[j][passwords.get(i).getElement(j)[1]]++;

			}
		}


		/*
		 * Now we calculate the entropy for the groups and the elements
		 */
		this.groupEntropies=new double[maxLength];
		this.elementEntropies=new double[maxLength];

		for ( int i=0; i<maxLength; i++ ) {
			this.groupEntropies[i]=ShannonEntropyHelper.getEntropy(groupBuckets[i]);
			this.elementEntropies[i]=ShannonEntropyHelper.getEntropy(elementBuckets[i]);
		}

		//TODO These return values need to be replaced
		return new double[][]{this.groupEntropies,this.elementEntropies};
	}

	@Override
	public void verbosePrintResult(Writer outWriter) throws IOException {

		double overall=0;

		for ( int i=0; i<this.groupEntropies.length; i++ ) {
			outWriter.write("Element position: "+(i+1)+"\n");
			outWriter.write("Position entropy (groups): "+this.groupEntropies[i]+"\n");
			outWriter.write("Position entropy (elements): "+this.elementEntropies[i]+"\n");
			overall += this.elementEntropies[i];
			outWriter.write("-------------------------------\n");
		}
		
		outWriter.write("Overall element entropy: "+overall+"\n");
		outWriter.close();
	}

	@Override
	public void shortPrintRestult(Writer outWriter) throws IOException {

		double overall=0;

		for ( int i=0; i<this.groupEntropies.length; i++ ) {
			overall += this.elementEntropies[i];
		}
		
		outWriter.write("Overall element entropy: "+overall+"\n");
		outWriter.close();
	}

	@Override
	public PasswordType getPasswordType() {
		return PasswordType.GRAPHICAL_COGNOMETRIC_GROUP;
	}

}
