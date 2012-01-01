/*******************************************************************************
 * Copyright (c) 2008, 2012 Philip (eselmeister) Wenig.
 * 
 * All rights reserved.
 *******************************************************************************/
package net.openchrom.chromatogram.msd.comparison.supplier.pbm.comparator;

import net.openchrom.chromatogram.msd.comparison.exceptions.NoMassSpectrumComparisonResultAvailableException;
import net.openchrom.chromatogram.msd.comparison.spectrum.IMassSpectrumComparisonResult;
import net.openchrom.chromatogram.msd.comparison.spectrum.MassSpectrumComparator;
import net.openchrom.chromatogram.msd.model.core.IMassSpectrum;
import net.openchrom.chromatogram.msd.model.implementation.DefaultMassSpectrum;
import net.openchrom.chromatogram.msd.model.xic.IIonRange;
import net.openchrom.chromatogram.msd.model.xic.IonRange;
import junit.framework.TestCase;

public class PBMMassSpectrumComparator_2_Test extends TestCase {

	@SuppressWarnings("unused")
	private IMassSpectrumComparisonResult result;
	private IMassSpectrum unknown;
	private IMassSpectrum reference;
	private IIonRange ionRange;

	@Override
	protected void setUp() throws Exception {

		unknown = new DefaultMassSpectrum();
		reference = new DefaultMassSpectrum();
		ionRange = new IonRange(20, 120);
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {

		unknown = null;
		reference = null;
		ionRange = null;
		result = null;
		super.tearDown();
	}

	public void testMassSpectrumComparatorCompare_1() {

		try {
			result = MassSpectrumComparator.compare(null, null, null, PBMMassSpectrumComparator.COMPARATOR_ID);
		} catch(NoMassSpectrumComparisonResultAvailableException e) {
			assertTrue("NoMassSpectrumComparisonResultAvailableException", true);
		}
	}

	public void testMassSpectrumComparatorCompare_2() {

		try {
			result = MassSpectrumComparator.compare(unknown, null, null, PBMMassSpectrumComparator.COMPARATOR_ID);
		} catch(NoMassSpectrumComparisonResultAvailableException e) {
			assertTrue("NoMassSpectrumComparisonResultAvailableException", true);
		}
	}

	public void testMassSpectrumComparatorCompare_3() {

		try {
			result = MassSpectrumComparator.compare(null, reference, null, PBMMassSpectrumComparator.COMPARATOR_ID);
		} catch(NoMassSpectrumComparisonResultAvailableException e) {
			assertTrue("NoMassSpectrumComparisonResultAvailableException", true);
		}
	}

	public void testMassSpectrumComparatorCompare_4() {

		try {
			result = MassSpectrumComparator.compare(unknown, reference, null, PBMMassSpectrumComparator.COMPARATOR_ID);
		} catch(NoMassSpectrumComparisonResultAvailableException e) {
			assertTrue("NoMassSpectrumComparisonResultAvailableException", true);
		}
	}

	public void testMassSpectrumComparatorCompare_5() {

		try {
			result = MassSpectrumComparator.compare(unknown, reference, ionRange, PBMMassSpectrumComparator.COMPARATOR_ID);
		} catch(NoMassSpectrumComparisonResultAvailableException e) {
			assertTrue("NoMassSpectrumComparisonResultAvailableException", true);
		}
	}

	public void testMassSpectrumComparatorCompare_6() {

		try {
			result = MassSpectrumComparator.compare(unknown, reference, ionRange, null);
		} catch(NoMassSpectrumComparisonResultAvailableException e) {
			assertTrue("NoMassSpectrumComparisonResultAvailableException", true);
		}
	}
}
