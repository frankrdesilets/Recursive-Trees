import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.*;

/**
 * This is the JFrame in which the application lives. It's purpose is to
 * initialize and hold onto all of the major parts.
 * 
 * @author frankdesilets
 *
 */
public class MainFrame extends JFrame {

	private Tree tree;
	private LeftPanel leftPanel;
	private TreeCanvas treeCanvas;
	private BottomPanel bottomPanel;
	private JPanel wrapperPanel;

	public MainFrame(String title) {

		this.setTitle(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);

		this.tree = new Tree();

		/*
		 * A wrapper panel is used to add padding to the borders. Other major panels are
		 * added to this wrapper panel, which has a border layout, and the wrapper panel
		 * is then added to the main content pane of the frame.
		 */
		wrapperPanel = new JPanel(new BorderLayout());

		this.getContentPane().setLayout(new BorderLayout());
		this.treeCanvas = new TreeCanvas(this.tree);
		this.wrapperPanel.add(treeCanvas, BorderLayout.CENTER);

		this.leftPanel = new LeftPanel(this.tree, this.treeCanvas);
		this.wrapperPanel.add(leftPanel, BorderLayout.WEST);

		this.bottomPanel = new BottomPanel(this.treeCanvas, this, leftPanel);
		this.wrapperPanel.add(bottomPanel, BorderLayout.SOUTH);

		this.wrapperPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 20, 20));
		this.getContentPane().add(wrapperPanel);
		this.pack();

	}

}
