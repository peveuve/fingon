package test.contrib;

/*
 * Created on 09.04.2008
 *
 * Version: NewTest
 */

/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * SimpleTableDemo.java requires no other files.
 */

import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.*;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.fonts.FontPolicy;
import org.pushingpixels.substance.api.fonts.FontSet;
import org.pushingpixels.substance.api.skin.SubstanceNebulaBrickWallLookAndFeel;

public class SimpleTableDemo extends JPanel {

	public SimpleTableDemo() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		String[] columnNames = { "First Name", "<html>Last<br> Na_p_me",
				"Sport", "# of Years", "Vegetarian" };

		Object[][] data = {
				{ "Mary", "C__ampione", "Snowboarding", new Integer(5),
						new Boolean(false) },
				{ "Alison", "Huml", "Rowing", new Integer(3), new Boolean(true) },
				{ "Kathy", "Walrath", "Knitting", new Integer(2),
						new Boolean(false) },
				{ "Sharon", "Zakhour", "Speed reading", new Integer(20),
						new Boolean(true) },
				{ "Philip", "Milne", "Pool", new Integer(10),
						new Boolean(false) } };

		final JTable table = new JTable(data, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		Dimension d = table.getTableHeader().getPreferredSize();
		double headerHeight = d.getHeight();
		d.setSize(d.getWidth(), headerHeight * 2); // 2 = 2 lines

		table.getTableHeader().setPreferredSize(d);
		table.setAutoCreateRowSorter(true);

		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		JPanel contPanel = new JPanel();
		contPanel.setLayout(new BoxLayout(contPanel, BoxLayout.Y_AXIS));

		// Add the scroll pane to this panel.
		contPanel.add(scrollPane);

		JPanel buttonBarOperation = new JPanel();
		buttonBarOperation.setLayout(new BoxLayout(buttonBarOperation,
				BoxLayout.X_AXIS));
		buttonBarOperation.add(Box.createHorizontalStrut(5), null);
		buttonBarOperation.add(new JButton("Button1"));
		buttonBarOperation.add(Box.createHorizontalStrut(5), null);
		buttonBarOperation.add(new JButton("Button2"));
		buttonBarOperation.add(Box.createHorizontalStrut(5), null);
		buttonBarOperation.add(new JButton("Button3"));
		buttonBarOperation.add(Box.createHorizontalStrut(5), null);
		buttonBarOperation.add(Box.createVerticalGlue());
		buttonBarOperation.setBorder(BorderFactory
				.createTitledBorder("Border1"));

		JPanel buttonBarHeap = new JPanel();
		buttonBarHeap.setLayout(new BoxLayout(buttonBarHeap, BoxLayout.X_AXIS));
		buttonBarHeap.add(Box.createHorizontalStrut(5), null);
		// buttonBarHeap.add(new HeapView());
		// buttonBarHeap.add(Box.createHorizontalStrut(5), null);
		buttonBarHeap.setBorder(BorderFactory.createTitledBorder("Java Heap"));

		JPanel buttonBar = new JPanel();
		buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.X_AXIS));
		buttonBar.add(Box.createHorizontalStrut(5), null);
		buttonBar.add(buttonBarOperation, null);
		buttonBar.add(Box.createHorizontalStrut(5), null);
		buttonBar.add(Box.createHorizontalStrut(5), null);
		buttonBar.add(buttonBarHeap, null);
		buttonBar.add(Box.createHorizontalStrut(5), null);

		contPanel.add(buttonBar);
		contPanel.add(Box.createVerticalStrut(5), null);

		add(contPanel);
		//
		// add(new JButton("button1"));
		// add(Box.createVerticalStrut(5), null);
		// add(new JButton("button2"));
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// TODO Auto-generated method stub
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				try {
					UIManager
							.setLookAndFeel(new SubstanceNebulaBrickWallLookAndFeel());
					SubstanceLookAndFeel.setToUseConstantThemesOnDialogs(true);
				} catch (Exception e) {
				}
				JFrame frame = new JFrame("SimpleTableDemo");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// reset the base font policy to null - this
				// restores the original font policy (default size).
				SubstanceLookAndFeel.setFontPolicy(null);
				// Get the default font set
				final FontSet substanceCoreFontSet = SubstanceLookAndFeel
						.getFontPolicy().getFontSet("Substance", null);
				// Create the wrapper font set
				FontPolicy newFontPolicy = new FontPolicy() {

					public FontSet getFontSet(String lafName, UIDefaults table) {
						return new WrapperFontSet(substanceCoreFontSet, 0);
					}
				};

				try {
					frame.setCursor(Cursor
							.getPredefinedCursor(Cursor.WAIT_CURSOR));
					// set the new font policy
					SubstanceLookAndFeel.setFontPolicy(newFontPolicy);
					frame.setCursor(Cursor.getDefaultCursor());

					// Create and set up the content pane.
					SimpleTableDemo newContentPane = new SimpleTableDemo();
					newContentPane.setOpaque(true); // content panes must be
					// opaque

					frame.setContentPane(newContentPane);

					// Display the window.
					frame.pack();
					frame.setVisible(true);

				} catch (Exception exc) {
					exc.printStackTrace();
				}
				// Create and set up the window.
			}
		});
	}

	public static void main(String[] args) {

		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				createAndShowGUI();
			}
		});
	}
}
