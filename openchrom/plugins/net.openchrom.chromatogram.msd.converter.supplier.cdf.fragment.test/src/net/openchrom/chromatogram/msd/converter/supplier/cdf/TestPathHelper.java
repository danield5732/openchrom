/*******************************************************************************
 * Copyright (c) 2008, 2011 Philip (eselmeister) Wenig.
 * 
 * This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston MA 02111-1307, USA
 * 
 * 
 * Contributors: Philip (eselmeister) Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.chromatogram.msd.converter.supplier.cdf;

import net.openchrom.chromatogram.msd.converter.supplier.cdf.PathResolver;

/**
 * THIS CLASS IS NOT SUITED FOR PRODUCTIVE USE!<br/>
 * IT IS A TESTCLASS!
 * 
 * @author eselmeister
 */
public class TestPathHelper extends PathResolver {

	/*
	 * IMPORT
	 */
	public static final String TESTFILE_IMPORT_TEST = "testData/files/import/TEST.CDF";
	public static final String TESTFILE_IMPORT_EMPTY = "testData/files/import/EMPTY.CDF";
	public static final String TESTFILE_IMPORT_NOT_READABLE = "testData/files/import/NOT_READABLE.CDF";
	public static final String TESTFILE_IMPORT_TEST_LC = "testData/files/import/test_lc.cdf";
	public static final String TESTFILE_IMPORT_TEST_UC = "testData/files/import/TEST_UC.CDF";
	public static final String TESTFILE_IMPORT_OP17760 = "testData/files/import/OP17760.CDF";
	public static final String TESTFILE_IMPORT_OP17760_AGILENT = "testData/files/import/OP17760.D/DATA.MS";
	public static final String TESTFILE_IMPORT_080903_011 = "testData/files/import/miscellaneous/080903_011.CDF";
	public static final String TESTFILE_IMPORT_OP17767 = "testData/files/import/miscellaneous/OP17767.CDF";
	public static final String TESTFILE_IMPORT_OP17768 = "testData/files/import/miscellaneous/OP17768.CDF";
	public static final String TESTFILE_IMPORT_VOCJ3205 = "testData/files/import/miscellaneous/VOCJ3205.CDF";
	public static final String TESTFILE_IMPORT_080811_005 = "testData/files/import/miscellaneous/080811_005.CDF";
	public static final String TESTFILE_IMPORT_080811_026 = "testData/files/import/miscellaneous/080811_026.CDF";
	public static final String TESTFILE_IMPORT_080903_006 = "testData/files/import/miscellaneous/080903_006.CDF";
	public static final String TESTFILE_IMPORT_EIEI_2_01 = "testData/files/import/miscellaneous/EI-EI_2_01.CDF";
	public static final String TESTFILE_IMPORT_EIEI_3_01 = "testData/files/import/miscellaneous/EI-EI_3_01.CDF";
	public static final String TESTFILE_IMPORT_EIEI_8_01 = "testData/files/import/miscellaneous/EI-EI_8_01.CDF";
	public static final String TESTFILE_IMPORT_OP17780 = "testData/files/import/miscellaneous/OP17780.CDF";
	public static final String TESTFILE_IMPORT_OP17796 = "testData/files/import/miscellaneous/OP17796.CDF";
	public static final String TESTFILE_IMPORT_OP17804 = "testData/files/import/miscellaneous/OP17804.CDF";
	public static final String TESTFILE_IMPORT_OP8777 = "testData/files/import/miscellaneous/OP8777.CDF";
	public static final String TESTFILE_IMPORT_OP8786 = "testData/files/import/miscellaneous/OP8786.CDF";
	public static final String TESTFILE_IMPORT_CVB_2010_08_17 = "testData/files/import/miscellaneous/cvb-2010-08-17_1_17.CDF";
	/*
	 * EXPORT
	 */
	public static final String TESTFILE_EXPORT_TEST = "testData/files/export/TEST.CDF";
	/*
	 * SPECIFICATION VALIDATOR TEST
	 */
	public static final String VALIDATOR_TEST_SPEC = "testData/files/import/CHROMATOGRAM.CDF";
	public static final String VALIDATOR_TEST_1 = "testData/files/import/CHROMATOGRAM.CDF";
	// Works not under Microsoft Windows.
	// public static final String VALIDATOR_TEST_2 =
	// "testData/files/import/CHROMATOGRAM.";
	public static final String VALIDATOR_TEST_3 = "testData/files/import/CHROMATOGRAM";
	public static final String VALIDATOR_TEST_4 = "testData/files/import";
}
