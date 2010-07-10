package test.contrib;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel;

/**
 * Color on RootPane is not installed by Basic themes, while it is for Synth.
 */
public class XSubstanceJRootPaneBorder {

	public static void main(String[] args) throws InterruptedException {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// setLaF(new MetalLookAndFeel());
				setLaF(SubstanceRavenLookAndFeel.class.getName());
				startGUI();
			}
		});

		Thread.sleep(3500);

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				setLaF("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
				updateWindows();
			}
		});

		Thread.sleep(3500);

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// setLaF(new MetalLookAndFeel());
				setLaF(SubstanceRavenLookAndFeel.class.getName());
				updateWindows();
			}
		});
	}

	private static void updateWindows() {
		for (Window window : Window.getWindows()) {
			try {
				SwingUtilities.updateComponentTreeUI(window);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static void startGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JButton("button"), BorderLayout.NORTH);
		panel.add(new JTextArea("test"), BorderLayout.CENTER);
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEADING));
		bottom.add(new JCheckBox("sample"));
		panel.add(bottom, BorderLayout.SOUTH);

		JFrame frame = new JFrame("Substance Border");
		frame.add(panel);

		JRootPane rootPane = frame.getRootPane();
		rootPane.setBorder(new Bordering());

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

	private static void setLaF(String laf) {
		try {
			UIManager.setLookAndFeel(laf);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}