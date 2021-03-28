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

final class FileUtils {
	
	static Optional<BufferedImage> getImageFromFile(final File imageFile,
			final Point2D.Double scalingFactors) {
		if(imageFile == null) Optional.empty();
		try {
			BufferedImage image = ImageIO.read(imageFile);
			AffineTransformOp transformOp = new AffineTransformOp(
				AffineTransform.getScaleInstance(scalingFactors.x, scalingFactors.y),
				AffineTransformOp.TYPE_BILINEAR);
			BufferedImage imageTransformed = transformOp.filter(image, null);
			return Optional.ofNullable(imageTransformed);
		} catch (Exception e) {
			return Optional.empty();
		}
	}
	
	static Optional<File> toTxtFile(final File file) {
		if(file == null) Optional.empty();
		if(!file.isFile()) Optional.empty();
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
	
	private static Optional<String> createFileName(final File file, final int n) {
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
	
	static Optional<File> nextFile(final File file) {
		if(file == null) Optional.empty();
		if(!file.exists()) Optional.of(file);
		for(int existingFileCount = 0; existingFileCount >= 0; existingFileCount++) {
			Optional<String> optFileName = createFileName(file, existingFileCount);
			if(optFileName.isEmpty()) return Optional.empty();
			File nextFile = new File(optFileName.get());
			if(!nextFile.exists()) return Optional.of(nextFile);
		}
		return Optional.empty();
	}
	
	static Optional<File> getOutputFile(final File inputFile) {
		if(inputFile == null) return Optional.empty();
		Optional<File> optTxtFile = FileUtils.toTxtFile(inputFile);
		if(optTxtFile.isEmpty()) return Optional.empty();
		return FileUtils.nextFile(optTxtFile.get());
	}
	
	static boolean writeToFile(final String[] lines, final File file) {
		if(lines == null || file == null) return false;
		try {
			FileWriter fileWriter = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			for(String line : lines) {
				writer.append(line);
				writer.newLine();
			}
			writer.close();
			fileWriter.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
