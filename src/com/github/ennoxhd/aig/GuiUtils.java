package com.github.ennoxhd.aig;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

final class GuiUtils {
	
	private static boolean isInitialized = false;
	
	static void initializeGui() {
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
}
