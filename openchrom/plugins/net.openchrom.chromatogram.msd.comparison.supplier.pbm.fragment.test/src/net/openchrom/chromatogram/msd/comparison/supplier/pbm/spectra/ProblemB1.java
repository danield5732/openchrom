/*******************************************************************************
 * Copyright (c) 2014 Dr. Philip Wenig.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.chromatogram.msd.comparison.supplier.pbm.spectra;

import net.chemclipse.msd.model.core.IMassSpectrum;
import net.chemclipse.msd.model.implementation.DefaultIon;
import net.chemclipse.msd.model.implementation.DefaultMassSpectrum;

public class ProblemB1 implements ITestMassSpectrum {

	private IMassSpectrum massSpectrum;

	public ProblemB1() throws Exception {

		massSpectrum = new DefaultMassSpectrum();
		massSpectrum.addIon(new DefaultIon(50.0d, 50.822044f));
		massSpectrum.addIon(new DefaultIon(70.0d, 50.822044f));
		massSpectrum.addIon(new DefaultIon(34.0d, 59.292385f));
		massSpectrum.addIon(new DefaultIon(26.0d, 67.762726f));
		massSpectrum.addIon(new DefaultIon(36.0d, 84.70341f));
		massSpectrum.addIon(new DefaultIon(16.0d, 105.87926f));
		massSpectrum.addIon(new DefaultIon(69.0d, 190.58267f));
		massSpectrum.addIon(new DefaultIon(42.0d, 220.22885f));
		massSpectrum.addIon(new DefaultIon(37.0d, 398.10602f));
		massSpectrum.addIon(new DefaultIon(40.0d, 567.5128f));
		massSpectrum.addIon(new DefaultIon(38.0d, 635.2756f));
		massSpectrum.addIon(new DefaultIon(39.0d, 3405.0577f));
		massSpectrum.addIon(new DefaultIon(68.0d, 4230.935f));
	}

	@Override
	public IMassSpectrum getMassSpectrum() {

		return massSpectrum;
	}
}
