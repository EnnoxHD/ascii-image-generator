package com.github.ennoxhd.aig;

/**
 * Provides utilities for the conversion of an ARGB 32 bit color
 * to a 32 bit grayscale one.
 */
final class Grayscale {
	
	/**
	 * Private default constructor (not used).
	 */
	private Grayscale() {}
	
	/**
	 * Number of bits per channel.
	 */
	private static final int BITS_COMPONENT = 8;
	
	/**
	 * Bit mask for the red channel.
	 */
	private static final int MASK_R = 0xff0000;
	
	/**
	 * Bit mask for the green channel.
	 */
	private static final int MASK_G = 0xff00;
	
	/**
	 * Bit mask for the blue channel.
	 */
	private static final int MASK_B = 0xff;
	
	/**
	 * Bit mask to get the value of a known gray color.
	 */
	static final int MASK_GRAY_VALUE = MASK_B;
	
	/**
	 * Maximum value of a color channel.
	 */
	static final int MAX_COMPONENT = MASK_B;
	
	/**
	 * Maximum {@code double} value of a color channel.
	 */
	private static final double MAX_COMPONENT_D = MAX_COMPONENT;

	/**
	 * Converts a color into the individual color components and normalizes its values.
	 * @param srgb the color to be converted
	 * @return three values for the color channels ranging from {@code 0.0} to {@code 1.0}
	 */
	private static final double[] srgbToCSrgb(final int srgb) {
		final double rSrgb = ((double) ((srgb & MASK_R) >>> (2 * BITS_COMPONENT))) / MAX_COMPONENT_D;
		final double gSrgb = ((double) ((srgb & MASK_G) >>> BITS_COMPONENT)) / MAX_COMPONENT_D;
		final double bSrgb = ((double) (srgb & MASK_B)) / MAX_COMPONENT_D;
		return new double[]{rSrgb, gSrgb, bSrgb};
	}
	
	/**
	 * Removes the gamma compression and transforms the component to linear colorspace.
	 * @param cSrgb gamma compressed component value
	 * @return component value in linear colorspace
	 * @see #gammaCompression(double)
	 */
	private static final double gammaExpansion(final double cSrgb) {
		double cLinear = 0.0d;
		if(cSrgb <= 0.04045d) {
			cLinear = cSrgb / 12.92d;
		} else {
			cLinear = Math.pow((cSrgb + 0.055d) / 1.055d, 2.4d);
		}
		return cLinear;
	}
	
	/**
	 * Removes the gamma compression for each component and transforms them to linear colorspace.
	 * @param cSrgb gamma compressed components
	 * @return components in linear colorspace
	 * @see #gammaCompression(double)
	 */
	private static final double[] gammaExpansion(final double[] cSrgb) {
		final double[] cLinear = new double[cSrgb.length];
		for(int i = 0; i < cSrgb.length; i++) {
			cLinear[i] = gammaExpansion(cSrgb[i]);
		}
		return cLinear;
	}
	
	/**
	 * Transforms the linear-intensity of each component to an overall linear luminance.
	 * @param rLinear linear-intensity of the red channel
	 * @param gLinear linear-intensity of the green channel
	 * @param bLinear linear-intensity of the blue channel
	 * @return overall linear luminance
	 */
	private static final double linearLuminance(final double rLinear, final double gLinear, final double bLinear) {
		final double yLinear = 0.2126d * rLinear + 0.7152d * gLinear + 0.0722d * bLinear;
		return yLinear;
	}
	
	/**
	 * Transforms the linear-intensity of each component to an overall linear luminance.
	 * @param cLinear linear-intensity of each channel
	 * @return overall linear luminance
	 */
	private static final double linearLuminance(final double[] cLinear) {
		return linearLuminance(cLinear[0], cLinear[1], cLinear[2]);
	}
	
	/**
	 * Gamma-compresses the linear luminance and transforms it to non-linear colorspace.
	 * @param yLinear linear luminance
	 * @return gamma-compressed luminance in non-linear colorspace
	 * @see #gammaExpansion(double)
	 */
	private static final double gammaCompression(final double yLinear) {
		double ySrgb = 0.0d;
		if(yLinear <= 0.0031308d) {
			ySrgb = 12.92d * yLinear;
		} else {
			ySrgb = 1.055d * Math.pow(yLinear, 1.0d / 2.4d) - 0.055d;
		}
		return ySrgb;
	}
	
	/**
	 * Converts the gamma-compressed luminance back to a gray color.
	 * Sets the alpha channel to be fully opaque and
	 * sets all color components to the given luminance.
	 * @param ySrgb gamma-compressed luminance
	 * @return fully opaque gray color
	 */
	private static final int ySrgbToGrayscale(final double ySrgb) {
		int grayScale = MAX_COMPONENT;
		for(int i = 0; i < 3; i++) {
			grayScale <<= BITS_COMPONENT;
			grayScale |= (((int) Math.round(ySrgb * MAX_COMPONENT_D)) & MASK_GRAY_VALUE);
		}
		return grayScale;
	}
	
	/**
	 * Converts any ARGB 32 bit color to a fully opaque grayscale one.
	 * <br/>
	 * The steps involved are:
	 * <ol>
	 * <li>normalization of color components</li>
	 * <li>transformation to linear colorspace through gamma expansion</li>
	 * <li>calculation of the overall linear luminance</li>
	 * <li>transformation to non-linear colorspace through gamma compression</li>
	 * <li>denormalization and building of a fully opaque grayscale color</li>
	 * </ol>
	 * @param srgb original color
	 * @return fully opaque grayscale color
	 */
	static final int srgbToGrayscale(final int srgb) {
		final double[] cSrgb = srgbToCSrgb(srgb);
		final double[] cLinear = gammaExpansion(cSrgb);
		final double yLinear = linearLuminance(cLinear);
		final double ySrgb = gammaCompression(yLinear);
		final int grayscale = ySrgbToGrayscale(ySrgb);
		return grayscale;
	}
}
