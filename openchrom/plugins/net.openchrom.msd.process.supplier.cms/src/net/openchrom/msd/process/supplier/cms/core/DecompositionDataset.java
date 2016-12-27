/*******************************************************************************
 * Copyright (c) 2016 whitlow.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * whitlow - initial API and implementation
 *******************************************************************************/
package net.openchrom.msd.process.supplier.cms.core;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import net.openchrom.msd.converter.supplier.cms.model.ICalibratedVendorLibraryMassSpectrum;
import net.openchrom.msd.converter.supplier.cms.model.ICalibratedVendorMassSpectrum;

class DecompositionDataset {

	private LibIon libions[];
	private int libIonsCount; // # ions read from library file
	private int libIonsUsed; // # library ions that matched with scan ions, <= libIonsCount
	public ScanIonMeasurement scanions[];
	private int scanIonsCount; // # ions read from scan file
	private int scanIonsUsed; // # ions from scan file that matched with library ions, <= scanIonsCount
	private LibComponent libComps[]; // keep track of library component information
	private int libCompsCount; // the number of components found in the library file
	private int libCompsUsed; // how many components are used in the LS fit, <= libCompsCount
	private SortedSet<String> compNameSet; // used to help ensure no duplicate library components
	private boolean matched; // false and then set true after executing matchIons()
	private boolean canDoQuantitative; // set true if can do quantitative result
	private ICalibratedVendorMassSpectrum scanRef;

	DecompositionDataset() {
		libions = new LibIon[10];
		scanions = new ScanIonMeasurement[10];
		libComps = new LibComponent[10];
		libIonsCount = 0;
		scanIonsCount = 0;
		libCompsCount = 0;
		libIonsUsed = -1;
		scanIonsUsed = -1;
		libCompsUsed = -1;
		compNameSet = new ConcurrentSkipListSet<String>(String.CASE_INSENSITIVE_ORDER);
		matched = false;
		canDoQuantitative = false;
		scanRef = null;
	}

	boolean canDoQuantitative() {

		if(!matched)
			return false;
		return canDoQuantitative;
	}

	ICalibratedVendorMassSpectrum getScanRef() {

		return scanRef;
	}

	void setScanRef(ICalibratedVendorMassSpectrum scan) {

		scanRef = scan;
	}

	String getLibCompName(int i) {

		return libComps[i].libraryRef.getLibraryInformation().getName();
	}

	ICalibratedVendorLibraryMassSpectrum getLibRef(int i) {

		return libComps[i].libraryRef;
	}

	LibIon[] getLibIons() {

		return libions;
	}

	ScanIonMeasurement[] getScanIons() {

		return scanions;
	}

	int getUsedLibIonCount() throws InvalidLibIonCountException {

		if(!matched)
			throw new InvalidLibIonCountException();
		if(0 >= libIonsUsed)
			throw new InvalidLibIonCountException();
		return libIonsUsed;
	}

	int getUsedScanIonCount() throws InvalidScanIonCountException {

		if(!matched)
			throw new InvalidScanIonCountException();
		if(0 >= scanIonsUsed)
			throw new InvalidScanIonCountException();
		return scanIonsUsed;
	}

	int getUsedCompCount() throws InvalidGetCompCountException {

		if(!matched)
			throw new InvalidGetCompCountException(libCompsUsed);
		if(0 >= libCompsUsed)
			throw new InvalidGetCompCountException(libCompsUsed);
		return libCompsUsed;
	}

	void addLibIon(double mass, double abundance, int compIndex) throws InvalidComponentIndexException {

		if(0 > compIndex) {
			throw new InvalidComponentIndexException(compIndex);
		}
		if(libions.length <= libIonsCount) {
			libions = Arrays.copyOf(libions, 2 * libions.length);
		}
		libions[libIonsCount] = new LibIon(mass, abundance, compIndex, libComps[compIndex]);
		libIonsCount++;
	}

	int addNewComponent(ICalibratedVendorLibraryMassSpectrum libraryMassSpectrum) throws DuplicateCompNameException {

		String compName = libraryMassSpectrum.getLibraryInformation().getName();
		if(!compNameSet.add(compName)) {
			throw new DuplicateCompNameException(compName);
		}
		if(libComps.length <= libCompsCount) {
			libComps = Arrays.copyOf(libComps, 2 * libComps.length);
		}
		libComps[libCompsCount] = new LibComponent(libraryMassSpectrum);
		return libCompsCount++;
	}

	int getCompCount() {

		return libCompsCount;
	}

	int getIonsInScan() {

		return scanIonsCount;
	}
	// LibIon getLibIon(int index) {
	// if (!matched)
	// throw new InvalidGetLibIonException(index);
	// return libions[index];
	// }

