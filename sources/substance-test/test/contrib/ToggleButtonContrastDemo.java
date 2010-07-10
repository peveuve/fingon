package test.contrib;

import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlueSteelLookAndFeel;

public class ToggleButtonContrastDemo extends JFrame {

	private JPanel contentPane;
	private JXTaskPane taskPane;
	private JPanel panelLeft;
	private JXTaskPaneContainer taskPaneContainerRight;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					try {
						UIManager
								.setLookAndFeel(new SubstanceBusinessBlueSteelLookAndFeel());
					} catch (Exception e) {
						System.out.println("Error loading Look&Feel" + e); //$NON-NLS-1$
						System.exit(0);
					}
					ToggleButtonContrastDemo frame = new ToggleButtonContrastDemo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ToggleButtonContrastDemo() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		{
			panelLeft = new JPanel();
			contentPane.add(panelLeft);
		}
		{
			taskPaneContainerRight = new JXTaskPaneContainer();
			contentPane.add(taskPaneContainerRight);
		}
		{
			taskPane = new JXTaskPane();
			taskPaneContainerRight.add(taskPane);
		}
		{
			JToggleButton tbOnPanelNotSelected = new JToggleButton(
					"Not toggled");
			panelLeft.add(tbOnPanelNotSelected);
			JToggleButton tbOnPanelSelected = new JToggleButton("Toggled");
			tbOnPanelSelected.setSelected(true);
			panelLeft.add(tbOnPanelSelected);
		}
		{
			JToggleButton tbOnTaskpaneNotSelected = new JToggleButton(
					"Not toggled");
			taskPane.add(tbOnTaskpaneNotSelected);
			JToggleButton tbOnTaskpaneSelected = new JToggleButton("Toggled");
			tbOnTaskpaneSelected.setSelected(true);
			taskPane.add(tbOnTaskpaneSelected);
		}
	}

}
