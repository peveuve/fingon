package test.contrib;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;

public class SelectAllDemo {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame.setDefaultLookAndFeelDecorated(true);
					JDialog.setDefaultLookAndFeelDecorated(true);
					UIManager
							.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());

					final JFrame frame = new JFrame("SelectAllDemo");
					frame.setMinimumSize(new Dimension(640, 800));
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.getContentPane().setLayout(new BorderLayout());

					// button
					final JButton button = new JButton(
							"Edit (only availabe on single selection)");
					frame.getContentPane().add(button, BorderLayout.SOUTH);

					// list
					final DefaultListModel model = new DefaultListModel();
					for (int i = 0; i < 1000; i++)
						model.addElement("element " + i);

					final JList list = new JList();
					list.setModel(model);
					frame.getContentPane().add(new JScrollPane(list),
							BorderLayout.CENTER);

					list.addListSelectionListener(new ListSelectionListener() {
						public void valueChanged(final ListSelectionEvent lse) {
							button
									.setEnabled(list.getSelectedIndices().length == 1);
						}
					});

					frame.setVisible(true);
				} catch (final Throwable thr) {
					thr.printStackTrace();
				}
			}
		});
	}
}