	// ScanIon getScanIon(int index) {
	// if (!matched)
	// throw new InvalidGetScanIonException(index);
	// return scanions[index];
	// }
	void addScanIon(double mass, double abundance, ICalibratedVendorMassSpectrum scan) {

		if(scanions.length <= scanIonsCount) {
			scanions = Arrays.copyOf(scanions, 2 * scanions.length);
		}
		scanions[scanIonsCount] = new ScanIonMeasurement(mass, abundance, scan, scanIonsCount);
		scanIonsCount++;
	}

	void matchIons(double massTol) throws NoLibIonsException, NoScanIonsException {

		int i, j, rowCount;
		if(0 >= libIonsCount)
			throw new NoLibIonsException(libIonsCount);
		if(0 >= scanIonsCount)
			throw new NoScanIonsException(scanIonsCount);
		Arrays.sort(this.libions, 0, libIonsCount);
		Arrays.sort(this.scanions, 0, scanIonsCount);
		i = 0; // index into libIOns[]
		rowCount = 0;
		for(j = 0; j < scanIonsCount; j++) {
			while((i < libIonsCount) && (libions[i].massLess(scanions[j].ionMass, massTol))) { // skip over library ion masses that are less than current scan ion mass
				i++;
			}
			if((i < libIonsCount) && (libions[i].massEqual(scanions[j].ionMass, massTol))) { // test if library ion mass == current scan ion mass
				do { // mark ions having equal masses
					libions[i].setMark();
					libions[i].ionRowIndex = rowCount;
					scanions[j].setMark();
					scanions[j].ionRowIndex = rowCount;
					i++;
				} while((i < libIonsCount) && (libions[i].massEqual(scanions[j].ionMass, massTol)));
				rowCount++;
			} // if
		} // for
		j = 0;
		for(i = 0; i < libIonsCount; i++) { // save only marked library ions
			if(libions[i].mark) {
				libions[j] = libions[i];
				j++;
			}
			// else {
			// System.out.println("eliminating ion ("
			// + libions[i].ionMass +","
			// + libions[i].ionAbundance +"), component "
			// + libions[i].componentRef.libraryRef.getLibraryInformation().getName());
			// }
		}
		libIonsUsed = j;
		libions = Arrays.copyOf(libions, libIonsUsed);
		j = 0;
		for(i = 0; i < scanIonsCount; i++) { // save only marked scan ions
			if(scanions[i].mark) {
				scanions[j] = scanions[i];
				j++;
			}
		}
		scanIonsUsed = j;
		scanions = Arrays.copyOf(scanions, scanIonsUsed);
		for(i = 0; i < libIonsUsed; i++) { // identify library components we actually use
			// libComps[libions[i].ionCompIndex].mark = true;
			libions[i].componentRef.mark = true;
		}
		j = 0;
		for(i = 0; i < libCompsCount; i++) { // consolidate library components we actually use
			if(libComps[i].mark) {
				libComps[j] = libComps[i];
				libComps[j].componentIndex = j;
				j++;
			} else {
				System.out.println("Eliminating library component \"" + libComps[i].libraryRef.getLibraryInformation().getName() + "\", no ions are present in the scan");
			}
		}
		libCompsUsed = j;
		libComps = Arrays.copyOf(libComps, libCompsUsed);
		// check if can do quantitative result
		canDoQuantitative = true;
		// String scanPressureUnits, scanSignalUnits, libSignalUnits;
		// double libPressure;
		for(LibComponent libComp : libComps) {
			// scanPressureUnits = scanRef.getSourcePressureUnits();
			// scanSignalUnits = scanRef.getSignalUnits();
			// libSignalUnits = libComp.libraryRef.getSignalUnits();
			// libPressure = libComp.libraryRef.getSourcePressure(scanRef.getSourcePressureUnits());
			if(canDoQuantitative) {
				if(0 == libComp.libraryRef.getSourcePressure(scanRef.getSourcePressureUnits())) {
					System.out.println("Library file pressure units (" + libComp.libraryRef.getSourcePressureUnits() + ") cannot be converted to scan file pressure units (" + scanRef.getSourcePressureUnits() + ")");
					canDoQuantitative = false;
				}
				if(!libComp.libraryRef.getSignalUnits().equalsIgnoreCase(scanRef.getSignalUnits())) {
					System.out.println("Library file signal units (" + libComp.libraryRef.getSignalUnits() + ") != (" + scanRef.getSignalUnits() + ")");
					canDoQuantitative = false;
				}
			}
		}
		matched = true;
	}

	public int getScanPeakIndex(int i) throws InvalidScanIonCountException {

		if(!matched)
			throw new InvalidScanIonCountException();
		if((0 > i) || (scanIonsUsed <= i))
			throw new InvalidScanIonCountException();
		return scanions[i].ionIndex;
	}
} // class