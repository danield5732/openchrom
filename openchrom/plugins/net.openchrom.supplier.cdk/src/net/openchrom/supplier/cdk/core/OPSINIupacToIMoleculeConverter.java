/*******************************************************************************
 * Copyright (c) 2013 Marwin Wollschläger.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Marwin Wollschläger - initial API and implementation
 *******************************************************************************/
package net.openchrom.supplier.cdk.core;

import org.openscience.cdk.interfaces.IMolecule;

import uk.ac.cam.ch.wwmm.opsin.NameToStructure;
import uk.ac.cam.ch.wwmm.opsin.NameToStructureConfig;
import uk.ac.cam.ch.wwmm.opsin.OpsinResult;

public class OPSINIupacToIMoleculeConverter implements IStructureGenerator {

	CDKSmilesToIMoleculeConverter smilesToIMolecule = new CDKSmilesToIMoleculeConverter();

	@Override
	public IMolecule generate(String input) {

		NameToStructure nameStructure = NameToStructure.getInstance();
		NameToStructureConfig nameStructureConfig = new NameToStructureConfig();
		nameStructureConfig.setAllowRadicals(true);
		OpsinResult result = nameStructure.parseChemicalName(input, nameStructureConfig);
		return smilesToIMolecule.generate(result.getSmiles());
	}
}
