package com.github.ennoxhd.aig;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Optional;

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
import javax.swing.JSlider;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;

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
	
	static Optional<Point2D.Double> chooseScalingFactorsDialog() {
		GuiUtils.initializeGui();
		
		AbstractFormatterFactory formatterFactory = new AbstractFormatterFactory() {
			@Override
			public AbstractFormatter getFormatter(JFormattedTextField tf) {
				DecimalFormat format = new DecimalFormat();
				format.setDecimalSeparatorAlwaysShown(false);
				format.setGroupingUsed(false);
				format.setMaximumFractionDigits(0);
				format.setMinimumFractionDigits(0);
				format.setMaximumIntegerDigits(3);
				format.setMinimumIntegerDigits(1);
				NumberFormatter formatter = new NumberFormatter(format);
				formatter.setMinimum(Integer.valueOf(5));
				formatter.setMaximum(Integer.valueOf(500));
				formatter.setAllowsInvalid(true);
				formatter.setCommitsOnValidEdit(true);
				return formatter;
			}
		};
		
		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		panel.setLayout(layout);
		
		JLabel widthLabel = new JLabel("Width:");
		JFormattedTextField widthField = new JFormattedTextField(formatterFactory);
		widthField.setValue(Integer.valueOf(100));
		widthField.setColumns(3);
		JLabel widthPercentSign = new JLabel("%");
		JSlider widthSlider = new JSlider(5, 500, 100);
		widthSlider.addChangeListener(e -> {
			widthField.setText(String.valueOf(widthSlider.getValue()));
		});
		widthField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				return;
			}
			@Override
			public void focusLost(FocusEvent e) {
				try {
					widthField.commitEdit();
					widthSlider.setValue(Integer.valueOf(widthField.getText()));
				} catch (ParseException | NumberFormatException ex) {
					widthField.setValue(widthSlider.getValue());
				}
			}
		});
		JButton widthReset = new JButton("Reset");
		widthReset.addActionListener(e -> {
			widthSlider.setValue(100);
		});
		
		JLabel heightLabel = new JLabel("Height:");
		JFormattedTextField heightField = new JFormattedTextField(formatterFactory);
		heightField.setValue(Integer.valueOf(100));
		heightField.setColumns(3);
		JLabel heightPercentSign = new JLabel("%");
		JSlider heightSlider = new JSlider(5, 500, 100);
		heightSlider.addChangeListener(e -> {
			heightField.setText(String.valueOf(heightSlider.getValue()));
		});
		heightField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				return;
			}
			@Override
			public void focusLost(FocusEvent e) {
				try {
					heightField.commitEdit();
					heightSlider.setValue(Integer.valueOf(heightField.getText()));
				} catch (ParseException | NumberFormatException ex) {
					heightField.setValue(heightSlider.getValue());
				}
			}
		});
		JButton heightReset = new JButton("Reset");
		heightReset.addActionListener(e -> {
			heightSlider.setValue(100);
		});
		JButton heightHalf = new JButton("Half height");
		heightHalf.addActionListener(e -> {
			heightSlider.setValue(Math.round(widthSlider.getValue() / 2.0f));
		});
		JButton heightProportional = new JButton("Proportional");
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
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Scaling factors",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if(result == JOptionPane.OK_OPTION) {
			Point2D.Double scale = new Point2D.Double(widthSlider.getValue() / 100.0,
					heightSlider.getValue() / 100.0);
			return Optional.of(scale);
		}
		return Optional.empty();
	}
}
