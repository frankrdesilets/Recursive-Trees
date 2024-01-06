import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

/**
 * This class holds all of the current settings for the tree, as well as a
 * method to draw itself based on the settings set from Panels that are part of
 * the GUI.
 * 
 * 
 * @author frankdesilets
 *
 */
public class Tree {

	private double splitAngle; // the angle in which two segments split
	private int maxSegments; // the amount of segments we will draw
	private double maxBranchNoise; // randomness to branch length
	private double maxAngleNoise; // randomness to angle length
	private int leafParts; // how many leaves drawn per segment
	private boolean showFruit;
	private Color fruitColor;
	private Color leafColor;
	private final int leavesAppear = 5; // leaves appear after the remaining segments to draw is less than 5
	private int upperBranchLengthForCalculation; // used to hold the maximum branch length the user sets
	private int theta;
	private Color branchColor;

	public Tree() {

		this.splitAngle = 20.0;
		this.maxBranchNoise = 0.0;
		this.maxAngleNoise = 0.0;
		this.maxSegments = 6;
		this.leafParts = 1;
		this.showFruit = false;
		this.leafColor = this.leafColor(); // randomly generate a starting leaf color
		this.fruitColor = this.redFruitColor(); // randomly generate a starting red fruit color
		this.theta = -90;
		this.branchColor = new Color(122, 86, 0);

	}

	/**
	 * 
	 * This method is called to draw a tree on the canvas.
	 * 
	 * @param g
	 * @param x
	 * @param y
	 */
	public void drawOn(Graphics g, int x, int y) { // x and y is where the tree is "planted".

		int stroke = maxSegments - 1; // to ensure even stroke throughout the tree, the initial stroke is set to the
										// amount of segments.
		this.drawTree(g, x, y, this.splitAngle, this.theta, this.maxSegments, stroke);

	}

	/**
	 * 
	 * This is a recursive method that draws a tree based on starting values, and
	 * modified values that it passes back into itself.
	 * 
	 * @param g
	 * @param sx
	 * @param sy
	 * @param splitAngle
	 * @param theta
	 * @param segsRemaining
	 * @param stroke
	 */
	private void drawTree(Graphics g, int sx, int sy, double splitAngle, double theta, int segsRemaining, int stroke) {

		/*
		 * The stroke is set.
		 */
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(branchColor);
		BasicStroke basicStroke = new BasicStroke(stroke);
		g2.setStroke(basicStroke);

		/*
		 * This method keeps calling as long as there are segments to draw.
		 */
		if (segsRemaining > 0) {

			/*
			 * Length is calculated via a helper method, and calculate the new x and new y
			 * to draw a line to.
			 */
			int length1 = this.branchLength(segsRemaining);
			int length2 = this.branchLength(segsRemaining);
			int nx = (int) (sx + length1 * Math.cos(Math.toRadians(theta)));
			int ny = (int) (sy + length2 * Math.sin(Math.toRadians(theta)));
			g.drawLine(sx, sy, nx, ny);

			/*
			 * Leaves are attempted to be drawn if the segments remaining to draw is less
			 * than 5.
			 */
			if (segsRemaining < leavesAppear) {

				this.drawLeaves(g2, nx, ny, theta);

			} else {
			}

			/*
			 * The new angles and split angles are calculated for the new segments.
			 */
			int newAngle1 = (int) (theta + splitAngle + maxAngleNoise);
			int newAngle2 = (int) (theta - splitAngle + maxAngleNoise);
			int upper = (int) (this.splitAngle);
			int lower = 20;
			int splitAngle1 = (int) ((Math.random() * (upper - lower)) + lower);
			int splitAngle2 = (int) ((Math.random() * (upper - lower)) + lower);

			/*
			 * The stroke is decreased by one unless its value is already one.
			 */
			if (stroke > 1) {
				stroke--;
			} else {
			}

			/*
			 * The amount of segments left to draw is set.
			 */
			segsRemaining--;

			/*
			 * Drawing of the new segments begins by passing in our newly calculated values.
			 */
			this.drawTree(g2, nx, ny, splitAngle1, newAngle1, segsRemaining, stroke);
			this.drawTree(g2, nx, ny, splitAngle2, newAngle2, segsRemaining, stroke);

		}

	}

