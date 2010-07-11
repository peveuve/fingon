package test;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class MaximizeBounds extends JFrame {
	protected GraphicsConfiguration currentRootPaneGC;

	public MaximizeBounds() {
		super("Double click the title!");

		setSize(400, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ComponentListener maxListener = new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				this.processNewPosition();
			}

			@Override
			public void componentResized(ComponentEvent e) {
				this.processNewPosition();
			}

			protected void processNewPosition() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						Window window = MaximizeBounds.this;
						if (window == null)
							return;

						if (!window.isShowing() || !window.isDisplayable()) {
							currentRootPaneGC = null;
							return;
						}

						GraphicsEnvironment ge = GraphicsEnvironment
								.getLocalGraphicsEnvironment();
						GraphicsDevice[] gds = ge.getScreenDevices();
						if (gds.length == 1)
							return;
						Point midLoc = new Point(window.getLocationOnScreen().x
								+ window.getWidth() / 2, window
								.getLocationOnScreen().y
								+ window.getHeight() / 2);
						int index = 0;
						for (GraphicsDevice gd : gds) {
							GraphicsConfiguration gc = gd
									.getDefaultConfiguration();
							Rectangle bounds = gc.getBounds();
							if (bounds.contains(midLoc)) {
								if (gc != currentRootPaneGC) {
									currentRootPaneGC = gc;
									setMaximized();
								}
								break;
							}
							index++;
						}
					}
				});
			}
		};
		addComponentListener(maxListener);
	}

	public void setMaximized() {
		GraphicsConfiguration gc = (currentRootPaneGC != null) ? currentRootPaneGC
				: getGraphicsConfiguration();
		Rectangle screenBounds = gc.getBounds();
		screenBounds.x = 0;
		screenBounds.y = 0;
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
		Rectangle maxBounds = new Rectangle(
				(screenBounds.x + screenInsets.left),
				(screenBounds.y + screenInsets.top), screenBounds.width
						- ((screenInsets.left + screenInsets.right)),
				screenBounds.height
						- ((screenInsets.top + screenInsets.bottom)));
		setMaximizedBounds(maxBounds);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame.setDefaultLookAndFeelDecorated(true);
				try {
					UIManager.setLookAndFeel(new MetalLookAndFeel());
				} catch (Exception e) {
				}
				new MaximizeBounds().setVisible(true);
			}
		});
	}

}
