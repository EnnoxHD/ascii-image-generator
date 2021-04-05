package com.github.ennoxhd.aig;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;

import com.github.ennoxhd.aig.ImageConversionMethods.InterpolationType;

/**
 * Provides several dialogs for interaction with the user.
 */
final class Dialogs {
	
	/**
	 * Private default constructor (not used).
	 */
	private Dialogs() {}
	
	/**
	 * Converts an {@link Exception} to a string containing the stack trace. 
	 * @param e exception to be converted
	 * @return the stack trace of the exception
	 */
	private static final String exceptionToString(final Exception e) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	
	/**
	 * Displays an error dialog with the exception message and
	 * stack trace of a given exception.
	 * @param e exception to be displayed to the user
	 */
	static final void errorDialog(final Exception e) {
		try {
			GuiUtils.initializeGui();
			final JPanel panel = new JPanel();
			final GroupLayout layout = new GroupLayout(panel);
			layout.setAutoCreateContainerGaps(true);
			layout.setAutoCreateGaps(true);
			panel.setLayout(layout);
			final JLabel message = new JLabel(e.getMessage());
			final Font messageFont = message.getFont();
			message.setFont(messageFont.deriveFont(messageFont.getStyle() | Font.BOLD));
			final String stackTrace = exceptionToString(e);
			final JTextArea details = new JTextArea(stackTrace, 15, 80);
			final JScrollPane scrollPane = new JScrollPane(details);
			layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
					.addComponent(message)
					.addComponent(scrollPane));
			layout.setVerticalGroup(layout.createSequentialGroup()
					.addComponent(message)
					.addComponent(scrollPane));
			JOptionPane.showMessageDialog(null, panel,
					"AsciiImageGenerator - Error", JOptionPane.ERROR_MESSAGE);
		} catch(final Exception ex) {
			return;
		}
	}
	
	/**
	 * Displays a success dialog and informs the user about successful conversion.
	 * @param fileName the file name of the generated file
	 */
	static final void successDialog(final String fileName) {
		String message = "The image file has been converted to ASCII art.";
		if(fileName != null && !fileName.isBlank())
			message += "\nFile: " + fileName;
		JOptionPane.showMessageDialog(null, message, "AsciiImageGenerator",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Displays an open file dialog to choose an image file.
	 * @return the image file if the user choose one
	 */
	static final Optional<File> chooseImageFileDialog() {
		GuiUtils.initializeGui();
		final JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Ascii Image Generator");
		final FileFilter imageFileFilter = new FileNameExtensionFilter("Image file",
				"bmp", "gif", "jpg", "jpeg", "png", "tiff", "wbmp");
		chooser.addChoosableFileFilter(imageFileFilter);
		chooser.setFileFilter(imageFileFilter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		if(chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			return Optional.empty();
		} else {
			final File selectedFile = chooser.getSelectedFile();
			if(selectedFile.isFile()) {
				return Optional.of(selectedFile);
			} else {
				return Optional.empty();
			}
		}
	}
	
	/**
	 * Displays a dialog to set the scaling factors for the resulting image.
	 * @return scaling factors for width ({@link Point2D.Double#x}) and
	 * height ({@link Point2D.Double#y})
	 */
	static final Optional<Point2D.Double> chooseScalingFactorsDialog() {
		GuiUtils.initializeGui();
		
		final AbstractFormatterFactory formatterFactory = new AbstractFormatterFactory() {
			@Override
			public final AbstractFormatter getFormatter(final JFormattedTextField tf) {
				final DecimalFormat format = new DecimalFormat();
				format.setDecimalSeparatorAlwaysShown(false);
				format.setGroupingUsed(false);
				format.setMaximumFractionDigits(0);
				format.setMinimumFractionDigits(0);
				format.setMaximumIntegerDigits(3);
				format.setMinimumIntegerDigits(1);
				final NumberFormatter formatter = new NumberFormatter(format);
				formatter.setMinimum(Integer.valueOf(5));
				formatter.setMaximum(Integer.valueOf(500));
				formatter.setAllowsInvalid(true);
				formatter.setCommitsOnValidEdit(true);
				return formatter;
			}
		};
		
		final JPanel panel = new JPanel();
		final GroupLayout layout = new GroupLayout(panel);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		panel.setLayout(layout);
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Scaling factors"));
		
		final JLabel widthLabel = new JLabel("Width:");
		final JFormattedTextField widthField = new JFormattedTextField(formatterFactory);
		widthField.setValue(Integer.valueOf(100));
		widthField.setColumns(3);
		final JLabel widthPercentSign = new JLabel("%");
		final JSlider widthSlider = new JSlider(5, 500, 100);
		widthSlider.addChangeListener(e -> {
			widthField.setText(String.valueOf(widthSlider.getValue()));
		});
		widthField.addFocusListener(new FocusListener() {
			@Override
			public final void focusGained(final FocusEvent e) {
				return;
			}
			@Override
			public final void focusLost(final FocusEvent e) {
				try {
					widthField.commitEdit();
					widthSlider.setValue(Integer.valueOf(widthField.getText()));
				} catch (final ParseException | NumberFormatException ex) {
					widthField.setValue(widthSlider.getValue());
				}
			}
		});
		final JButton widthReset = new JButton("Reset");
		widthReset.addActionListener(e -> {
			widthSlider.setValue(100);
		});
		
		final JLabel heightLabel = new JLabel("Height:");
		final JFormattedTextField heightField = new JFormattedTextField(formatterFactory);
		heightField.setValue(Integer.valueOf(100));
		heightField.setColumns(3);
		final JLabel heightPercentSign = new JLabel("%");
		final JSlider heightSlider = new JSlider(5, 500, 100);
		heightSlider.addChangeListener(e -> {
			heightField.setText(String.valueOf(heightSlider.getValue()));
		});
		heightField.addFocusListener(new FocusListener() {
			@Override
			public final void focusGained(final FocusEvent e) {
				return;
			}
			@Override
			public final void focusLost(final FocusEvent e) {
				try {
					heightField.commitEdit();
					heightSlider.setValue(Integer.valueOf(heightField.getText()));
				} catch (final ParseException | NumberFormatException ex) {
					heightField.setValue(heightSlider.getValue());
				}
			}
		});
		final JButton heightReset = new JButton("Reset");
		heightReset.addActionListener(e -> {
			heightSlider.setValue(100);
		});
		final JButton heightHalf = new JButton("Half height");
		heightHalf.addActionListener(e -> {
			heightSlider.setValue(Math.round(widthSlider.getValue() / 2.0f));
		});
		final JButton heightProportional = new JButton("Proportional");
		heightProportional.addActionListener(e -> {
			heightSlider.setValue(widthSlider.getValue());
		});
		
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.TRAILING)
					.addComponent(widthLabel)
					.addComponent(heightLabel))
				.addGroup(layout.createParallelGroup(Alignment.TRAILING)
					.addComponent(widthField)
					.addComponent(heightField))
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
					.addComponent(widthPercentSign)
					.addComponent(heightPercentSign))
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
					.addComponent(widthSlider)
					.addComponent(heightSlider)
					.addGroup(layout.createSequentialGroup()
						.addComponent(heightHalf)
						.addComponent(heightProportional)
					))
				.addGroup(layout.createParallelGroup(Alignment.TRAILING)
					.addComponent(widthReset)
					.addComponent(heightReset))
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(widthLabel)
					.addComponent(widthField)
					.addComponent(widthPercentSign)
					.addComponent(widthSlider, Alignment.CENTER)
					.addComponent(widthReset))
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(heightLabel)
					.addComponent(heightField)
					.addComponent(heightPercentSign)
					.addComponent(heightSlider, Alignment.CENTER)
					.addComponent(heightReset))
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(heightHalf)
					.addComponent(heightProportional))
		);
		
		final int result = JOptionPane.showConfirmDialog(null, panel, "Scaling factors",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if(result == JOptionPane.OK_OPTION) {
			final Point2D.Double scale = new Point2D.Double(widthSlider.getValue() / 100.0,
					heightSlider.getValue() / 100.0);
			return Optional.of(scale);
		}
		return Optional.empty();
	}
	
	/**
	 * Displays a dialog to configure which image manipulation methods to use during conversion.
	 * @return the configuration
	 * @see ImageConversionMethods
	 */
	static final Optional<ImageConversionMethods> chooseMethodsDialog() {
		GuiUtils.initializeGui();
		
		final JPanel interpolationTypePanel = new JPanel();
		final GroupLayout interpolationTypeLayout = new GroupLayout(interpolationTypePanel);
		interpolationTypeLayout.setAutoCreateContainerGaps(true);
		interpolationTypeLayout.setAutoCreateGaps(true);
		interpolationTypePanel.setLayout(interpolationTypeLayout);
		interpolationTypePanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Interpolation type for image scaling"));
		final JRadioButton interpolationTypeBicubic = new JRadioButton("Bicubic");
		final JRadioButton interpolationTypeBilinear = new JRadioButton("Bilinear");
		final JRadioButton interpolationTypeNearestNeighbor = new JRadioButton("Nearest Neighbor");
		final Font interpolationFont = interpolationTypeBilinear.getFont();
		interpolationTypeBilinear.setFont(interpolationFont.deriveFont(interpolationFont.getStyle() | Font.BOLD));
		final ButtonGroup interpolationTypeGroup = new ButtonGroup();
		interpolationTypeGroup.add(interpolationTypeBicubic);
		interpolationTypeGroup.add(interpolationTypeBilinear);
		interpolationTypeGroup.add(interpolationTypeNearestNeighbor);
		interpolationTypeGroup.setSelected(interpolationTypeBilinear.getModel(), true);
		interpolationTypeLayout.setHorizontalGroup(
			interpolationTypeLayout.createSequentialGroup()
				.addComponent(interpolationTypeBicubic)
				.addComponent(interpolationTypeBilinear)
				.addComponent(interpolationTypeNearestNeighbor));
		interpolationTypeLayout.setVerticalGroup(
			interpolationTypeLayout.createParallelGroup()
				.addComponent(interpolationTypeBicubic)
				.addComponent(interpolationTypeBilinear)
				.addComponent(interpolationTypeNearestNeighbor));
		
		final JPanel quantizerMethodPanel = new JPanel();
		final GroupLayout quantizerMethodLayout = new GroupLayout(quantizerMethodPanel);
		quantizerMethodLayout.setAutoCreateContainerGaps(true);
		quantizerMethodLayout.setAutoCreateGaps(true);
		quantizerMethodPanel.setLayout(quantizerMethodLayout);
		quantizerMethodPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Rounding method for quantization"));
		final JRadioButton quantizerMethodCeil = new JRadioButton("Ceil");
		final JRadioButton quantizerMethodFloor = new JRadioButton("Floor");
		final JRadioButton quantizerMethodRound = new JRadioButton("Round");
		final Font quantizerMethodFont = quantizerMethodRound.getFont();
		quantizerMethodRound.setFont(quantizerMethodFont.deriveFont(quantizerMethodFont.getStyle() | Font.BOLD));
		final ButtonGroup quantizerMethodGroup = new ButtonGroup();
		quantizerMethodGroup.add(quantizerMethodCeil);
		quantizerMethodGroup.add(quantizerMethodFloor);
		quantizerMethodGroup.add(quantizerMethodRound);
		quantizerMethodGroup.setSelected(quantizerMethodRound.getModel(), true);
		quantizerMethodLayout.setHorizontalGroup(
				quantizerMethodLayout.createSequentialGroup()
					.addComponent(quantizerMethodCeil)
					.addComponent(quantizerMethodFloor)
					.addComponent(quantizerMethodRound));
		quantizerMethodLayout.setVerticalGroup(
				quantizerMethodLayout.createParallelGroup()
					.addComponent(quantizerMethodCeil)
					.addComponent(quantizerMethodFloor)
					.addComponent(quantizerMethodRound));
		
		final JPanel characterMapperModePanel = new JPanel();
		final GroupLayout characterMapperModeLayout = new GroupLayout(characterMapperModePanel);
		characterMapperModeLayout.setAutoCreateContainerGaps(true);
		characterMapperModeLayout.setAutoCreateGaps(true);
		characterMapperModePanel.setLayout(characterMapperModeLayout);
		characterMapperModePanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Character variation in resulting image"));
		final JRadioButton characterMapperModeDepth10 = new JRadioButton("10 characters");
		final JRadioButton characterMapperModeDepth70 = new JRadioButton("70 characters");
		final Font characterMapperModeFont = characterMapperModeDepth10.getFont();
		characterMapperModeDepth10.setFont(characterMapperModeFont.deriveFont(characterMapperModeFont.getStyle() | Font.BOLD));
		final ButtonGroup characterMapperModeGroup = new ButtonGroup();
		characterMapperModeGroup.add(characterMapperModeDepth10);
		characterMapperModeGroup.add(characterMapperModeDepth70);
		characterMapperModeGroup.setSelected(characterMapperModeDepth10.getModel(), true);
		characterMapperModeLayout.setHorizontalGroup(
				characterMapperModeLayout.createSequentialGroup()
					.addComponent(characterMapperModeDepth10)
					.addComponent(characterMapperModeDepth70));
		characterMapperModeLayout.setVerticalGroup(
				characterMapperModeLayout.createParallelGroup()
					.addComponent(characterMapperModeDepth10)
					.addComponent(characterMapperModeDepth70));
		
		final JPanel mainPanel = new JPanel();
		final GroupLayout mainLayout = new GroupLayout(mainPanel);
		mainLayout.setAutoCreateContainerGaps(true);
		mainLayout.setAutoCreateGaps(true);
		mainPanel.setLayout(mainLayout);
		mainLayout.setHorizontalGroup(
			mainLayout.createParallelGroup(Alignment.CENTER)
				.addComponent(interpolationTypePanel)
				.addComponent(quantizerMethodPanel)
				.addComponent(characterMapperModePanel));
		mainLayout.setVerticalGroup(
			mainLayout.createSequentialGroup()
				.addComponent(interpolationTypePanel)
				.addComponent(quantizerMethodPanel)
				.addComponent(characterMapperModePanel));
		
		double maxWidth = 0.0;
		double maxHeight = 0.0;
		final GroupLayout[] layouts = new GroupLayout[] {
				interpolationTypeLayout, quantizerMethodLayout, characterMapperModeLayout};
		final JPanel[] panels = new JPanel[] {
				interpolationTypePanel, quantizerMethodPanel, characterMapperModePanel};
		for(int i = 0; i < layouts.length; i++) {
			final Dimension currentDimension = layouts[i].preferredLayoutSize(panels[i]);
			if(currentDimension.getWidth() > maxWidth) maxWidth = currentDimension.getWidth();
			if(currentDimension.getHeight() > maxHeight) maxHeight = currentDimension.getHeight();
		}
		final Dimension maxDimension = new Dimension((int) Math.round(maxWidth), (int) Math.round(maxHeight));
		for(int i = 0; i < panels.length; i++) {
			panels[i].setPreferredSize(maxDimension);
			panels[i].setMinimumSize(maxDimension);
		}
		
		final int result = JOptionPane.showConfirmDialog(null, mainPanel, "Image conversion methods",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if(result == JOptionPane.OK_OPTION) {
			final ImageConversionMethods methods = new ImageConversionMethods();
			if(interpolationTypeBicubic.isSelected())
				methods.setInterpolationType(InterpolationType.BICUBIC);
			else if(interpolationTypeBilinear.isSelected())
				methods.setInterpolationType(InterpolationType.BILINEAR);
			else if(interpolationTypeNearestNeighbor.isSelected())
				methods.setInterpolationType(InterpolationType.NEAREST_NEIGHBOR);
			if(quantizerMethodCeil.isSelected())
				methods.setQuantizerMethod(Quantizer.Method.CEIL);
			else if(quantizerMethodFloor.isSelected())
				methods.setQuantizerMethod(Quantizer.Method.FLOOR);
			else if(quantizerMethodRound.isSelected())
				methods.setQuantizerMethod(Quantizer.Method.ROUND);
			if(characterMapperModeDepth10.isSelected())
				methods.setCharacterMode(CharacterMapper.Mode.DEPTH_10);
			else if(characterMapperModeDepth70.isSelected())
				methods.setCharacterMode(CharacterMapper.Mode.DEPTH_70);
			return Optional.of(methods);
		}
		return Optional.empty();
	}
}
