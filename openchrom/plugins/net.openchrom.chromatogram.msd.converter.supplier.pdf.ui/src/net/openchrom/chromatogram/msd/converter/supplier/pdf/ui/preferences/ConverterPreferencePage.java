/*******************************************************************************
 * Copyright (c) 2012, 2013 Philip (eselmeister) Wenig.
 * 
 * All rights reserved.
 *******************************************************************************/
package net.openchrom.chromatogram.msd.converter.supplier.pdf.ui.preferences;

import net.openchrom.chromatogram.msd.converter.supplier.pdf.preferences.BundleProductPreferences;
import net.openchrom.chromatogram.msd.converter.supplier.pdf.ui.Activator;
import net.openchrom.keys.ui.preferences.AbstractCustomFieldEditorPreferencePage;
import net.openchrom.keys.ui.preferences.IKeyPreferencePage;

public class ConverterPreferencePage extends AbstractCustomFieldEditorPreferencePage implements IKeyPreferencePage {

	public ConverterPreferencePage() {

		super(Activator.getDefault().getPreferenceStore(), new BundleProductPreferences(), false);
	}

	@Override
	public void createSettingPages() {

	}
}
