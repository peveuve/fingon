package test;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.GeminiSkin;

public class ComboPrefSize extends JFrame {

	public ComboPrefSize() {
		this.setLayout(new FlowLayout());

		final JComboBox jcb = new JComboBox(new Object[] { "aaa",
				"bbbnnnnnnnnnnnnnnnn", "ccc" });
		System.out.println(jcb.getPreferredSize());

		this.add(jcb);

		final JCheckBox check = new JCheckBox("editable");
		check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						jcb.setEditable(check.isSelected());
						System.out.println(jcb.getPreferredSize());
					}
				});
			}
		});
		this.add(check);

		this.setSize(400, 100);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SubstanceLookAndFeel.setSkin(new GeminiSkin());
				// try {
				// UIManager.setLookAndFeel(new MetalLookAndFeel());
				// } catch (Exception e) {
				// }
				new ComboPrefSize().setVisible(true);
			}
		});
	}

}
