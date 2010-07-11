package test.contrib;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel;

public class XSubstanceBorder {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				startGUI();
			}
		});
	}

	private static void startGUI() {
		setLaF();
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JButton("button"), BorderLayout.NORTH);
		panel.add(new JTextArea("test"), BorderLayout.CENTER);
		panel.setBorder(new Bordering());

		JFrame frame = new JFrame("Substance Border");
		frame.add(panel);

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(240, 240);
		frame.setVisible(true);
	}

	private static class Bordering extends SoftBevelBorder {
		public Bordering() {
			super(BevelBorder.RAISED);
		}

		public static final int INSETS_SIZE = 10;
		private static Insets INSETS = new Insets(INSETS_SIZE, INSETS_SIZE,
				INSETS_SIZE, INSETS_SIZE);

		@Override
		public Insets getBorderInsets(@SuppressWarnings("unused") Component c) {
			return INSETS;
		}

		@Override
		public Insets getBorderInsets(@SuppressWarnings("unused") Component c,
				Insets insets) {
			insets.left = insets.top = insets.right = insets.bottom = INSETS_SIZE;
			return insets;
		}
	}

	private static void setLaF() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		try {
			// UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
			UIManager.setLookAndFeel(new SubstanceRavenLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}
	}
}