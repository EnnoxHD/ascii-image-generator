package com.github.ennoxhd.aig;

import java.io.File;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

final class GuiUtils {
	
	private static boolean isInitialized = false;
	
	private static void initializeGui() {
		if(isInitialized) return;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// do nothing
		} finally {
			isInitialized = true;
		}
	}
	
	static Optional<File> chooseImageFileDialog() {
		initializeGui();
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Ascii Image Generator");
		FileFilter imageFileFilter = new FileNameExtensionFilter("Image file",
				"bmp", "gif", "jpg", "jpeg", "png", "tiff", "wbmp");
		chooser.addChoosableFileFilter(imageFileFilter);
		chooser.setFileFilter(imageFileFilter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		if(chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			return Optional.empty();
		} else {
			File selectedFile = chooser.getSelectedFile();
			if(selectedFile.isFile()) {
				return Optional.of(selectedFile);
			} else {
				return Optional.empty();
			}
		}
	}
}
