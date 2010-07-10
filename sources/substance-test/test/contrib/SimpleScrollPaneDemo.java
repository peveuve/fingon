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

import javax.swing.*;

import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.preview.DefaultPreviewPainter;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceNebulaBrickWallLookAndFeel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class SimpleScrollPaneDemo extends JPanel {

	public SimpleScrollPaneDemo() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JScrollPane scrPane = new JScrollPane(buildLayout());
		scrPane.putClientProperty(LafWidget.COMPONENT_PREVIEW_PAINTER,
				new DefaultPreviewPainter());

		add(scrPane);
	}

	private JPanel buildLayout() {
		FormLayout layout = new FormLayout("3dlu, p, 3dlu,", // cols
				"3dlu, p, 3dlu, f:p:g, 3dlu, p, 3dlu" // rows
		);
		PanelBuilder builder = new PanelBuilder(layout);

		CellConstraints cc = new CellConstraints();
		int startCol = 2;
		int rows = 2;
		int cols = startCol;

		cols = startCol;
		builder.add(new JLabel("xxxxx"), cc.xy(cols, rows));
		rows++;
		rows++;
		builder.add(new JLabel("yyyyyyyyy"), cc.xy(cols, rows));
		rows++;
		rows++;
		builder.add(new JLabel("zzzzzzz"), cc.xy(cols, rows));

		return builder.getPanel();
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				try {
					UIManager
							.setLookAndFeel(new SubstanceNebulaBrickWallLookAndFeel());
					SubstanceLookAndFeel.setToUseConstantThemesOnDialogs(true);
					UIManager.put(SubstanceLookAndFeel.SHOW_EXTRA_WIDGETS,
							Boolean.TRUE);
				} catch (Exception e) {
				}
				JFrame frame = new JFrame("SimpleScrollDemo");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				try {

					// Create and set up the content pane.
					SimpleScrollPaneDemo newContentPane = new SimpleScrollPaneDemo();
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
