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
package net.sf.bioutils.proteomics.feature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sf.bioutils.proteomics.Spectrum;
import net.sf.bioutils.proteomics.peak.ComparatorPeakByMZ;
import net.sf.bioutils.proteomics.peak.Peak;
import net.sf.bioutils.proteomics.peak.PeakFractionated;
import net.sf.kerner.utils.collections.UtilCollection;
import net.sf.kerner.utils.collections.list.UtilList;

public class UtilFeature {

	// private final static Logger log =
	// LoggerFactory.getLogger(UtilFeature.class);
	@Deprecated
	public static List<Feature> asFeatures(final Collection<? extends Peak> peaks) {

		return UtilList.cast(peaks);
	}

	public static List<Feature> cast(final Collection<? extends Peak> peaks) {

		return UtilList.cast(peaks);
	}

	public static boolean containsFeatures(final Collection<? extends Peak> peaks) {

		for(final Peak p : peaks) {
			if(p instanceof Feature) {
				return true;
			}
		}
		return false;
	}

	public static List<Peak> getAllMemberPeaks(final Collection<? extends Peak> features) {

		final ArrayList<Peak> result = new ArrayList<Peak>(features.size() * 4);
		for(final Peak p : features) {
			if(p instanceof Feature) {
				final Feature f = (Feature)p;
				result.addAll(f.getMembers());
			} else {
				result.add(p);
			}
		}
		return result;
	}

	public static double getHighestMZ(final Feature f) {

		if(f == null) {
			throw new NullPointerException();
		}
		return UtilCollection.getHighest(f.getMembers(), new ComparatorPeakByMZ()).getMz();
	}

	public static int getLength(final Peak peak) {

		if(peak instanceof Feature) {
			final Feature f = (Feature)peak;
			synchronized(f) {
				return f.getIndexLast() - f.getIndexFirst() + 1;
			}
		}
		return 1;
	}

	public static double getLowestMZ(final Feature f) {

		if(f == null) {
			throw new NullPointerException();
		}
		return UtilCollection.getLowest(f.getMembers(), new ComparatorPeakByMZ()).getMz();
	}

	public static Collection<Peak> getPeaks(final Collection<? extends Feature> features) {

		final Collection<Peak> result = UtilCollection.newCollection();
		for(final Feature f : features) {
			result.addAll(f.getMembers());
		}
		return result;
	}

	public static List<Spectrum> getSpectra(final Collection<? extends Peak> peaks) {

		return getSpectra(peaks, true);
	}

	public static List<Spectrum> getSpectra(final Collection<? extends Peak> peaks, final boolean nonEmpty) {

		final List<Spectrum> result = UtilList.newList();
		for(final Peak p : peaks) {
			if(p instanceof PeakFractionated) {
				if(p instanceof Feature) {
					for(final Peak pp : (Feature)p) {
						if(pp instanceof PeakFractionated) {
							if(((PeakFractionated)pp).getSpectrum() != null && !((PeakFractionated)pp).getSpectrum().getPeaks().isEmpty()) {
								result.add(((PeakFractionated)pp).getSpectrum());
							}
						}
					}
				} else {
					if(p instanceof PeakFractionated) {
						if(((PeakFractionated)p).getSpectrum() != null && !((PeakFractionated)p).getSpectrum().getPeaks().isEmpty()) {
							result.add(((PeakFractionated)p).getSpectrum());
						}
					}
				}
			}
		}
		return result;
	}

	public static List<Spectrum> getSpectra(final Peak peak) {

		return getSpectra(peak, true);
	}

	public static List<Spectrum> getSpectra(final Peak peak, final boolean nonEmpty) {

		return getSpectra(Arrays.asList(peak), nonEmpty);
	}

	public static boolean isNullFeature(final Feature feature) {

		return feature == null || UtilCollection.nullCollection(feature.getMembers()) || feature.getMembers().isEmpty();
	}

	public static boolean onlyFeatures(final Collection<? extends Peak> peaks) {

		for(final Peak p : peaks) {
			if(p instanceof Feature) {
				// ok
			} else {
				return false;
			}
		}
		return true;
	}

	private UtilFeature() {
	}
}
