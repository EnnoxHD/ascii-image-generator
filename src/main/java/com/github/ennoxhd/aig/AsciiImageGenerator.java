package com.github.ennoxhd.aig;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Main application class with the {@link #main(String[])} method.
 */
public final class AsciiImageGenerator {
	
	/**
	 * Private default constructor (not used).
	 */
	private AsciiImageGenerator() {}
	
	/**
	 * This is the main entry point for application execution.
	 * Performs a linear workflow for configuration and conversion.
	 * Potential errors are presented through dialogs to the user.
	 * @param args command line arguments (not used)
	 * @see Dialogs#errorDialog(Exception)
	 * @see AsciiImageGeneratorException
	 */
	public static void main(final String[] args) {
		try {
			final File imageFile = Dialogs.chooseImageFileDialog()
					.orElseThrow(() -> new AsciiImageGeneratorException("No image file was chosen."));
			final Point2D.Double scalingFactors = Dialogs.chooseScalingFactorsDialog()
					.orElseThrow(() -> new AsciiImageGeneratorException("No scaling factors were chosen."));
			final ImageConversionMethods methods = Dialogs.chooseMethodsDialog()
					.orElseThrow(() -> new AsciiImageGeneratorException("No image conversion methods chosen."));
			final File textFile = FileUtils.getOutputFile(imageFile)
					.orElseThrow(() -> new AsciiImageGeneratorException("Could not determine the output file name."));
			final BufferedImage image = FileUtils.getImageFromFile(imageFile, scalingFactors, methods.getInterpolationType())
					.orElseThrow(() -> new AsciiImageGeneratorException("Could not load image from file."));
			final String[] asciiImage = Converter.convertToAscii(image, methods.getCharacterMode(), methods.getQuantizerMethod())
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
	
	/**
	 * Custom exception for this application.
	 */
	@SuppressWarnings("serial")
	private static final class AsciiImageGeneratorException extends Exception {
		
		/**
		 * Constructs a custom exception from the given parameters.
		 * @param message message for display to the user
		 * @see Exception#Exception(String)
		 */
		private AsciiImageGeneratorException(final String message) {
	        super(message);
	    }
		
		/**
		 * Constructs a custom exception from the given parameters.
		 * @param message message for display to the user
		 * @param cause stack trace for detailed analysis of the error and for display to the user
		 * @see Exception#Exception(String, Throwable)
		 */
		private AsciiImageGeneratorException(final String message, final Throwable cause) {
	        super(message, cause);
	    }
	}
}
