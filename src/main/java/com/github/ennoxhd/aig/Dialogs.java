package com.github.ennoxhd.aig;

import java.io.File;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

final class Dialogs {

	static Optional<File> chooseImageFileDialog() {
		GuiUtils.initializeGui();
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
