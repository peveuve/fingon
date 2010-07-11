package test.contrib;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;

public class CheckBoxTest {

	private static JPanel getTestPanel() {
		JPanel panel = new JPanel(new FlowLayout());

		panel.add(new JCheckBox("Hello"));

		panel.add(new JCheckBox("Hello") {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				return new Dimension((int) d.getWidth(),
						(int) d.getHeight() - 4);
			}
		});

		panel.add(new JButton("Hello"));

		panel.add(new JTextField("Hello"));

		return panel;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SubstanceLookAndFeel
						.setSkin("org.pushingpixels.substance.api.skin.OfficeBlue2007Skin");

				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.add(getTestPanel());
				frame.pack();
				frame.setSize(400, 200);
				Dimension paneSize = frame.getSize();
				Dimension screenSize = frame.getToolkit().getScreenSize();
				frame.setLocation((screenSize.width - paneSize.width) / 2,
						(screenSize.height - paneSize.height) / 2);
				frame.setTitle("Test");
				frame.setVisible(true);
			}
		});
	}
}
