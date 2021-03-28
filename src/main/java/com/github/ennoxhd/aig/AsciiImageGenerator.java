package com.github.ennoxhd.aig;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

public final class AsciiImageGenerator {
	
	public static void main(final String[] args) {
		try {
			Optional<File> optImageFile = Dialogs.chooseImageFileDialog();
			Optional<Point2D.Double> optScaling = Dialogs.chooseScalingFactorsDialog();
			if(optImageFile.isEmpty() || optScaling.isEmpty()) System.exit(1);
			Optional<File> optTextFile = FileUtils.getOutputFile(optImageFile.get());
			if(optTextFile.isEmpty()) System.exit(2);
			Optional<BufferedImage> optImage = FileUtils.getImageFromFile(optImageFile.get(), optScaling.get());
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
