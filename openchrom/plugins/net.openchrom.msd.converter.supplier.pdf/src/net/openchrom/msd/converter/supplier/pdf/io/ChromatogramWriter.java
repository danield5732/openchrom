/*******************************************************************************
 * Copyright (c) 2011, 2015 Philip (eselmeister) Wenig.
 * 
 * All rights reserved.
 *******************************************************************************/
package net.openchrom.msd.converter.supplier.pdf.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;

import net.chemclipse.converter.exceptions.FileIsNotWriteableException;
import net.chemclipse.msd.converter.io.AbstractChromatogramMSDWriter;
import net.openchrom.msd.converter.supplier.pdf.internal.io.PDFSupport;
import net.chemclipse.msd.model.core.IChromatogramMSD;

public class ChromatogramWriter extends AbstractChromatogramMSDWriter {

	@Override
	public void writeChromatogram(File file, IChromatogramMSD chromatogram, IProgressMonitor monitor) throws FileNotFoundException, FileIsNotWriteableException, IOException {

		PDFSupport pdfSupport = new PDFSupport();
		try {
			pdfSupport.exportChromatogram(file, chromatogram, monitor);
		} catch(Exception e) {
			throw new IOException(e);
		}
	}
}
