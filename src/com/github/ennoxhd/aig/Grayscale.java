package com.github.ennoxhd.aig;

final class Grayscale {

	private static double[] srgbToCSrgb(final int srgb) {
		double rSrgb = ((double) ((srgb & 0xff0000) >>> 16)) / 255.0d;
		double gSrgb = ((double) ((srgb & 0xff00) >>> 8)) / 255.0d;
		double bSrgb = ((double) (srgb & 0xff)) / 255.0d;
		return new double[]{rSrgb, gSrgb, bSrgb};
	}
	
	private static double gammaExpansion(final double cSrgb) {
		double cLinear = 0.0d;
		if(cSrgb <= 0.04045d) {
			cLinear = cSrgb / 12.92d;
		} else {
			cLinear = Math.pow((cSrgb + 0.055d) / 1.055d, 2.4d);
		}
		return cLinear;
	}
	
	private static double[] gammaExpansion(final double[] cSrgb) {
		double[] cLinear = new double[cSrgb.length];
		for(int i = 0; i < cSrgb.length; i++) {
			cLinear[i] = gammaExpansion(cSrgb[i]);
		}
		return cLinear;
	}
	
	private static double linearLuminance(final double rLinear, final double gLinear, final double bLinear) {
		double yLinear = 0.2126d * rLinear + 0.7152d * gLinear + 0.0722d * bLinear;
		return yLinear;
	}
	
	private static double linearLuminance(final double[] cLinear) {
		return linearLuminance(cLinear[0], cLinear[1], cLinear[2]);
	}
	
	private static double gammaCompression(final double yLinear) {
		double ySrgb = 0.0d;
		if(yLinear <= 0.0031308d) {
			ySrgb = 12.92d * yLinear;
		} else {
			ySrgb = 1.055d * Math.pow(yLinear, 1.0d / 2.4d) - 0.055d;
		}
		return ySrgb;
	}
	
	private static int ySrgbToGrayscale(final double ySrgb) {
		int grayScale = 0xff;
		for(int i = 0; i < 3; i++) {
			grayScale <<= 8;
			grayScale |= (((int) Math.round(ySrgb * 255.0d)) & 0xff);
		}
		return grayScale;
	}
	
	static int srgbToGrayscale(final int srgb) {
		int grayscale = 0;
		double[] cSrgb = srgbToCSrgb(srgb);
		double[] cLinear = gammaExpansion(cSrgb);
		double yLinear = linearLuminance(cLinear);
		double ySrgb = gammaCompression(yLinear);
		grayscale = ySrgbToGrayscale(ySrgb);
		return grayscale;
	}
}
