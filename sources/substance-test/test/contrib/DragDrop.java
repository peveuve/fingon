package test.contrib;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;

public class DragDrop {

	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {

				try {
					JFrame.setDefaultLookAndFeelDecorated(true);
					UIManager
							.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());

					final JTree lTree = new JTree(new Object[] { "One", "Two",
							"Three" });
					lTree.setDragEnabled(true);
					lTree.setTransferHandler(new TransferHandler() {
						@Override
						public int getSourceActions(JComponent c) {
							return COPY_OR_MOVE;
						}

						@Override
						protected Transferable createTransferable(JComponent c) {
							TreePath path = lTree.getSelectionPath();
							final DefaultMutableTreeNode lNode = (DefaultMutableTreeNode) path
									.getLastPathComponent();
							String lObj = (String) lNode.getUserObject();
							return new StringSelection(lObj);
						}
					});

					JFrame lFrame = new JFrame("Test");
					lFrame.add(lTree);
					lFrame.pack();
					lFrame.setVisible(true);

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
	}

}
