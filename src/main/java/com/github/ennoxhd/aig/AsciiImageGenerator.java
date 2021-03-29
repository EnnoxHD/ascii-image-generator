package com.github.ennoxhd.aig;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;

public final class AsciiImageGenerator {
	
	public static void main(final String[] args) {
		try {
			File imageFile = Dialogs.chooseImageFileDialog()
					.orElseThrow(() -> new AsciiImageGeneratorException("No image file was chosen."));
			Point2D.Double scalingFactors = Dialogs.chooseScalingFactorsDialog()
					.orElseThrow(() -> new AsciiImageGeneratorException("No scaling factors were chosen."));
			File textFile = FileUtils.getOutputFile(imageFile)
					.orElseThrow(() -> new AsciiImageGeneratorException("Could not determine the output file name."));
			BufferedImage image = FileUtils.getImageFromFile(imageFile, scalingFactors)
					.orElseThrow(() -> new AsciiImageGeneratorException("Could not load image from file."));
			String[] asciiImage = Converter.convertToAscii(image)
					.orElseThrow(() -> new AsciiImageGeneratorException("Could not convert image to ASCII characters."));
			if(!FileUtils.writeToFile(asciiImage, textFile))
				throw new AsciiImageGeneratorException("Could not write to output file.");
			Dialogs.successDialog(textFile.getName());
		} catch(Exception e) {
			if(!(e instanceof AsciiImageGeneratorException))
				e = new AsciiImageGeneratorException("An unexpected error has occurred.", e);
			Dialogs.errorDialog(e);
		}
	}
	
	private static class AsciiImageGeneratorException extends Exception {
		private static final long serialVersionUID = 1L;

		public AsciiImageGeneratorException(String message) {
	        super(message);
	    }
		
		public AsciiImageGeneratorException(String message, Throwable cause) {
	        super(message, cause);
	    }
	}
}