	/**
	 * 
	 * This is a private helper method to draw leaves at the current x and y value
	 * of the 'trunk' line in the tree.
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param theta
	 */
	private void drawLeaves(Graphics2D g, int x, int y, double theta) {

		AffineTransform xf = g.getTransform(); // saves current transform

		/*
		 * Depending on how many leaves to be drawn (1, 2, or 3) are set from the GUI,
		 * the corresponding amount are drawn.
		 */
		switch (leafParts) {
		case 1:
			g.translate(x, y); // translates the origin to the current x and y passed in

			g.setColor(this.leafColor()); // sets the color of this leaf to be random
			g.rotate(theta); // rotates the leaf
			g.fillOval(0, 0, 5, 10); // draws the leaf

			g.setTransform(xf); // restores the origin
			g.setColor(branchColor); // restores the color
			break;
		case 2:
			g.translate(x, y);

			g.setColor(this.leafColor());
			g.fillOval(0, 0, 5, 10);
			g.rotate(theta);

			g.setColor(this.leafColor());
			g.fillOval(0, 0, 5, 10);

			g.setTransform(xf);
			g.setColor(branchColor);
			break;
		case 3:
			g.translate(x, y);

			g.setColor(this.leafColor());
			g.fillOval(0, 0, 5, 10);
			g.rotate(theta);

			g.setColor(this.leafColor());
			g.fillOval(0, 0, 5, 10);
			g.rotate(theta);

			g.setColor(this.leafColor());
			g.fillOval(0, 0, 5, 10);

			g.setTransform(xf);
			g.setColor(branchColor);
			break;
		default:
			System.out.println("Not a valid leafPart value.");
			break;
		}

		/*
		 * If the user has selected to show fruit via the GUI, it is drawn here.
		 */
		if (showFruit) {

			/*
			 * A random integer is calculated between 1 and 20.
			 */
			int upper = 20;
			int lower = 0;
			int drawFruit = (int) ((Math.random() * (upper - lower)) + lower);

			/*
			 * If the integer is greater than 15, a fruit is drawn at the current x, y. This
			 * randomizes and spreads out the drawing of fruit.
			 */
			if (drawFruit > 15) {

				g.translate(x, y);
				g.setColor(this.fruitColor);
				g.rotate(theta);
				g.fillOval(0, 0, 5, 5);
				g.setTransform(xf);
				g.setColor(branchColor);

			} else {
			}

		} else {
		}

	}

	/**
	 * 
	 * This is a private helper method, called from drawTree(), to calculate the
	 * length of the line. It takes into account noise set from the user.
	 * 
	 * @param segsRemaining
	 * @return
	 */
	private int branchLength(int segsRemaining) {

		int upper = upperBranchLengthForCalculation;
		int lower = 0;
		this.maxBranchNoise = (int) ((Math.random() * (upper - lower)) + lower);

		int length = (int) (10 * segsRemaining + this.maxBranchNoise);
		return length;

	}

	/**
	 * 
	 * This is a private helper method that returns a random green Color between a
	 * set of RGB values.
	 * 
	 * @return
	 */
	private Color leafColor() {

		/*
		 * Red is calculated.
		 */
		int upperRed = 63;
		int lowerRed = 44;
		int red = (int) ((Math.random() * (upperRed - lowerRed)) + lowerRed);

		/*
		 * Green is calculated.
		 */
		int upperGreen = 222;
		int lowerGreen = 151;
		int green = (int) ((Math.random() * (upperGreen - lowerGreen)) + lowerGreen);

		/*
		 * Blue is calculated.
		 */
		int upperBlue = 95;
		int lowerBlue = 66;
		int blue = (int) ((Math.random() * (upperBlue - lowerBlue)) + lowerBlue);

		/*
		 * Alpha is calculated.
		 */
		int upperAlpha = 255;
		int lowerAlpha = 220;
		int alpha = (int) ((Math.random() * (upperAlpha - lowerAlpha)) + lowerAlpha);

		/*
		 * Creates and returns the Color.
		 */
		Color leafColor = new Color(red, green, blue, alpha);
		return leafColor;

	}

