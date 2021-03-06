/*******************************************************************************
 * Copyright (c) 2021 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 * Philip Wenig - adjustment AutoClosable
 *******************************************************************************/
package net.openchrom.chromatogram.msd.identifier.supplier.cdk.converter;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.eclipse.chemclipse.logging.core.Logger;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemSequence;
import org.openscience.cdk.io.MDLReader;

/**
 * Load MDL MOL streams using CDK
 */
@SuppressWarnings("deprecation")
public class CDKMolToMoleculeConverter implements IStructureConverter {

	private static final Logger logger = Logger.getLogger(CDKMolToMoleculeConverter.class);

	@Override
	public IAtomContainer generate(String input) {

		IAtomContainer molecule = null;
		if(input != null) {
			try (MDLReader mdlReader = new MDLReader(IOUtils.toInputStream(input))) {
				ChemFile chemFile = (ChemFile)mdlReader.read(new ChemFile());
				if(chemFile.getChemSequenceCount() > 0) {
					IChemSequence chemSequence = chemFile.getChemSequence(0);
					if(chemSequence.getChemModelCount() > 0) {
						IChemModel chemModel = chemSequence.getChemModel(0);
						if(chemModel.getMoleculeSet().getAtomContainerCount() > 0) {
							molecule = chemModel.getMoleculeSet().getAtomContainer(0);
						}
					}
				}
			} catch(IOException | CDKException e) {
				logger.error(e);
			}
		}
		return molecule;
	}
}