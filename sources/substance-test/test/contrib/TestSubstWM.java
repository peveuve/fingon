package test.contrib;

import java.awt.Dimension;

import javax.swing.*;

import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.BusinessBlueSteelSkin;

public class TestSubstWM extends JFrame {

	public TestSubstWM() {
		super("TestSubstWM");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String... args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				UIManager.put(SubstanceLookAndFeel.SHOW_EXTRA_WIDGETS,
						Boolean.TRUE);

				try {
					SubstanceLookAndFeel.setSkin(new BusinessBlueSteelSkin());
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				JFrame.setDefaultLookAndFeelDecorated(true);
				TestSubstWM frame = new TestSubstWM();
				frame.setPreferredSize(new Dimension(200, 200));
				frame.setSize(frame.getPreferredSize());
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);

				SubstanceLookAndFeel
						.setWidgetVisible(
								frame.getRootPane(),
								true,
								SubstanceConstants.SubstanceWidgetType.TITLE_PANE_HEAP_STATUS);
				SubstanceLookAndFeel.setWidgetVisible(frame.getRootPane(),
						true,
						SubstanceConstants.SubstanceWidgetType.MENU_SEARCH);

				frame.getRootPane().putClientProperty(
						SubstanceLookAndFeel.WINDOW_MODIFIED, Boolean.FALSE);
			}
		});
	}
}