	/**
	 * 
	 * This is a private helper method that returns a random red Color between a set
	 * of RGB values.
	 * 
	 * @return
	 */
	public Color redFruitColor() {

		/*
		 * Red is calculated.
		 */
		int upperRed = 226;
		int lowerRed = 201;
		int red = (int) ((Math.random() * (upperRed - lowerRed)) + lowerRed);

		/*
		 * Green is calculated.
		 */
		int upperGreen = 55;
		int lowerGreen = 78;
		int green = (int) ((Math.random() * (upperGreen - lowerGreen)) + lowerGreen);

		/*
		 * Blue is calculated.
		 */
		int upperBlue = 86;
		int lowerBlue = 98;
		int blue = (int) ((Math.random() * (upperBlue - lowerBlue)) + lowerBlue);

		Color leafColor = new Color(red, green, blue);
		return leafColor;

	}

	/**
	 * 
	 * This is a private helper method that returns a random red Color between a set
	 * of RGB values.
	 * 
	 * @return
	 */
	public Color yellowFruitColor() {

		/*
		 * Red is calculated.
		 */
		int upperRed = 221;
		int lowerRed = 179;
		int red = (int) ((Math.random() * (upperRed - lowerRed)) + lowerRed);

		/*
		 * Green is calculated.
		 */
		int upperGreen = 232;
		int lowerGreen = 191;
		int green = (int) ((Math.random() * (upperGreen - lowerGreen)) + lowerGreen);

		/*
		 * Blue is calculated.
		 */
		int upperBlue = 64;
		int lowerBlue = 22;
		int blue = (int) ((Math.random() * (upperBlue - lowerBlue)) + lowerBlue);

		Color leafColor = new Color(red, green, blue);
		return leafColor;

	}

	public double getSplitAngle() {
		return splitAngle;
	}

	public void setSplitAngle(double splitAngle) {
		this.splitAngle = splitAngle;
	}

	public int getMaxSegments() {
		return maxSegments;
	}

	public void setMaxSegments(int maxSegments) {
		this.maxSegments = maxSegments;
	}

	public double getMaxBranchNoise() {
		return maxBranchNoise;
	}

	public void setMaxBranchNoise(double maxBranchNoise) {
		this.maxBranchNoise = maxBranchNoise;
	}

	public double getMaxAngleNoise() {
		return maxAngleNoise;
	}

	public void setMaxAngleNoise(double maxAngleNoise) {
		this.maxAngleNoise = maxAngleNoise;
	}

	public int getLeafParts() {
		return leafParts;
	}

	public void setLeafParts(int leafParts) {
		this.leafParts = leafParts;
	}

	public boolean isShowFruit() {
		return showFruit;
	}

	public void setShowFruit(boolean showFruit) {
		this.showFruit = showFruit;
	}

	public Color getFruitColor() {
		return fruitColor;
	}

	public void setFruitColor(Color fruitColor) {
		this.fruitColor = fruitColor;
	}

	public Color getLeafColor() {
		return leafColor;
	}

	public void setLeafColor(Color leafColor) {
		this.leafColor = leafColor;
	}

	public int getUpperBranchLengthForCalculation() {
		return upperBranchLengthForCalculation;
	}

	public void setUpperBranchLengthForCalculation(int upperBranchLengthForCalculation) {
		this.upperBranchLengthForCalculation = upperBranchLengthForCalculation;
	}

	public void setTheta(int theta) {
		this.theta = theta;
	}

	public Color getBranchColor() {
		return branchColor;
	}

	public void setBranchColor(Color branchColor) {
		this.branchColor = branchColor;
	}

	public int getLeavesAppear() {
		return leavesAppear;
	}

	public int getTheta() {
		return theta;
	}

}
