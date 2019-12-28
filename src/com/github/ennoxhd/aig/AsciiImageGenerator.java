package com.github.ennoxhd.aig;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

public final class AsciiImageGenerator {
	
	private static Optional<File> getOutputFile(final File inputFile) {
		if(inputFile == null) return Optional.empty();
		Optional<File> optTxtFile = FileUtils.toTxtFile(inputFile);
		if(optTxtFile.isEmpty()) return Optional.empty();
		return FileUtils.nextFile(optTxtFile.get());
	}
	
	public static void main(final String[] args) {
		try {
			Optional<File> optImageFile = GuiUtils.chooseImageFileDialog();
			if(optImageFile.isEmpty()) System.exit(1);
			Optional<File> optTextFile = getOutputFile(optImageFile.get());
			if(optTextFile.isEmpty()) System.exit(2);
			Optional<BufferedImage> optImage = FileUtils.getImageFromFile(optImageFile.get());
			if(optImage.isEmpty()) System.exit(3);
			Optional<String[]> optAsciiImage = Converter.convertToAscii(optImage.get());
			if(optAsciiImage.isEmpty()) System.exit(4);
			if(!FileUtils.writeToFile(optAsciiImage.get(), optTextFile.get())) System.exit(5);
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(6);
		}
		System.exit(0);
	}
}
