/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.classifier.supplier.ratios.ui.internal.provider;

import java.text.DecimalFormat;

import org.eclipse.chemclipse.model.core.AbstractChromatogram;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.swt.graphics.Image;

import net.openchrom.xxd.classifier.supplier.ratios.model.TraceRatio;

public class TraceRatioLabelProvider extends AbstractChemClipseLabelProvider {

	private DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish();
	private String displayOption = "";

	public TraceRatioLabelProvider() {
		this(TraceRatioResultTitles.OPTION_RESULTS);
	}

	public TraceRatioLabelProvider(String displayOption) {
		this.displayOption = (displayOption.equals(TraceRatioResultTitles.OPTION_SETTINGS)) ? TraceRatioResultTitles.OPTION_SETTINGS : TraceRatioResultTitles.OPTION_RESULTS;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			return getImage(element);
		} else {
			return null;
		}
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		if(TraceRatioResultTitles.OPTION_RESULTS.equals(displayOption)) {
			return getColumnTextResults(element, columnIndex);
		} else if(TraceRatioResultTitles.OPTION_SETTINGS.equals(displayOption)) {
			return getColumnTextSettings(element, columnIndex);
		} else {
			return "";
		}
	}

	private String getColumnTextSettings(Object element, int columnIndex) {

		String text = "";
		if(element instanceof TraceRatio) {
			TraceRatio traceRatio = (TraceRatio)element;
			switch(columnIndex) {
				case 0:
					text = traceRatio.getName();
					break;
				case 1:
					text = traceRatio.getTestCase();
					break;
				case 2:
					text = decimalFormat.format(traceRatio.getExpectedRatio());
					break;
				case 3:
					text = decimalFormat.format(traceRatio.getDeviationWarn());
					break;
				case 4:
					text = decimalFormat.format(traceRatio.getDeviationError());
					break;
			}
		}
		return text;
	}

	private String getColumnTextResults(Object element, int columnIndex) {

		String text = "";
		if(element instanceof TraceRatio) {
			TraceRatio traceRatio = (TraceRatio)element;
			switch(columnIndex) {
				case 0:
					IPeak peak = traceRatio.getPeak();
					if(peak != null) {
						text = decimalFormat.format(peak.getPeakModel().getRetentionTimeAtPeakMaximum() / AbstractChromatogram.MINUTE_CORRELATION_FACTOR);
					} else {
						text = "--";
					}
					break;
				case 1:
					text = traceRatio.getName();
					break;
				case 2:
					text = traceRatio.getTestCase();
					break;
				case 3:
					text = decimalFormat.format(traceRatio.getRatio());
					break;
				case 4:
					text = decimalFormat.format(traceRatio.getDeviation());
					break;
			}
		}
		return text;
	}

	public Image getImage(Object element) {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_CLASSIFIER, IApplicationImage.SIZE_16x16);
	}
}
