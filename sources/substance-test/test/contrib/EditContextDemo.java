package test.contrib;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;

public class EditContextDemo {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame.setDefaultLookAndFeelDecorated(true);
					UIManager
							.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
					UIManager.put(LafWidget.TEXT_EDIT_CONTEXT_MENU,
							Boolean.TRUE);

					final JFrame frame = new JFrame("SelectAllDemo");
					frame.setMinimumSize(new Dimension(640, 800));
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

					final JEditorPane editPane = new JEditorPane("text/rtf", "");
					frame.getContentPane().add(new JScrollPane(editPane),
							BorderLayout.CENTER);

					frame.setVisible(true);
				} catch (final Throwable thr) {
					thr.printStackTrace();
				}
			}
		});
	}
}