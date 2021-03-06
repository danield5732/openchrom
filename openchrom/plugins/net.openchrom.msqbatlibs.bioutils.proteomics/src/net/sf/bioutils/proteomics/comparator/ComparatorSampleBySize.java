/*******************************************************************************
 * Copyright (c) 2015, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.sf.bioutils.proteomics.comparator;

import java.util.Comparator;

import net.sf.bioutils.proteomics.sample.Sample;

public class ComparatorSampleBySize implements Comparator<Sample> {

	public int compare(final Sample o1, final Sample o2) {

		return Integer.valueOf(o1.getSize()).compareTo(Integer.valueOf(o2.getSize()));
	}
}
