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
package net.sf.kerner.utils.collections;

import java.util.Collection;

import net.sf.kerner.utils.collections.filter.FilterApplier;
import net.sf.kerner.utils.visitor.VisitorApplier;

public interface CollectionWalker<E> extends Walker<E>, FilterApplier<E>, VisitorApplier<E> {

	void walk(Collection<? extends E> list);
}
