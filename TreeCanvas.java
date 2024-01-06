import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.*;

/**
 * 
 * The purpose of this class is to provide a panel on which to draw the Tree on
 * the GUI.
 * 
 * @author frankdesilets
 *
 */
public class TreeCanvas extends JPanel {

	private Tree tree;

	public TreeCanvas(Tree tree) {
		this.tree = tree;
		this.setPreferredSize(new Dimension(600, 600));

	}

	/**
	 * A border rectangle and the Tree is drawn.
	 */
	@Override
	public void paintComponent(Graphics g) {

		g.drawRect(75, 30, 500, 500);
		tree.drawOn(g, 330, 490);

	}

	public Tree getTree() {
		return tree;
	}

}
