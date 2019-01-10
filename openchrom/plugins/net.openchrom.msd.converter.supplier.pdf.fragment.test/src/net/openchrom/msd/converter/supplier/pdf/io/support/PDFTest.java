/*******************************************************************************
 * Copyright (c) 2018 pwenig.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * pwenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.msd.converter.supplier.pdf.io.support;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import junit.framework.TestCase;

public class PDFTest extends TestCase {

	private static final String OPENCHROM = "OpenChrom - the open source alternative for chromatography/spectrometry";
	private static final String LINE_FIRST = "First Line";
	private static final String LINE_CONTENT = "Lorem ipsum";
	private static final String LINE_LAST = "Last Line";
	private static final String FOOTER = "Page 1/20";
	//
	private DecimalFormat decimalFormat = new DecimalFormat("0.0000");

	public void test1() throws IOException {

		PDDocument document = null;
		try {
			document = new PDDocument();
			printHeaderData(document);
			document.save("/home/pwenig/Schreibtisch/Test.pdf");
		} catch(IOException e) {
			System.out.println(e);
		} finally {
			if(document != null) {
				document.close();
			}
		}
	}

	private void printHeaderData(PDDocument document) throws IOException {

		// printPage(document, PDType1Font.COURIER);
		// printPage(document, PDType1Font.COURIER_BOLD);
		// printPage(document, PDType1Font.COURIER_BOLD_OBLIQUE);
		// printPage(document, PDType1Font.COURIER_OBLIQUE);
		// printPage(document, PDType1Font.HELVETICA_BOLD);
		// printPage(document, PDType1Font.HELVETICA_BOLD_OBLIQUE);
		// printPage(document, PDType1Font.HELVETICA_OBLIQUE);
		// printPage(document, PDType1Font.TIMES_BOLD);
		// printPage(document, PDType1Font.TIMES_BOLD_ITALIC);
		// printPage(document, PDType1Font.TIMES_ITALIC);
		// printPage(document, PDType1Font.TIMES_ROMAN);
		//
		printPage1(document, PDType1Font.HELVETICA);
		printPage2(document, PDType1Font.HELVETICA);
		printPage3(document, PDType1Font.HELVETICA);
		printPage4(document, PDType1Font.HELVETICA);
		printPage5(document, PDType1Font.HELVETICA);
		printPage6(document, PDType1Font.HELVETICA);
		printPage7(document, PDType1Font.HELVETICA);
		printPage8(document, PDType1Font.HELVETICA);
	}

	private PDPage printPage1(PDDocument document, PDFont font) throws IOException {

		PageUtil pageUtil = new PageUtil(document, PDRectangle.A4);
		//
		TextElement textElement = new TextElement().setX(10).setMaxWidth(190);
		pageUtil.printText(textElement.setY(0).setText(LINE_FIRST));
		for(int i = 1; i <= 28; i++) {
			pageUtil.printText(textElement.setY(i * 10).setText(LINE_CONTENT));
		}
		pageUtil.printText(textElement.setY(297).setText(LINE_LAST).setReferenceY(ReferenceY.BOTTOM));
		//
		pageUtil.printLine(new LineElement(10, 10, 10, 287).setWidth(0.2f)); // left
		pageUtil.printLine(new LineElement(10, 10, 200, 10).setWidth(0.2f)); // top
		pageUtil.printLine(new LineElement(200, 10, 200, 287).setWidth(0.2f)); // right
		pageUtil.printLine(new LineElement(10, 287, 200, 287).setWidth(0.2f)); // bottom
		//
		pageUtil.close();
		return pageUtil.getPage();
	}

	private PDPage printPage2(PDDocument document, PDFont font) throws IOException {

		PageUtil pageUtil = new PageUtil(document, PDRectangle.A4, true);
		//
		TextElement textElement = new TextElement().setX(10).setMaxWidth(277);
		pageUtil.printText(textElement.setY(0).setText(LINE_FIRST));
		for(int i = 1; i <= 17; i++) {
			pageUtil.printText(textElement.setY(i * 10).setText(LINE_CONTENT));
		}
		pageUtil.printText(textElement.setY(210).setText(LINE_LAST).setReferenceY(ReferenceY.BOTTOM));
		//
		pageUtil.printLine(new LineElement(10, 10, 10, 200).setWidth(0.2f)); // left
		pageUtil.printLine(new LineElement(10, 10, 287, 10).setWidth(0.2f)); // top
		pageUtil.printLine(new LineElement(287, 10, 287, 200).setWidth(0.2f)); // right
		pageUtil.printLine(new LineElement(10, 200, 287, 200).setWidth(0.2f)); // bottom
		//
		pageUtil.close();
		return pageUtil.getPage();
	}

	private PDPage printPage3(PDDocument document, PDFont font) throws IOException {

		PDPage page = printPage2(document, font);
		page.setRotation(0);
		return page;
	}

	private PDPage printPage4(PDDocument document, PDFont font) throws IOException {

		PDPage page = printPage2(document, font);
		page.setRotation(-180);
		return page;
	}

	private PDPage printPage5(PDDocument document, PDFont font) throws IOException {

		PageUtil pageUtil = new PageUtil(document, PDRectangle.A4);
		//
		pageUtil.printImage(new ImageElement().setImage(getImage(document)).setX(10).setY(10).setWidth(63.5f).setHeight(8.05f));
		pageUtil.printText(new TextElement().setX(10).setY(20).setMaxWidth(190).setText(OPENCHROM));
		//
		pageUtil.printImage(new ImageElement().setImage(getImage(document)).setX(10).setY(148.5f).setWidth(63.5f).setHeight(8.05f).setReferenceY(ReferenceY.CENTER));
		pageUtil.printText(new TextElement().setX(83.5f).setY(148.5f).setMaxWidth(116.5f).setText(OPENCHROM).setReferenceY(ReferenceY.CENTER));
		//
		pageUtil.printText(new TextElement().setX(10).setY(277).setMaxWidth(190).setText(OPENCHROM).setReferenceY(ReferenceY.BOTTOM));
		pageUtil.printImage(new ImageElement().setImage(getImage(document)).setX(10).setY(287).setWidth(63.5f).setHeight(8.05f).setReferenceY(ReferenceY.BOTTOM));
		pageUtil.printText(new TextElement().setX(74).setY(287).setMaxWidth(190).setText(FOOTER).setReferenceY(ReferenceY.BOTTOM));
		//
		pageUtil.close();
		return pageUtil.getPage();
	}

	private PDPage printPage6(PDDocument document, PDFont font) throws IOException {

		PageUtil pageUtil = new PageUtil(document, PDRectangle.A4, true);
		//
		pageUtil.printImage(new ImageElement().setImage(getImage(document)).setX(10).setY(10).setWidth(63.5f).setHeight(8.05f));
		pageUtil.printText(new TextElement().setX(10).setY(20).setMaxWidth(277).setText(OPENCHROM));
		//
		pageUtil.printImage(new ImageElement().setImage(getImage(document)).setX(10).setY(105).setWidth(63.5f).setHeight(8.05f).setReferenceY(ReferenceY.CENTER));
		pageUtil.printText(new TextElement().setX(83.5f).setY(105).setMaxWidth(203.5f).setText(OPENCHROM).setReferenceY(ReferenceY.CENTER));
		//
		pageUtil.printText(new TextElement().setX(10).setY(190).setMaxWidth(277).setText(OPENCHROM).setReferenceY(ReferenceY.BOTTOM));
		pageUtil.printImage(new ImageElement().setImage(getImage(document)).setX(10).setY(200).setWidth(63.5f).setHeight(8.05f).setReferenceY(ReferenceY.BOTTOM));
		pageUtil.printText(new TextElement().setX(74).setY(200).setMaxWidth(277).setText(FOOTER).setReferenceY(ReferenceY.BOTTOM));
		//
		pageUtil.close();
		return pageUtil.getPage();
	}

	private PDPage printPage7(PDDocument document, PDFont font) throws IOException {

		PageUtil pageUtil = new PageUtil(document, PDRectangle.A4);
		//
		PDFTable pdfTable = new PDFTable();
		pdfTable.setPositionX(10);
		pdfTable.setPositionY(10);
		/*
		 * Header
		 */
		pdfTable.addColumn("A", 50);
		pdfTable.addColumn("B", 100);
		pdfTable.addColumn("C", 40);
		/*
		 * Data
		 */
		for(int i = 0; i < 30; i++) {
			List<String> row = new ArrayList<>();
			row.add(decimalFormat.format(Math.random()));
			row.add(decimalFormat.format(Math.random()));
			row.add(decimalFormat.format(Math.random()));
			pdfTable.addRow(row);
		}
		pageUtil.printTable(pdfTable);
		//
		pageUtil.close();
		return pageUtil.getPage();
	}

	private PDPage printPage8(PDDocument document, PDFont font) throws IOException {

		PageUtil pageUtil = new PageUtil(document, PDRectangle.A4, true);
		//
		PDFTable pdfTable = new PDFTable();
		pdfTable.setPositionX(10);
		pdfTable.setPositionY(10);
		/*
		 * Header
		 */
		pdfTable.addColumn("A", 77);
		pdfTable.addColumn("B", 150);
		pdfTable.addColumn("C", 50);
		/*
		 * Data
		 */
		for(int i = 0; i < 30; i++) {
			List<String> row = new ArrayList<>();
			row.add(decimalFormat.format(Math.random()));
			row.add(decimalFormat.format(Math.random()));
			row.add(decimalFormat.format(Math.random()));
			pdfTable.addRow(row);
		}
		pageUtil.printTable(pdfTable);
		//
		pageUtil.close();
		return pageUtil.getPage();
	}

	private PDImageXObject getImage(PDDocument document) throws IOException {

		return JPEGFactory.createFromStream(document, PDFTest.class.getResourceAsStream("openchromlogo.jpg"));
	}
}
