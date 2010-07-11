package test.contrib;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class BaselineTest extends JFrame {

	public BaselineTest() {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Box box = Box.createVerticalBox();
		box.add(new JLabel("Testing"));
		box.add(Box.createVerticalGlue());

		getContentPane().add(box, BorderLayout.WEST);
		getContentPane().add(new JTextArea("Testing Text Area"),
				BorderLayout.EAST);

		pack();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				try {
					UIManager.setLookAndFeel(new MetalLookAndFeel());
					// UIManager
					// .setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceBusinessLookAndFeel");
					JFrame.setDefaultLookAndFeelDecorated(true);
				} catch (Exception e) {
				}

				BaselineTest test = new BaselineTest();
				test.setLocationRelativeTo(null);
				test.setVisible(true);
			}

		});
	}

}
