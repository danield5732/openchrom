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
package net.openchrom.nmr.processing.supplier.base.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.complex.Complex;
import org.eclipse.chemclipse.model.core.IMeasurement;
import org.eclipse.chemclipse.model.filter.IMeasurementFilter;
import org.eclipse.chemclipse.nmr.model.core.FilteredSpectrumMeasurement;
import org.eclipse.chemclipse.nmr.model.core.SimpleNMRSignal;
import org.eclipse.chemclipse.nmr.model.core.SpectrumMeasurement;
import org.eclipse.chemclipse.processing.core.MessageConsumer;
import org.eclipse.chemclipse.processing.filter.Filter;
import org.eclipse.chemclipse.processing.filter.FilterChain;
import org.eclipse.core.runtime.IProgressMonitor;
import org.ejml.simple.SimpleMatrix;
import org.osgi.service.component.annotations.Component;

import net.openchrom.nmr.processing.supplier.base.core.UtilityFunctions.SpectrumData;
import net.openchrom.nmr.processing.supplier.base.settings.ChemicalShiftCalibrationSettings;
import net.openchrom.nmr.processing.supplier.base.settings.IcoShiftAlignmentSettings;
import net.openchrom.nmr.processing.supplier.base.settings.support.ChemicalShiftCalibrationTargetCalculation;
import net.openchrom.nmr.processing.supplier.base.settings.support.ChemicalShiftCalibrationUtilities;
import net.openchrom.nmr.processing.supplier.base.settings.support.IcoShiftAlignmentGapFillingType;
import net.openchrom.nmr.processing.supplier.base.settings.support.IcoShiftAlignmentShiftCorrectionType;
import net.openchrom.nmr.processing.supplier.base.settings.support.IcoShiftAlignmentType;
import net.openchrom.nmr.processing.supplier.base.settings.support.IcoShiftAlignmentUtilities.Interval;

@Component(service = {Filter.class, IMeasurementFilter.class})
public class ChemicalShiftCalibration implements IMeasurementFilter<ChemicalShiftCalibrationSettings> {

	@Override
	public String getName() {

		return "Chemical Shift Calibration";
	}

	@Override
	public ChemicalShiftCalibrationSettings createNewConfiguration() {

		return new ChemicalShiftCalibrationSettings();
	}

	@Override
	public Collection<? extends IMeasurement> filterIMeasurements(Collection<? extends IMeasurement> filterItems, ChemicalShiftCalibrationSettings configuration, FilterChain<Collection<? extends IMeasurement>> nextFilter, MessageConsumer messageConsumer, IProgressMonitor monitor) throws IllegalArgumentException {

		if(configuration == null) {
			configuration = createNewConfiguration();
		}
		Collection<SpectrumMeasurement> collection = new ArrayList<>();
		for(IMeasurement measurement : filterItems) {
			if(measurement instanceof SpectrumMeasurement) {
				collection.add((SpectrumMeasurement)measurement);
			} else {
				throw new IllegalArgumentException();
			}
		}
		SimpleMatrix calibrationResult = calibrate(collection, configuration);
		int lengthOfCalibrationResult = calibrationResult.numRows() - 1;
		double[] chemicalShiftAxis = ChemicalShiftCalibrationUtilities.getChemicalShiftAxis(collection);
		List<IMeasurement> results = new ArrayList<>();
		//
		for(SpectrumMeasurement measurement : collection) {
			FilteredSpectrumMeasurement filteredSpectrumMeasurement = new FilteredSpectrumMeasurement(measurement);
			List<SimpleNMRSignal> newSignals = new ArrayList<>();
			double[] dataArray = calibrationResult.extractVector(true, lengthOfCalibrationResult).getMatrix().getData();
			for(int i = 0; i < dataArray.length; i++) {
				newSignals.add(new SimpleNMRSignal(chemicalShiftAxis[i], dataArray[i], 0, null));
				// SimpleNMRSignal(Number chemicalShift, Number real, Number imaginary, BigDecimal scalingFactor)
				// => no imaginary part and no scaling factor
			}
			lengthOfCalibrationResult++;
			filteredSpectrumMeasurement.setSignals(newSignals);
			results.add(filteredSpectrumMeasurement);
		}
		return nextFilter.doFilter(results, messageConsumer);
	}

