package com.github.ennoxhd.aig;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class AsciiImageGenerator {
	
	public static void main(final String[] args) {
		try {
			File imageFile = Dialogs.chooseImageFileDialog().orElseThrow();
			Point2D.Double scalingFactors = Dialogs.chooseScalingFactorsDialog().orElseThrow();
			File textFile = FileUtils.getOutputFile(imageFile).orElseThrow();
			BufferedImage image = FileUtils.getImageFromFile(imageFile, scalingFactors).orElseThrow();
			String[] asciiImage = Converter.convertToAscii(image).orElseThrow();
			if(!FileUtils.writeToFile(asciiImage, textFile))
				throw new IOException("Could not write to file.");
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}
}
