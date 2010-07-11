package test.contrib;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.BusinessBlueSteelSkin;

public class TestSubstanceLock extends JFrame {

	public TestSubstanceLock() {
		super("Changer");

		final JTextField textField = new JTextField("text field 1");
		textField.setAlignmentX(0.0f);

		final JSpinner spinner = new JSpinner();
		spinner.setValue(10);
		spinner.setAlignmentX(0.0f);

		final JTextField textField2 = new JTextField("text field 2");
		textField2.setAlignmentX(0.0f);

		final JComboBox comboBox = new JComboBox(new Object[] { "comboBox",
				"Test 1", "Test2" }) {
			@Override
			public void updateUI() {
				final ComboBoxEditor editor = getEditor();
				final boolean editable = editor == null
						|| ((JTextComponent) editor.getEditorComponent())
								.isEditable();
				super.updateUI();
				((JTextComponent) getEditor().getEditorComponent())
						.setEditable(editable);
			}
		};
		comboBox.setEditable(true);
		comboBox.setSelectedIndex(0);
		comboBox.setAlignmentX(0.0f);

		final JTextArea textArea = new JTextArea("text area");
		textArea.setAlignmentX(0.0f);
		textArea.setBorder(UIManager.getBorder("TextField.border"));
		textArea.setRows(5);

		JPanel center = new JPanel();
		final BoxLayout layout = new BoxLayout(center, BoxLayout.Y_AXIS);
		center.setLayout(layout);
		center.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		center.add(textField);
		center.add(spinner);
		center.add(textField2);
		center.add(comboBox);
		center.add(textArea);

		this.add(center, BorderLayout.CENTER);

		final JButton toggleEditable = new JButton("Not editable");
		toggleEditable.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final boolean editable = textField.isEditable();
				toggleEditable.setText(editable ? "Editable" : "Not editable");

				setEditable(textField, !editable);
				setEditable(textField2, !editable);
				setEditable(textArea, !editable);

				if (spinner.isEnabled()) {
					setEditable(((JSpinner.DefaultEditor) spinner.getEditor())
							.getTextField(), !editable);
					setEditable((JTextComponent) comboBox.getEditor()
							.getEditorComponent(), !editable);
				}
			}

		});

		final JButton toggleEnabled = new JButton("Disable");
		toggleEnabled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleEnabled.setText(spinner.isEnabled() ? "Enable"
						: "Disable");

				boolean editable = spinner.isEnabled()
						|| textField.isEditable();
				setEditable(((JSpinner.DefaultEditor) spinner.getEditor())
						.getTextField(), editable);
				setEditable((JTextComponent) comboBox.getEditor()
						.getEditorComponent(), editable);

				spinner.setEnabled(!spinner.isEnabled());
				comboBox.setEnabled(!comboBox.isEnabled());
			}

			private void toggleEditable(final JTextComponent editor) {
				editor.setEditable(!editor.isEditable());
			}
		});

		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttons.add(toggleEnabled);
		buttons.add(toggleEditable);
		this.add(buttons, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void setEditable(final JTextComponent editor, final boolean editable) {
		editor.setEditable(editable);
	}

	public static void main(String... args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				UIManager.put(SubstanceLookAndFeel.SHOW_EXTRA_WIDGETS,
						Boolean.TRUE);
				UIManager.put(LafWidget.HAS_LOCK_ICON, true);

				try {
					SubstanceLookAndFeel.setSkin(new BusinessBlueSteelSkin());
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				JFrame.setDefaultLookAndFeelDecorated(true);
				TestSubstanceLock ch = new TestSubstanceLock();
				ch.setPreferredSize(new Dimension(400, 250));
				ch.setSize(ch.getPreferredSize());
				ch.setLocationRelativeTo(null);
				ch.setVisible(true);

			}
		});
	}
}