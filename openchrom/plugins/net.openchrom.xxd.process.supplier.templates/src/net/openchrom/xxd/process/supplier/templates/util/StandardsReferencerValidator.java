/*******************************************************************************
 * Copyright (c) 2018, 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.templates.util;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import net.openchrom.xxd.process.supplier.templates.model.AssignerReference;

public class StandardsReferencerValidator extends AbstractTemplateValidator implements ITemplateValidator {

	private static final String ERROR_ENTRY = "Please enter an item, e.g.: '" + StandardsReferencerListUtil.EXAMPLE_SINGLE + "'";
	private static final String SEPARATOR_TOKEN = StandardsReferencerListUtil.SEPARATOR_TOKEN;
	private static final String SEPARATOR_ENTRY = StandardsReferencerListUtil.SEPARATOR_ENTRY;
	private static final String ERROR_TOKEN = "The item must not contain: " + SEPARATOR_TOKEN;
	//
	private double startRetentionTimeMinutes = 0;
	private double stopRetentionTimeMinutes = 0;
	private String internalStandard = "";
	private String identifier = "";

	//
	@Override
	public IStatus validate(Object value) {

		String message = null;
		if(value == null) {
			message = ERROR_ENTRY;
		} else {
			if(value instanceof String) {
				String text = ((String)value).trim();
				if(text.contains(SEPARATOR_TOKEN)) {
					message = ERROR_TOKEN;
				} else if("".equals(text.trim())) {
					message = ERROR_ENTRY;
				} else {
					/*
					 * Extract retention time, ...
					 */
					String[] values = text.trim().split("\\" + SEPARATOR_ENTRY); // The pipe needs to be escaped.
					if(values.length >= 3) {
						/*
						 * Evaluation
						 */
						startRetentionTimeMinutes = parseDouble(values, 0);
						if(startRetentionTimeMinutes < 0.0d) {
							message = "The start retention time must be not lower than 0.";
						}
						//
						stopRetentionTimeMinutes = parseDouble(values, 1);
						internalStandard = parseString(values, 2);
						if("".equals(internalStandard)) {
							message = "The name of the internal standards needs to be set.";
						}
						identifier = parseString(values, 3);
						/*
						 * Extended Check
						 */
						if(startRetentionTimeMinutes == 0.0d && stopRetentionTimeMinutes == 0.0d) {
							if("".equals(identifier)) {
								message = "Please set a source identifier instead of the start/stop retention time.";
							}
						} else {
							if(stopRetentionTimeMinutes <= startRetentionTimeMinutes) {
								message = "The stop retention time must be greater then the start retention time.";
							}
						}
					} else {
						message = ERROR_ENTRY;
					}
				}
			} else {
				message = ERROR_ENTRY;
			}
		}
		//
		if(message != null) {
			return ValidationStatus.error(message);
		} else {
			return ValidationStatus.ok();
		}
	}

	public AssignerReference getSetting() {

		AssignerReference settings = new AssignerReference();
		settings.setStartRetentionTimeMinutes(startRetentionTimeMinutes);
		settings.setStopRetentionTimeMinutes(stopRetentionTimeMinutes);
		settings.setInternalStandard(internalStandard);
		settings.setIdentifier(identifier);
		return settings;
	}
}
