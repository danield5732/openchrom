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
package net.openchrom.xxd.process.supplier.templates.ui.internal.provider;

import java.text.DecimalFormat;

import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.swt.graphics.Image;

import net.openchrom.xxd.process.supplier.templates.model.DetectorSetting;

public class PeakDetectorLabelProvider extends AbstractChemClipseLabelProvider {

	public static final String START_RETENTION_TIME = "Start Retention Time";
	public static final String STOP_RETENTION_TIME = "Stop Retention Time";
	public static final String DETECTOR_TYPE = "Detector Type";
	public static final String TRACES = "Traces";
	public static final String OPTIMIZE_RANGE = "Optimize Range";
	//
	public static final int INDEX_OPTIMIZE_RANGE = 4;
	//
	private DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish("0.0##");
	//
	public static final String[] TITLES = { //
			START_RETENTION_TIME, //
			STOP_RETENTION_TIME, //
			DETECTOR_TYPE, //
			TRACES, //
			OPTIMIZE_RANGE //
	};
	public static final int[] BOUNDS = { //
			100, //
			100, //
			50, //
			100, //
			30 //
	};

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			return getImage(element);
		} else if(columnIndex == INDEX_OPTIMIZE_RANGE) {
			if(element instanceof DetectorSetting) {
				DetectorSetting setting = (DetectorSetting)element;
				String fileName = (setting.isOptimizeRange()) ? IApplicationImage.IMAGE_SELECTED : IApplicationImage.IMAGE_DESELECTED;
				return ApplicationImageFactory.getInstance().getImage(fileName, IApplicationImage.SIZE_16x16);
			}
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		String text = "";
		if(element instanceof DetectorSetting) {
			DetectorSetting setting = (DetectorSetting)element;
			switch(columnIndex) {
				case 0:
					text = decimalFormat.format(setting.getStartRetentionTime());
					break;
				case 1:
					text = decimalFormat.format(setting.getStopRetentionTime());
					break;
				case 2:
					text = setting.getDetectorType();
					break;
				case 3:
					text = setting.getTraces();
					break;
				case 4:
					text = "";
					break;
				default:
					text = "n.v.";
			}
		}
		return text;
	}

	@Override
	public Image getImage(Object element) {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_PEAK_DETECTOR, IApplicationImage.SIZE_16x16);
	}
}
