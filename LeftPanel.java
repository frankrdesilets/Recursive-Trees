import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 
 * The purpose of this class is to provide a panel for the options menu of the
 * GUI. Many nested panels are used.
 * 
 * @author frankdesilets
 *
 */
public class LeftPanel extends JPanel implements ActionListener, ChangeListener, DocumentListener {

	private JTextField textField; // text field used by anglePanel
	private JCheckBox checkBox;
	private JSlider branchLengthNoiseSlider;
	private JSlider branchSplitNoiseSlider;
	private JRadioButton singleLeafButton;
	private JRadioButton doubleLeafButton;
	private JRadioButton tripleLeafButton;
	private ButtonGroup buttonGroup;
	private JLabel branchLengthNoiseLabel1;
	private JLabel branchLengthNoiseLabel2;
	private JLabel branchSplitNoiseLabel1;
	private JLabel branchSplitNoiseLabel2;
	private JComboBox comboBoxColor;
	private JSpinner comboBoxSegments;
	private Tree tree;

	public LeftPanel(Tree tree, TreeCanvas treeCanvas) {

		this.tree = tree;

		/*
		 * This class, LeftPanel, is set to have a BorderLayout. Two new JPanels are
		 * created and are added to ourself. northPanel serves as padding for the top of
		 * the frame. optionsPanel will be used to host the four different options, and
		 * as such it has a GridLayout with 4 rows and 1 column.
		 */
		this.setLayout(new BorderLayout());
		JPanel northPanel = new JPanel(new FlowLayout());
		this.add(northPanel, BorderLayout.NORTH); // serves as northern padding
		JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
		this.add(optionsPanel, BorderLayout.CENTER);

		/*
		 * A new JPanel is created to host the GUI elements of setting the segments and
		 * angle, configured with a BorderLayout.
		 * 
		 * It is given a title border and two new JPanels are created, each with a
		 * FlowLayout, to host the segments and angle options, respectively.
		 */
		JPanel segmentsAnglePanel = new JPanel(new GridLayout(1, 2));
		segmentsAnglePanel.setBorder(BorderFactory.createTitledBorder("Segments and Angle"));
		JPanel segmentsPanel = new JPanel();
		JPanel anglePanel = new JPanel();

		/*
		 * A title JLabel is created to preface the editable object in the segments
		 * panel.
		 */
		JLabel segments = new JLabel("Segments:");
		segmentsPanel.add(segments);
		/*
		 * A new JSpinner is created and given an initial value of 6. The size is set
		 * via a handle on it's Editor.
		 */
		this.comboBoxSegments = new JSpinner();
		Integer initialValue = 6;
		comboBoxSegments.setValue(initialValue);
		JComponent editor = comboBoxSegments.getEditor();
		Dimension dimension = editor.getPreferredSize();
		editor.setPreferredSize(new Dimension(40, (int) dimension.getHeight()));
		/*
		 * A ChangeListener is added to the JSpinner. A helper class is then called to
		 * change maxSegments in the Tree. The JSpinner is added to the segments panel.
		 */
		comboBoxSegments.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				LeftPanel.this.segmentSpinnerChanged((int) (comboBoxSegments.getValue()));
			}
		});
		segmentsPanel.add(comboBoxSegments);

		/*
		 * A title JLabel is created to preface the editable object in the angle panel.
		 * A JTextField is created for the user to input values to, and a default value
		 * is set. The two panels related to the single options are added to their
		 * specific panel, and that panel is added to the options panel, effectively
		 * filling the first row of four in the GridLayout of the options panel.
		 */
		JLabel angle = new JLabel("Angle:");
		this.textField = new JTextField(4);
		textField.setText("-90");
		anglePanel.add(angle);
		anglePanel.add(textField);
		segmentsAnglePanel.add(segmentsPanel);
		segmentsAnglePanel.add(anglePanel);
		optionsPanel.add(segmentsAnglePanel);

		/*
		 * A new JPanel, the noise panel, is created to host the GUI elements relating
		 * to noise settings, configured with a BorderLayout. 3 separate panels will be
		 * added to fill in the grid.
		 * 
		 * The noise panel is given a title border and is added to the options panel.
		 */
		JPanel noisePanel = new JPanel(new GridLayout(3, 1));
		noisePanel.setBorder(BorderFactory.createTitledBorder("Noise"));
		optionsPanel.add(noisePanel);
		/*
		 * The first of three JPanels is created. This panel is used to hold a separator
		 * in the form of a JLabel. The label is added to the panel, and the panel is
		 * added to the noise panel, filling the first grid space.
		 * 
		 */
		JPanel linePanel = new JPanel();
		JLabel line = new JLabel("______________________________________________________________________");
		linePanel.add(line);
		noisePanel.add(linePanel);
		/*
		 * The second JPanel to be put into the noise panel is created. A title JLabel
		 * is created to preface the editable object. A JSlider is created to give the
		 * user the ability to set the value of the max noise length for calculation in
		 * the Tree. The two components are added to the panel.
		 */
		JPanel branchLengthNoisePanel = new JPanel();
		branchLengthNoiseLabel2 = new JLabel("Branch Length Noise");
		this.branchLengthNoiseSlider = new JSlider(0, 40);
		branchLengthNoisePanel.add(branchLengthNoiseLabel2);
		branchLengthNoisePanel.add(branchLengthNoiseSlider);
		/*
		 * A second JLabel is created to follow the JSlider. It's text will be updated
		 * whenever the JSlider detects a change in value, indicating that the user is
		 * manipulating it. We add the label to the panel and add an anonymous
		 * ChangeListener, that calls a helper method to update the value in the Tree,
		 * and to update the numerical value displayed in the trailing label.
		 */
		branchLengthNoiseLabel1 = new JLabel("Current Noise: " + "0");
		branchLengthNoisePanel.add(branchLengthNoiseLabel1);
		branchLengthNoiseSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				LeftPanel.this.branchLengthNoiseSliderChanged(branchLengthNoiseSlider.getValue());
			}
		});
		/*
		 * This panel is then added to the noise panel, filling the second grid slot.
		 */
		noisePanel.add(branchLengthNoisePanel);

		/*
		 * The third panel to be added to the noise panel is created. A JSlider is
		 * created to enable the user to set the branch split noise in the Tree. Two
		 * JLabels are created, one to preface the slider (the title), and one to
		 * follow. The three components are added to the panel.
		 */
		JPanel branchSplitNoisePanel = new JPanel();
		this.branchSplitNoiseSlider = new JSlider(0, 40);
		branchSplitNoiseLabel1 = new JLabel("Branch Split Noise");
		branchSplitNoiseLabel2 = new JLabel("Current Noise: " + "20");
		branchSplitNoisePanel.add(branchSplitNoiseLabel1);
		branchSplitNoisePanel.add(branchSplitNoiseSlider);
		branchSplitNoisePanel.add(branchSplitNoiseLabel2);
		/*
		 * An anonymous ChangeListener is added to the slider, calling a helper method
		 * that updates the value in the Tree and the numerical value in the trailing
		 * label.
		 */
		branchSplitNoiseSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				LeftPanel.this.branchSplitNoiseSliderChanged(branchSplitNoiseSlider.getValue());
			}
		});
		/*
		 * The panel is added to the noise panel, filling up 3/3 grid slots and
		 * completing noisePanel.
		 */
		noisePanel.add(branchSplitNoisePanel);

		/*
		 * A new JPanel, the leaf panel, is created to host the GUI elements relating to
		 * leaf settings, configured with a BorderLayout. 3 separate panels will be
		 * added to fill in the grid.
		 * 
		 * The leaf panel is given a title border and is added to the options panel.
		 */
		JPanel leafPanel = new JPanel(new GridLayout(3, 1));
		leafPanel.setBorder(BorderFactory.createTitledBorder("Leaves"));
		optionsPanel.add(leafPanel);
		/*
		 * A new ButtonGroup is created along with 3 new JRadioButtons. The radio
		 * buttons are added to the group, and the first button is set to be selected as
		 * it represents the default value. The radio buttons are added to the leaf
		 * panel.
		 */
		this.buttonGroup = new ButtonGroup();
		this.singleLeafButton = new JRadioButton("Single Leaf");
		this.doubleLeafButton = new JRadioButton("Double Leaf");
		this.tripleLeafButton = new JRadioButton("Triple Leaf");
		buttonGroup.add(singleLeafButton);
		buttonGroup.add(doubleLeafButton);
		buttonGroup.add(tripleLeafButton);
		singleLeafButton.setSelected(true);
		leafPanel.add(singleLeafButton);
		leafPanel.add(doubleLeafButton);
		leafPanel.add(tripleLeafButton);
		/*
		 * An anonymous ChangeListener is added to each radio button. If the button
		 * detects a change, and the button is currently selected, a helper method is
		 * called to update the value in the Tree.
		 */
		singleLeafButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				boolean isSelected = singleLeafButton.isSelected();

				if (isSelected) {

					LeftPanel.this.leafButton(1);

				} else {
				}

			}
		});
		doubleLeafButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				boolean isSelected = doubleLeafButton.isSelected();

				if (isSelected) {

					LeftPanel.this.leafButton(2);

				} else {
				}

			}
		});
		tripleLeafButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				boolean isSelected = tripleLeafButton.isSelected();

				if (isSelected) {

					LeftPanel.this.leafButton(3);

				} else {
				}

			}
		});

		/*
		 * A new JPanel, the fruit panel, is created to host the GUI elements relating
		 * to fruit settings, configured with a BorderLayout. 2 panels will be added to
		 * fill in the grid.
		 * 
		 * The fruit panel is given a title border and is added to the options panel,
		 * completing it.
		 */
		JPanel fruitPanel = new JPanel(new GridLayout(2, 1));
		fruitPanel.setBorder(BorderFactory.createTitledBorder("Fruit"));
		optionsPanel.add(fruitPanel);
		/*
		 * A new JCheckBox is created to enable the user to select whether they would
		 * like fruit to be drawn on the Tree. The checkbox is added to the fruit panel,
		 * and an anonymous ChangeListener is added. When a change is detected, a helper
		 * method is called to update the setting in the Tree depending on whether the
		 * box is selected.
		 */
		this.checkBox = new JCheckBox("Show Fruit");
		fruitPanel.add(checkBox);
		checkBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				LeftPanel.this.fruitCheckBox(LeftPanel.this.checkBox.isSelected());
			}
		});
		/*
		 * A new JPanel is created to host the settings related to the color selection
		 * of fruit. It has 1 row and 2 columns. A new title JLabel is created to
		 * preface the editable component.
		 */
		JPanel colorPanel = new JPanel(new GridLayout(1, 2));
		JLabel colorLabel = new JLabel("Color Selection: ");
		/*
		 * A new JComboBox is created to enable the user to select different colors for
		 * the fruit. Strings "Red" and "Yellow" are added to the combobox. An anonymous
		 * ActionListener is added to the combobox, which evaluates which item is
		 * selected and updates the Tree setting accordingly.
		 */
		this.comboBoxColor = new JComboBox();
		comboBoxColor.addItem("Red");
		comboBoxColor.addItem("Yellow");
		comboBoxColor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JComboBox comboBox = (JComboBox) e.getSource(); // local handle on the combobox
				String colorString = (String) comboBox.getSelectedItem(); // converted to String

				/*
				 * Sets the appropriate setting in the Tree depending on what String the item
				 * selected matches.
				 */
				if (colorString.equals("Red")) {
					LeftPanel.this.tree.setFruitColor(LeftPanel.this.tree.redFruitColor());
				} else if (colorString.equals("Yellow")) {
					LeftPanel.this.tree.setFruitColor(LeftPanel.this.tree.yellowFruitColor());
				}

			}

		});
		/*
		 * The components are added to the color panel, and the panel is added to the
		 * fruit panel.
		 */
		colorPanel.add(colorLabel);
		colorPanel.add(comboBoxColor);
		fruitPanel.add(colorPanel);

	}

	/**
	 * This is a private helper method that the anonymous ChangeListener added to
	 * the branch length noise JSlider calls. It sets the text in the corresponding
	 * trailing JLabel and updates the setting in the Tree.
	 * 
	 * @param value
	 */
	private void branchLengthNoiseSliderChanged(int value) {
		this.branchLengthNoiseLabel1.setText("Current Noise: " + value);
		tree.setUpperBranchLengthForCalculation(value);

	}

	/**
	 * This is a private helper method that the anonymous ChangeListener added to
	 * the branch split noise JSlider calls. It sets the text in the corresponding
	 * trailing JLabel and updates the setting in the Tree.
	 * 
	 * @param value
	 */
	private void branchSplitNoiseSliderChanged(int value) {
		this.branchSplitNoiseLabel2.setText("Current Noise: " + value);
		tree.setSplitAngle(value);

	}

	/**
	 * This is a private helper method that the anonymous ChangeListener added to
	 * the segment JComboBox calls. It updates the setting in the Tree.
	 * 
	 * @param value
	 */
	private void segmentSpinnerChanged(int value) {
		tree.setMaxSegments(value);

	}

	/**
	 * This is a private helper method that the anonymous ChangeListener added to
	 * the leaf part JRadioButton objects call. It updates the setting in the Tree
	 * depending on the value passed in.
	 * 
	 * @param value
	 */
	private void leafButton(int parts) {

		tree.setLeafParts(parts);

	}

	/**
	 * This is a private helper method that the anonymous ChangeListener added to
	 * the fruit selection JCheckBox calls. It updates the setting in the Tree
	 * depending on the value passed in.
	 * 
	 * @param value
	 */
	private void fruitCheckBox(boolean isSelected) {

		if (isSelected) {
			this.tree.setShowFruit(true);
		} else {
			this.tree.setShowFruit(false);
		}

	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public JTextField getTextField() {
		return textField;
	}

}
