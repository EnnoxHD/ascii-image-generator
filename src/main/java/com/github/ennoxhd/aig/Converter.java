package com.github.ennoxhd.aig;

import java.awt.image.BufferedImage;
import java.util.Optional;

import com.github.ennoxhd.aig.CharacterMapper.Mode;
import com.github.ennoxhd.aig.Quantizer.Method;

/**
 * Converts an image to ASCII art.
 * @see #convertToAscii(BufferedImage)
 */
final class Converter {
	
	/**
	 * Private default constructor (not used).
	 */
	private Converter() {}

	/**
	 * Converts an image to an array of strings.
	 * The width and height of the image are preserved in the textual representation.
	 * @param image image to generate ASCII art from
	 * @return text of the ASCII art version of the original image
	 */
	static final Optional<String[]> convertToAscii(final BufferedImage image) {
		if(image == null || image.getWidth() < 1 || image.getHeight() < 1) return Optional.empty();
		final String[] asciiImage = new String[image.getHeight()];
		for(int y = 0; y < image.getHeight(); y++) {
			String line = "";
			for(int x = 0; x < image.getWidth(); x++) {
				final int gray = Grayscale.srgbToGrayscale(image.getRGB(x, y)) & Grayscale.MASK_GRAY_VALUE;
				final int characterIdx = Quantizer.quantize(gray, Grayscale.MAX_COMPONENT,
						Mode.DEFAULT.seriesLength(), Method.DEFAULT);
				line += Mode.DEFAULT.apply(characterIdx);
			}
			asciiImage[y] = line;
		}
		return Optional.of(asciiImage);
	}
}
