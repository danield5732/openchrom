/*******************************************************************************
 * Copyright (c) 2016 Walter Whitlock, Philip Wenig.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Walter Whitlock - initial API and implementation
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.msd.converter.supplier.cms.model;

import java.util.List;

import org.eclipse.chemclipse.model.exceptions.AbundanceLimitExceededException;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;

public interface ICalibratedVendorMassSpectrum extends IRegularLibraryMassSpectrum, IScanMSD {

	/*
	 * TODO WALTER
	 * Add the CMS specific fields here.
	 */
	public List<IMsdPeakMeasurement> getPeaks();

	public boolean addPeak(IMsdPeakMeasurement peak);

	public boolean addPeak(double mz, float signal);

	public boolean scale();

	public boolean unscale();

	public IMsdPeakMeasurement getPeak(int scanPeakIndex);

	public double getSourcep();

	public String getSPunits();

	public String getSigunits();

	public String getScanName();

	public String getTstamp();

	public double getEtimes();

	public double getEenergy();

	public double getIenergy();

	public String getIname(String iname);

	public void setSourcep(double sourcep);

	public void setSPunits(String spunits);

	public void setSigunits(String sigunits);

	public void setTstamp(String tstamp);

	public void setEtimes(double etimes);

	public void setEenergy(double eenergy);

	public void setIenergy(double ienergy);

	public void setIname(String iname);

	public void setScanName(String name);

	public void updateIons();

	public void updateSignalLimits();

	public ICalibratedVendorMassSpectrum makeNoisyCopy(long l, double relativeError) throws CloneNotSupportedException;

	public ICalibratedVendorMassSpectrum makeDeepCopy() throws CloneNotSupportedException;

	public List<IIon> getIons();

	/**
	 * Returns the source.
	 * 
	 * @return String
	 */
	String getSource();

	/**
	 * Sets the source.
	 * 
	 * @param source
	 */
	void setSource(String source);
	// String getName();
}
