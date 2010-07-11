package test.contrib;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel;

public class XSubstanceKeybindings {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					launchGUI();
				} catch (UnsupportedLookAndFeelException e) {
					throw new AssertionError(e);
				}
			}
		});
	}

	private static void launchGUI() throws UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(new SubstanceRavenLookAndFeel());

		JPanel panel = new JPanel();

		final JButton buttonPress;
		final int[] counterPress = { 1 };

		final JButton buttonRelease;
		final int[] counterRelease = { 1 };

		buttonPress = new JButton("Hit Ctrl!");
		panel.add(buttonPress);
		buttonRelease = new JButton("Hit Ctrl!");
		panel.add(buttonRelease);

		InputMap inputMap = panel
				.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = panel.getActionMap();

		Object ctrl_press = "Control press";
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL,
				InputEvent.CTRL_DOWN_MASK), ctrl_press);
		actionMap.put(ctrl_press, new AbstractAction() {
			@Override
			public void actionPerformed(
					@SuppressWarnings("unused") ActionEvent e) {
				buttonPress.setText("Press# " + counterPress[0]++);
			}
		});
		Object ctrl_release = "Control release";
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, 0, true),
				ctrl_release);
		actionMap.put(ctrl_release, new AbstractAction() {
			@Override
			public void actionPerformed(
					@SuppressWarnings("unused") ActionEvent e) {
				buttonRelease.setText("Release# " + counterRelease[0]++);
			}
		});

		JTabbedPane tabbed = new JTabbedPane();
		tabbed.addTab("tab1", null, panel, "Annoying");

		JFrame frame = new JFrame("Keybindings Ctrl");
		frame.add(tabbed);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(640, 480);
		frame.setVisible(true);
	}
}