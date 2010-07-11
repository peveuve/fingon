package test.contrib;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;

/**
 * Substance still spawning TabPreviewWindow on hover over inactive Tab...
 */
public class XSubstanceTabs {

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
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane
				.addTab("Tab 1", null, new JButton("button"), "Tab 1 tralala");
		tabbedPane
				.addTab("Tab 2", null, new JButton("button"), "Tab 2 tralala");

		JFrame frame = new JFrame("TestFrame");
		frame.add(tabbedPane);

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(150, 150);
		frame.setVisible(true);
	}

	private static void setLaF() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}
	}
}
