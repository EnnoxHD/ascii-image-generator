package com.github.ennoxhd.aig;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Provides general GUI utilities.
 */
final class GuiUtils {
	
	/**
	 * Private default constructor (not used).
	 */
	private GuiUtils() {}
	
	/**
	 * Tracks if the GUI is initialized.
	 * @see #initializeGui()
	 */
	private static boolean isInitialized = false;
	
	/**
	 * Sets the system default LaF.
	 */
	static final void initializeGui() {
		if(isInitialized) return;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// do nothing
		} finally {
			isInitialized = true;
		}
	}
}
