import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * 
 * This is the bottom panel in the frame. It holds two buttons.
 * 
 * @author frankdesilets
 *
 */
public class BottomPanel extends JPanel implements ActionListener {

	private JButton repaintButton;
	private JButton closeButton;
	private TreeCanvas treeCanvas;
	private JFrame mainFrame;
	private LeftPanel leftPanel;

	/*
	 * These member variables are for a custom feature, Grow Tree.
	 */
	private JButton growTree;
	private JLabel dayCounter;
	private int daysPassed;
	private Tree tree;
	private int age = 1;
	private Timer timer;
	private double splitAngle;
	private int maxSegments;
	private double maxBranchNoise;
	private double maxAngleNoise;
	private int leafParts;
	private boolean showFruit;
	private Color fruitColor;
	private Color leafColor;
	private int upperBranchLengthForCalculation;
	private int theta;

	public BottomPanel(TreeCanvas treeCanvas, JFrame mainFrame, LeftPanel leftPanel) {

		this.treeCanvas = treeCanvas;
		this.mainFrame = mainFrame;
		this.leftPanel = leftPanel;

		this.setLayout(new FlowLayout(FlowLayout.RIGHT)); // The components are anchored to the right

		/*
		 * The following are for a custom feature, Grow Tree.
		 * 
		 * The Tree member variable is initialized as a handle on the Tree. When the
		 * action is performed, the current settings of the Tree are saved to member
		 * variables. When the tree is done growing, the settings are restored.
		 */
		this.tree = treeCanvas.getTree();
		this.growTree = new JButton("Grow Tree");
		growTree.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				splitAngle = tree.getSplitAngle();
				maxSegments = tree.getMaxSegments();
				maxBranchNoise = tree.getMaxBranchNoise();
				maxAngleNoise = tree.getMaxAngleNoise();
				leafParts = tree.getLeafParts();
				showFruit = tree.isShowFruit();
				fruitColor = tree.getFruitColor();
				leafColor = tree.getLeafColor();
				upperBranchLengthForCalculation = tree.getUpperBranchLengthForCalculation();
				theta = tree.getTheta();

				BottomPanel.this.growATree();

			}

		});

		/*
		 * The repaint button is created and an anonymous ActionListener is added.
		 */
		repaintButton = new JButton("Refresh");
		repaintButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JTextField textField = BottomPanel.this.leftPanel.getTextField(); // creates a local handle on the
																					// textField in the GUI

				String input = textField.getText(); // gets the value from the textField as a String
				int value = Integer.parseInt(input); // converts the value to an Integer
				Tree tree = BottomPanel.this.treeCanvas.getTree(); // creates a local handle on the Tree
				tree.setTheta(value); // sets the theta value in Tree

				BottomPanel.this.treeCanvas.repaint(); // repaints the treeCanvas
			}

		});

		/*
		 * The exit button is created and add an anonymous ActionListener is added.
		 */
		closeButton = new JButton("Exit");
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				BottomPanel.this.mainFrame.dispose(); // terminates the frame

			}

		});

		/*
		 * The buttons are added to BottomPanel.
		 */
		this.add(growTree);
		this.add(repaintButton);
		this.add(closeButton);

	}

	/**
	 * This method is used by a custom feature, Grow Tree.
	 * 
	 * @param age
	 */
	private void grow(int age) {

		/*
		 * This if statement executes when age exceeds 8. The member variable timer
		 * calling this method stops, and tree settings are restored. A single action
		 * local timer repaints the canvas to reflect restored changes after 5 seconds.
		 */
		if (age > 8) {
			timer.stop();
			this.age = 1;
			this.growRevert();
			Timer t = new Timer(5000, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					BottomPanel.this.treeCanvas.repaint();

				}
			});
			t.setRepeats(false);
			t.start();
			return;
		}

		/*
		 * The following modifies the settings for the Tree based on the current age.
		 */
		tree.setMaxSegments(age);
		if (age == 4 || age == 5) {
			tree.setLeafParts(2);
		} else if (age == 6 || age == 7) {
			tree.setShowFruit(true);
			tree.setFruitColor(tree.yellowFruitColor());
			tree.setLeafParts(3);
		} else if (age == 8) {
			tree.setShowFruit(true);
			tree.setFruitColor(tree.redFruitColor());
			tree.setLeafParts(3);
		} else {
			tree.setShowFruit(false);
		}

		Graphics g = treeCanvas.getGraphics();
		tree.drawOn(g, 330, 490);

		BottomPanel.this.treeCanvas.repaint();

	}

	/**
	 * This method is used by a custom feature, Grow Tree.
	 */
	private void growATree() {

		/*
		 * This member variable timer fires every 2 seconds and grows a new tree via
		 * calling a helper method.
		 */
		timer = new Timer(2000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				BottomPanel.this.grow(BottomPanel.this.age);
				BottomPanel.this.age++;

			}
		});
		timer.start();

	}

	/**
	 * This method is used by a custom feature, Grow Tree. It is used to restore
	 * settings to tree after the user executes the Grow Tree feature.
	 */
	private void growRevert() {

		tree.setSplitAngle(splitAngle);
		tree.setMaxSegments(maxSegments);
		tree.setMaxBranchNoise(maxBranchNoise);
		tree.setMaxAngleNoise(maxAngleNoise);
		tree.setLeafParts(leafParts);
		tree.setShowFruit(showFruit);
		tree.setFruitColor(fruitColor);
		tree.setLeafColor(leafColor);
		tree.setUpperBranchLengthForCalculation(upperBranchLengthForCalculation);
		tree.setTheta(theta);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
