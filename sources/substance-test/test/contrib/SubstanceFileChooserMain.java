package test.contrib;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.RavenSkin;

public class SubstanceFileChooserMain {
	public static void main(String[] args) throws Exception {

		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				SubstanceLookAndFeel.setSkin(new RavenSkin());
				JFileChooser chooser = new JFileChooser();
				JDialog dialog = new JDialog((JFrame) null, true);
				dialog.setContentPane(chooser);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.pack();
				dialog.setVisible(true);
			}
		});
	}
}