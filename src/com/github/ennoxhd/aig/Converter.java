package com.github.ennoxhd.aig;

import java.awt.image.BufferedImage;
import java.util.Optional;

final class Converter {

	static Optional<String[]> convertToAscii(BufferedImage image) {
		if(image.getWidth() < 1 || image.getHeight() < 1) return Optional.empty();
		String[] asciiImage = new String[image.getHeight()];
		for(int y = 0; y < image.getHeight(); y++) {
			String line = "";
			for(int x = 0; x < image.getWidth(); x++) {
				int gray = Grayscale.srgbToGrayscale(image.getRGB(x, y)) & 0xff;
				int characterIdx = Quantizer.quantize(gray, 255, CharacterMapper.Modus.DEFAULT.levels());
				line += CharacterMapper.mapToChar(characterIdx);
			}
			asciiImage[y] = line;
		}
		return Optional.of(asciiImage);
	}
}
