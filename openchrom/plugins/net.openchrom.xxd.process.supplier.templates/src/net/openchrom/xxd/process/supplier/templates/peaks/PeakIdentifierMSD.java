/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.templates.peaks;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.identifier.peak.IPeakIdentifierMSD;
import org.eclipse.chemclipse.chromatogram.msd.identifier.settings.IPeakIdentifierSettingsMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.xxd.process.supplier.templates.preferences.PreferenceSupplier;

public class PeakIdentifierMSD extends AbstractPeakIdentifier implements IPeakIdentifierMSD {

	@Override
	public IProcessingInfo identify(List<IPeakMSD> peaks, IPeakIdentifierSettingsMSD settings, IProgressMonitor monitor) {

		return applyIdentifier(peaks, settings, monitor);
	}

	@Override
	public IProcessingInfo identify(IPeakMSD peak, IPeakIdentifierSettingsMSD settings, IProgressMonitor monitor) {

		return identify(extractPeaks(peak), settings, monitor);
	}

	@Override
	public IProcessingInfo identify(IPeakMSD peak, IProgressMonitor monitor) {

		return identify(peak, getPeakIdentifierSettings(PreferenceSupplier.P_PEAK_IDENTIFIER_LIST_MSD), monitor);
	}

	@Override
	public IProcessingInfo identify(List<IPeakMSD> peaks, IProgressMonitor monitor) {

		return identify(peaks, getPeakIdentifierSettings(PreferenceSupplier.P_PEAK_IDENTIFIER_LIST_MSD), monitor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IProcessingInfo identify(IChromatogramSelectionMSD chromatogramSelectionMSD, IProgressMonitor monitor) {

		return identify(extractPeaks(chromatogramSelectionMSD), monitor);
	}
}
