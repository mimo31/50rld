package com.github.mimo31.w50rld;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Handles low level user interaction. Usually prepares event listeners and then calls methods in Main when the event is triggered.
 * @author mimo31
 *
 */
public class Gui {

	// frame's component used to paint the game
	private static JComponent paintComponent;

	public static void initializeGui()
	{
		// create the window and override the paint with our paint paint method
		JFrame frame = new JFrame("50rld");

		// create a component to paint
		paintComponent = new JComponent() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 371375858304466268L;

			@Override
			public void paint(Graphics graphics)
			{
				Main.paint((Graphics2D) graphics, this.getWidth(), this.getHeight());
			}

		};

		// add the component to the frame
		frame.add(paintComponent);

		// configure the window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		// create a timer that calls our update method and repaints when
		// triggered
		Timer updateTimer = new Timer(16, new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Container contentPane = frame.getContentPane();
				Main.update(contentPane.getWidth(), contentPane.getHeight(), 16);
				frame.repaint();
			}

		});

		// add key listener
		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				Main.keyReleased(e);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				Main.keyPressed(e);
			}
		});

		// add mouse listener
		frame.getContentPane().addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				Main.mouseClicked(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
		});
		
		// set minimal window size
		frame.setMinimumSize(new Dimension(400, 400));

		// start the update timer
		updateTimer.start();

		// make the window visible
		frame.setVisible(true);
	}
	
	/**
	 * Returns the size of the frame component that encloses all painting.
	 * @return size of the frame's contents
	 */
	public static Dimension getContentSize()
	{
		return paintComponent.getSize();
	}

}
