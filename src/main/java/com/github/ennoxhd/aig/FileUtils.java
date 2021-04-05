package com.github.ennoxhd.aig;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

/**
 * Provides utilities for interaction with the file system.
 */
final class FileUtils {
	
	/**
	 * Private default constructor (not used).
	 */
	private FileUtils() {}
	
	/**
	 * Loads an image from file if it exists and scales it.
	 * @param imageFile image file to load
	 * @param scalingFactors scaling factors for width ({@link Point2D.Double#x}) and
	 * height ({@link Point2D.Double#y})
	 * @param interpolationType interpolation type to use for scaling the image
	 * @return the scaled image
	 */
	static final Optional<BufferedImage> getImageFromFile(final File imageFile,
			final Point2D.Double scalingFactors,
			final ImageConversionMethods.InterpolationType interpolationType) {
		if(imageFile == null) Optional.empty();
		final Point2D.Double scalingFactorsToUse = scalingFactors == null ?
				new Point2D.Double(1.0, 1.0) : scalingFactors;
		final ImageConversionMethods.InterpolationType interpolationTypeToUse = interpolationType == null ?
				ImageConversionMethods.InterpolationType.DEFAULT : interpolationType;
		try {
			final BufferedImage image = ImageIO.read(imageFile);
			final AffineTransformOp transformOp = new AffineTransformOp(
				AffineTransform.getScaleInstance(scalingFactorsToUse.x, scalingFactorsToUse.y),
				interpolationTypeToUse.getType());
			final BufferedImage imageTransformed = transformOp.filter(image, null);
			return Optional.ofNullable(imageTransformed);
		} catch (final Exception e) {
			return Optional.empty();
		}
	}
	
	/**
	 * Builds a {@code *.txt} file out of a given file.
	 * @param file file with or without extension
	 * @return filename with {@code *.txt} extension
	 */
	private static final Optional<File> toTxtFile(final File file) {
		if(file == null || !file.isFile()) Optional.empty();
		final String txtExtension = ".txt";
		String newFile = null;
		int cutIdx = -1;
		if((cutIdx = file.getName().lastIndexOf(".")) <= 0) {
			newFile = file.getPath() + txtExtension;
		} else {
			String parent = file.getParent();
			if(parent == null) parent = "";
			newFile = parent + File.separator
					+ file.getName().substring(0, cutIdx) + txtExtension;
		}
		return Optional.of(new File(newFile));
	}
	
	/**
	 * Creates a new file name with a number appended.
	 * Preserves the file extension and changes only the name.
	 * @param file original file
	 * @param n number to append (must be positive)
	 * @return the new file name
	 * <ul>
	 * <li>if {@code file==null}:</li>
	 * <li><ul>
	 * <li>if {@code n==0}: empty Optional</li>
	 * <li>else: {@code n} as the filename</li>
	 * </ul></li>
	 * <li>else:
	 * <li><ul>
	 * <li>if {@code n==0}: the name of {@code file} itself</li>
	 * <li>else: new filename with appended {@code n} and extension (default case)</li>
	 * </ul></li>
	 * </ul>
	 */
	private static final Optional<String> createFileName(final File file, final int n) {
		if(n < 0) return Optional.empty();
		if(file == null) {
			if(n == 0) return Optional.empty();
			return Optional.of(String.valueOf(n));
		}
		if(n == 0) return Optional.of(file.getPath());
		String newFile = null;
		int cutIdx = -1;
		if((cutIdx = file.getName().lastIndexOf(".")) <= 0) {
			newFile = file.getPath() + n;
		} else {
			String parent = file.getParent();
			if(parent == null) parent = "";
			newFile = parent + File.separator
					+ file.getName().substring(0, cutIdx) + n
					+ file.getName().substring(cutIdx, file.getName().length());
		}
		return Optional.of(newFile);
	}
	
	/**
	 * Creates a new filename with an appended number and
	 * tries as long as there already exists such a file.
	 * @param file original file
	 * @return new file with number and extension that does not exist
	 * @see #createFileName(File, int)
	 */
	private static final Optional<File> nextFile(final File file) {
		if(file == null) Optional.empty();
		if(!file.exists()) Optional.of(file);
		for(int existingFileCount = 0; existingFileCount >= 0; existingFileCount++) {
			final Optional<String> optFileName = createFileName(file, existingFileCount);
			if(optFileName.isEmpty()) return Optional.empty();
			final File nextFile = new File(optFileName.get());
			if(!nextFile.exists()) return Optional.of(nextFile);
		}
		return Optional.empty();
	}
	
	/**
	 * Builds a new output file based on an original one.
	 * The output file gets a {@code *.txt} extension and
	 * a number appended to the name if such file already exists.
	 * @param inputFile original file
	 * @return new file
	 * @see #toTxtFile(File)
	 * @see #nextFile(File)
	 */
	static final Optional<File> getOutputFile(final File inputFile) {
		if(inputFile == null) return Optional.empty();
		final Optional<File> optTxtFile = FileUtils.toTxtFile(inputFile);
		if(optTxtFile.isEmpty()) return Optional.empty();
		return FileUtils.nextFile(optTxtFile.get());
	}
	
	/**
	 * Writes text to a file.
	 * @param lines the lines of the text, each entry corresponds to one line
	 * @param file file to write the text to
	 * @return {@code true} if successful, {@code false} otherwise
	 */
	static final boolean writeToFile(final String[] lines, final File file) {
		if(lines == null || file == null) return false;
		try(final FileWriter fileWriter = new FileWriter(file);
				final BufferedWriter writer = new BufferedWriter(fileWriter);) {
			for(final String line : lines) {
				writer.append(line);
				writer.newLine();
			}
			writer.close();
			fileWriter.close();
		} catch (final IOException e) {
			return false;
		}
		return true;
	}
}
