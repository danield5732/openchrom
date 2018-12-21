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
package net.openchrom.xxd.process.supplier.templates.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.core.runtime.IStatus;

import net.openchrom.xxd.process.supplier.templates.util.StandardsReferencerListUtil;
import net.openchrom.xxd.process.supplier.templates.util.StandardsReferencerValidator;

public class AssignerReferences extends HashMap<String, AssignerReference> {

	private static final Logger logger = Logger.getLogger(AssignerReferences.class);
	//
	private static final long serialVersionUID = -219152470872308287L;
	private StandardsReferencerListUtil listUtil = new StandardsReferencerListUtil();
	private static final String SEPARATOR_TOKEN = StandardsReferencerListUtil.SEPARATOR_TOKEN;
	private static final String SEPARATOR_ENTRY = StandardsReferencerListUtil.SEPARATOR_ENTRY;

	public void add(AssignerReference setting) {

		if(setting != null) {
			put(setting.getName(), setting);
		}
	}

	public void load(String items) {

		loadSettings(items);
	}

	public void loadDefault(String items) {

		loadSettings(items);
	}

	public String save() {

		StringBuilder builder = new StringBuilder();
		Iterator<AssignerReference> iterator = values().iterator();
		while(iterator.hasNext()) {
			AssignerReference setting = iterator.next();
			extractSetting(setting, builder);
			if(iterator.hasNext()) {
				builder.append(SEPARATOR_TOKEN);
			}
		}
		return builder.toString().trim();
	}

	public String extractSettingString(AssignerReference setting) {

		StringBuilder builder = new StringBuilder();
		extractSetting(setting, builder);
		return builder.toString();
	}

	public AssignerReference extractSettingInstance(String item) {

		AssignerReference setting = null;
		//
		if(!"".equals(item)) {
			String[] values = item.split("\\" + SEPARATOR_ENTRY);
			setting = new AssignerReference();
			setting.setStartRetentionTime(getDouble(values, 0));
			setting.setStopRetentionTime(getDouble(values, 1));
			setting.setName(getString(values, 2));
		}
		//
		return setting;
	}

	public void importItems(File file) {

		try {
			StandardsReferencerValidator validator = new StandardsReferencerValidator();
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			String line;
			while((line = bufferedReader.readLine()) != null) {
				IStatus status = validator.validate(line);
				if(status.isOK()) {
					add(validator.getSetting());
				} else {
					logger.warn(status.getMessage());
				}
			}
			bufferedReader.close();
		} catch(FileNotFoundException e) {
			logger.warn(e);
		} catch(IOException e) {
			logger.warn(e);
		}
	}

	public boolean exportItems(File file) {

		try {
			PrintWriter printWriter = new PrintWriter(file);
			Iterator<AssignerReference> iterator = values().iterator();
			while(iterator.hasNext()) {
				StringBuilder builder = new StringBuilder();
				AssignerReference setting = iterator.next();
				extractSetting(setting, builder);
				printWriter.println(builder.toString());
			}
			printWriter.flush();
			printWriter.close();
			return true;
		} catch(FileNotFoundException e) {
			logger.warn(e);
			return false;
		}
	}

	private void loadSettings(String iems) {

		if(!"".equals(iems)) {
			String[] items = listUtil.parseString(iems);
			if(items.length > 0) {
				for(String item : items) {
					AssignerReference setting = extractSettingInstance(item);
					if(setting != null) {
						add(setting);
					}
				}
			}
		}
	}

	private void extractSetting(AssignerReference setting, StringBuilder builder) {

		builder.append(setting.getStartRetentionTime());
		builder.append(" ");
		builder.append(SEPARATOR_ENTRY);
		builder.append(" ");
		builder.append(setting.getStopRetentionTime());
		builder.append(" ");
		builder.append(SEPARATOR_ENTRY);
		builder.append(" ");
		builder.append(setting.getName());
	}

	private String getString(String[] values, int index) {

		return (values.length > index) ? values[index].trim() : "";
	}

	private double getDouble(String[] values, int index) {

		double result = 0.0d;
		String value = getString(values, index);
		try {
			result = Double.parseDouble(value);
		} catch(NumberFormatException e) {
			//
		}
		return result;
	}
}
