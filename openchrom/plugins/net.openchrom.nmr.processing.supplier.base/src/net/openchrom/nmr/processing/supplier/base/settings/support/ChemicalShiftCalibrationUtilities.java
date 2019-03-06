/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Alexander Stark - initial API and implementation
 *******************************************************************************/
package net.openchrom.nmr.processing.supplier.base.settings.support;

import java.util.Collection;

import org.eclipse.chemclipse.nmr.model.core.IMeasurementNMR;
import org.eclipse.chemclipse.nmr.model.selection.DataNMRSelection;
import org.eclipse.chemclipse.nmr.model.selection.IDataNMRSelection;
import org.eclipse.chemclipse.nmr.model.support.ISignalExtractor;
import org.eclipse.chemclipse.nmr.model.support.SignalExtractor;
import org.ejml.simple.SimpleMatrix;

import net.openchrom.nmr.processing.supplier.base.core.UtilityFunctions;
import net.openchrom.nmr.processing.supplier.base.settings.IcoShiftAlignmentSettings;
import net.openchrom.nmr.processing.supplier.base.settings.support.IcoShiftAlignmentUtilities.Interval;

public class ChemicalShiftCalibrationUtilities {

	public static boolean isSamePeakPosition(int[] actualPositions, int intendedPosition) {

		for(int p : actualPositions) {
			if(p != intendedPosition) {
				return false;
			}
		}
		return true;
	}

	public static double[] getChemicalShiftAxis(Collection<? extends IMeasurementNMR> experimentalDatasetsList) {

		IMeasurementNMR measurementNMR = experimentalDatasetsList.iterator().next();
		IDataNMRSelection dataNMRSelection = new DataNMRSelection(measurementNMR);
		ISignalExtractor signalExtractor = new SignalExtractor(dataNMRSelection);
		return signalExtractor.extractChemicalShift();
	}

	public static int[] getActualPeakPositions(Interval<Integer> intervalIndices, SimpleMatrix calibratedData) {

		UtilityFunctions utilityFunction = new UtilityFunctions();
		int numRowsMax = calibratedData.numRows();
		int[] actualPositions = new int[numRowsMax];
		for(int r = 0; r < numRowsMax; r++) {
			double[] rowVector = calibratedData.extractVector(true, r).getMatrix().getData();
			double[] rowVectorPart = new double[intervalIndices.getStart() - intervalIndices.getStop()];
			//
			System.arraycopy(rowVector, intervalIndices.getStop(), rowVectorPart, 0, intervalIndices.getStart() - intervalIndices.getStop());
			double maxValue = utilityFunction.getMaxValueOfArray(rowVectorPart);
			actualPositions[r] = utilityFunction.findIndexOfValue(rowVectorPart, maxValue);
		}
		return actualPositions;
	}

	public static int getIntendedPeakPosition(Interval<Integer> intervalIndices, double[] chemicalShiftAxis) {

		UtilityFunctions utilityFunction = new UtilityFunctions();
		double[] chemicalShiftAxisPart = new double[intervalIndices.getStart() - intervalIndices.getStop()];
		System.arraycopy(chemicalShiftAxis, intervalIndices.getStop(), chemicalShiftAxisPart, 0, intervalIndices.getStart() - intervalIndices.getStop());
		return utilityFunction.findIndexOfValue(chemicalShiftAxisPart, 0);
	}

	public static Interval<Integer> getCalibrationIntervalIndices(double[] chemicalShiftAxis, IcoShiftAlignmentSettings calibrationSettings) {

		UtilityFunctions utilityFunction = new UtilityFunctions();
		IcoShiftAlignmentUtilities shiftUtils = new IcoShiftAlignmentUtilities();
		// int[] intervalIndices = new int[2];
		double leftMargin = calibrationSettings.getSinglePeakHigherBorder();
		double rigthMargin = calibrationSettings.getSinglePeakLowerBorder();
		int leftIndex = utilityFunction.findIndexOfValue(chemicalShiftAxis, leftMargin);
		int rightIndex = utilityFunction.findIndexOfValue(chemicalShiftAxis, rigthMargin);
		return shiftUtils.new Interval<Integer>(leftIndex, rightIndex);
	}
}
