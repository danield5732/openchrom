/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.msd.process.supplier.cms.ui.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.ux.extension.msd.ui.views.AbstractMassSpectrumSelectionView;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import net.openchrom.msd.converter.supplier.cms.model.ICalibratedVendorLibraryMassSpectrum;
import net.openchrom.msd.process.supplier.cms.core.MassSpectraHolder;
import net.openchrom.msd.process.supplier.cms.ui.parts.swt.IonMeasurementListUI;

public class IonMeasurementListPart extends AbstractMassSpectrumSelectionView {

	@Inject
	private Composite parent;
	private IonMeasurementListUI ionMeasurementListUI;

	@Inject
	public IonMeasurementListPart(EPartService partService, MPart part, IEventBroker eventBroker) {
		super(part, partService, eventBroker);
	}

	@PostConstruct
	private void createControl() {

		parent.setLayout(new FillLayout());
		ionMeasurementListUI = new IonMeasurementListUI(parent, SWT.NONE);
		update(MassSpectraHolder.getLatestResults(), true);
	}

	@PreDestroy
	private void preDestroy() {

	}

	@Focus
	public void setFocus() {

		ionMeasurementListUI.getTable().setFocus();
	}

	@Override
	public void update(IScanMSD massSpectrum, boolean forceReload) {

		if(isPartVisible()) {
			if(massSpectrum instanceof ICalibratedVendorLibraryMassSpectrum) {
				ICalibratedVendorLibraryMassSpectrum spectrum = (ICalibratedVendorLibraryMassSpectrum)massSpectrum;
				ionMeasurementListUI.update(spectrum);
			} else {
				ionMeasurementListUI.update(null);
			}
		}
	}
}