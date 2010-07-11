/*
 * Main.java
 * 
 * Created on Feb 15, 2010 at 8:16:05 PM.
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.contrib;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.fonts.SubstanceFontUtilities;

/**
 * <p>
 * A <code>Main</code> is a
 * </p>
 * 
 * @author Kharma
 */
public class BigButtons extends JFrame {

	public BigButtons() {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JToolBar toolbar = new JToolBar();
		JToggleButton jtb = new JToggleButton("one");
		jtb.getModel().setSelected(true);
		toolbar.add(jtb);

		getContentPane().add(toolbar, BorderLayout.NORTH);

		pack();
		setLocationRelativeTo(null);
	}

	/**
	 * The main application entry point.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				try {
					UIManager
							.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceAutumnLookAndFeel");
					SubstanceLookAndFeel.setFontPolicy(SubstanceFontUtilities
							.getScaledFontPolicy(10));
					new BigButtons().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});
	}

}