	@Override
	public boolean acceptsIMeasurement(IMeasurement item) {

		// calibration can be done with one measurement
		return true;
	}

	@Override
	public boolean acceptsIMeasurements(Collection<? extends IMeasurement> items) {

		if(items.size() < 2) {
			return false;
		}
		Collection<SpectrumMeasurement> collection = new ArrayList<>();
		for(IMeasurement measurement : items) {
			if(measurement instanceof SpectrumMeasurement) {
				collection.add((SpectrumMeasurement)measurement);
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * The method calibrate will define the necessary settings and calculate
	 * a target for calibration of the dataset and calibrate the data.
	 * <p>
	 * Commonly used internal standards for calibrating chemical shift:
	 * <ul>
	 * <li>TMS (Tetramethylsilane)</li>
	 *
	 * <li>DSS (4,4-dimethyl-4-silapentane-1-sulfonic acid)</li>
	 *
	 * <li>TSP (3-(trimethylsilyl)propionic acid, sodium salt)</li>
	 * <ul>
	 * <li>The chemical shift of the (main) singlet of each standard is
	 * assigned as 0 ppm.</li>
	 * </ul>
	 * </ul>
	 *
	 * @author Alexander Stark
	 */
	public SimpleMatrix calibrate(Collection<? extends SpectrumMeasurement> experimentalDatasetsList, ChemicalShiftCalibrationSettings calibrationSettings) {

		IcoShiftAlignmentSettings alignmentSettings = generateAlignmentSettings();
		IcoShiftAlignment icoShiftAlignment = new IcoShiftAlignment();
		if(!checkSettingsForPeakPosition(alignmentSettings, calibrationSettings)) {
			throw new IllegalArgumentException("Peak Position in calibration settings and alignment settings does not match.");
		}
		// set calibration target in IcoShift algorithm
		icoShiftAlignment.setCalculateCalibrationTargetFunction(new ChemicalShiftCalibrationTargetCalculation());
		icoShiftAlignment.setCalibrationSettings(calibrationSettings);
		SimpleMatrix calibratedData = icoShiftAlignment.process(experimentalDatasetsList, alignmentSettings);
		//
		double[] chemicalShiftAxis = ChemicalShiftCalibrationUtilities.getChemicalShiftAxis(experimentalDatasetsList);
		Collection<? extends SpectrumMeasurement> newDatasetsList = copyPartlyCalibratedData(experimentalDatasetsList, calibratedData);
		int checkIterator = 0;
		while(!checkCalibration(calibratedData, chemicalShiftAxis, alignmentSettings)) { // check for quality of calibration
			newDatasetsList = copyPartlyCalibratedData(newDatasetsList, calibratedData);
			// try to calibrate datasets again
			calibratedData = icoShiftAlignment.process(newDatasetsList, alignmentSettings);
			checkIterator++;
			if(checkIterator == 5) {
				break;
			}
		}
		//
		if(checkIterator > 2) {
			calibratedData = finalPeakCalibration(calibratedData, chemicalShiftAxis, alignmentSettings);
		}
		//
		return calibratedData;
	}

	private static boolean checkSettingsForPeakPosition(IcoShiftAlignmentSettings alignmentSettings, ChemicalShiftCalibrationSettings calibrationSettings) {

		double highBorder = alignmentSettings.getSinglePeakHigherBorder();
		double lowBorder = alignmentSettings.getSinglePeakLowerBorder();
		double alignmentPeakPosition = (highBorder + lowBorder) / 2;
		double calibrationPeakPosition = calibrationSettings.getLocationOfCauchyDistribution();
		if(Double.compare(alignmentPeakPosition, calibrationPeakPosition) == 0) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean checkCalibration(SimpleMatrix calibratedData, double[] chemicalShiftAxis, IcoShiftAlignmentSettings alignmentSettings) {

		Interval<Integer> intervalIndices = ChemicalShiftCalibrationUtilities.getCalibrationIntervalIndices(chemicalShiftAxis, alignmentSettings);
		int intendedPosition = ChemicalShiftCalibrationUtilities.getIntendedPeakPosition(intervalIndices, chemicalShiftAxis);
		int[] actualPositions = ChemicalShiftCalibrationUtilities.getActualPeakPositions(intervalIndices, calibratedData);
		return ChemicalShiftCalibrationUtilities.isSamePeakPosition(actualPositions, intendedPosition);
	}

	private static Collection<? extends SpectrumMeasurement> copyPartlyCalibratedData(Collection<? extends SpectrumMeasurement> experimentalDatasetsList, SimpleMatrix calibratedData) {

		Collection<SpectrumMeasurement> result = new ArrayList<>();
		int r = 0;
		for(SpectrumMeasurement measurementNMR : experimentalDatasetsList) {
			SpectrumData complexSpectrumData = UtilityFunctions.toComplexSpectrumData(measurementNMR.getSignals());
			double[] rowVector = calibratedData.extractVector(true, r).getMatrix().getData();
			r++;
			List<ComplexSpectrumSignal> newSignals = new ArrayList<>();
			for(int c = 0; c < rowVector.length; c++) {
				newSignals.add(new ComplexSpectrumSignal(complexSpectrumData.chemicalShift[c], new Complex(rowVector[c], 0)));
			}
			FilteredSpectrumMeasurement filtered = new FilteredSpectrumMeasurement(measurementNMR);
			filtered.setSignals(newSignals);
			result.add(filtered);
		}
		return result;
	}

	private static SimpleMatrix finalPeakCalibration(SimpleMatrix calibratedData, double[] chemicalShiftAxis, IcoShiftAlignmentSettings alignmentSettings) {

		Interval<Integer> intervalIndices = ChemicalShiftCalibrationUtilities.getCalibrationIntervalIndices(chemicalShiftAxis, alignmentSettings);
		int intendedPosition = ChemicalShiftCalibrationUtilities.getIntendedPeakPosition(intervalIndices, chemicalShiftAxis);
		int[] actualPositions = ChemicalShiftCalibrationUtilities.getActualPeakPositions(intervalIndices, calibratedData);
		//
		UtilityFunctions utilityFunction = new UtilityFunctions();
		// try to correct the remaining discrepancy
		for(int i = 0; i < actualPositions.length; i++) {
			double[] shiftVector = calibratedData.extractVector(true, i).getMatrix().getData();
			//
			if(actualPositions[i] > intendedPosition) {
				// leftShift
				utilityFunction.leftShiftNMRData(shiftVector, (actualPositions[i] - intendedPosition));
				calibratedData.setRow(i, 0, shiftVector);
			} else {
				// rightShift
				utilityFunction.rightShiftNMRData(shiftVector, (intendedPosition - actualPositions[i]));
				calibratedData.setRow(i, 0, shiftVector);
			}
		}
		return calibratedData;
	}

	/**
	 * This method will generate a separate instance of alignment settings
	 * used for a calibration of datasets.
	 * <p>
	 * <p>
	 * All settings are fixed for the calculation of the target. The peak of
	 * a commonly used internal standard for calibration (assigned as 0 ppm)
	 * is predefined.
	 * <p>
	 * If needed the range for a user defined peak can be defined by setting:
	 * <br>
	 * {@link calibrationSettings.setSinglePeakLowerBorder()}<br>
	 * and <br>
	 * {@link calibrationSettings.setSinglePeakHigherBorder()}.
	 * <p>
	 * The selected region should not be too wide to ensure that only the peak
	 * of interest is used for the calibration.
	 *
	 * @author Alexander Stark
	 *
	 */
	private static IcoShiftAlignmentSettings generateAlignmentSettings() {

		IcoShiftAlignmentSettings alignmentSettings = new IcoShiftAlignmentSettings();
		//
		alignmentSettings.setShiftCorrectionType(IcoShiftAlignmentShiftCorrectionType.BEST);
		//
		alignmentSettings.setAlignmentType(IcoShiftAlignmentType.SINGLE_PEAK);
		alignmentSettings.setSinglePeakLowerBorder(0.05);// changed from -0.05
		alignmentSettings.setSinglePeakHigherBorder(-0.05);// changed from 0.05
		//
		alignmentSettings.setGapFillingType(IcoShiftAlignmentGapFillingType.MARGIN);
		return alignmentSettings;
	}
}
