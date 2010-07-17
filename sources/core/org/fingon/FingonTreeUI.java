package org.fingon;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TreeUI;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.fingon.player.PlayException;
import org.fingon.player.PlayerFactory;
import org.fingon.player.SoundPlayer;
import org.fingon.synthesizer.SpeechSynthesizer;

/**
 * @author Paul-Emile
 * 
 */
public class FingonTreeUI extends TreeUI implements TreeSelectionListener, TreeExpansionListener, TreeModelListener, PropertyChangeListener {
    /** the instance common to every component */
    private static FingonTreeUI instance;
    /** expanded sound URL */
    private URL expandedSound;
    /** collapsed sound URL */
    private URL collapsedSound;

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	if (instance == null) {
	    instance = new FingonTreeUI();
	}
	return instance;
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	JTree tree = (JTree)c;
	tree.addTreeSelectionListener(this);
	tree.addTreeExpansionListener(this);
	tree.addPropertyChangeListener(this);
	TreeModel treeModel = tree.getModel();
	if (treeModel != null) {
	    treeModel.addTreeModelListener(this);
	}
	expandedSound = (URL)UIManager.get("Tree.expandedSound");
	collapsedSound = (URL)UIManager.get("Tree.collapsedSound");
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JTree tree = (JTree)c;
	tree.removeTreeSelectionListener(this);
	tree.removeTreeExpansionListener(this);
	tree.removePropertyChangeListener(this);
	TreeModel treeModel = tree.getModel();
	if (treeModel != null) {
	    treeModel.removeTreeModelListener(this);
	}
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
    }

    /**
     * @see javax.swing.plaf.TreeUI#cancelEditing(javax.swing.JTree)
     */
    @Override
    public void cancelEditing(JTree tree) {
    }

    /**
     * @see javax.swing.plaf.TreeUI#getClosestPathForLocation(javax.swing.JTree, int, int)
     */
    @Override
    public TreePath getClosestPathForLocation(JTree tree, int x, int y) {
	return null;
    }

    /**
     * @see javax.swing.plaf.TreeUI#getEditingPath(javax.swing.JTree)
     */
    @Override
    public TreePath getEditingPath(JTree tree) {
	return null;
    }

    /**
     * @see javax.swing.plaf.TreeUI#getPathBounds(javax.swing.JTree, javax.swing.tree.TreePath)
     */
    @Override
    public Rectangle getPathBounds(JTree tree, TreePath path) {
	return null;
    }

    /**
     * @see javax.swing.plaf.TreeUI#getPathForRow(javax.swing.JTree, int)
     */
    @Override
    public TreePath getPathForRow(JTree tree, int row) {
	return null;
    }

    /**
     * @see javax.swing.plaf.TreeUI#getRowCount(javax.swing.JTree)
     */
    @Override
    public int getRowCount(JTree tree) {
	return 0;
    }

    /**
     * @see javax.swing.plaf.TreeUI#getRowForPath(javax.swing.JTree, javax.swing.tree.TreePath)
     */
    @Override
    public int getRowForPath(JTree tree, TreePath path) {
	return 0;
    }

    /**
     * @see javax.swing.plaf.TreeUI#isEditing(javax.swing.JTree)
     */
    @Override
    public boolean isEditing(JTree tree) {
	return false;
    }

    /**
     * @see javax.swing.plaf.TreeUI#startEditingAtPath(javax.swing.JTree, javax.swing.tree.TreePath)
     */
    @Override
    public void startEditingAtPath(JTree tree, TreePath path) {
    }

    /**
     * @see javax.swing.plaf.TreeUI#stopEditing(javax.swing.JTree)
     */
    @Override
    public boolean stopEditing(JTree tree) {
	return false;
    }

    /**
     * Says the label of the selected node in the tree.
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    public void valueChanged(TreeSelectionEvent e) {
	TreePath selectedPath = e.getNewLeadSelectionPath();
	if (selectedPath != null) {
	    Object selectedComponent = selectedPath.getLastPathComponent();
	    SpeechSynthesizer synthesizer = PlayerFactory.getSpeechSynthesizer();
	    synthesizer.stop();
	    synthesizer.play(selectedComponent.toString());
	}
    }

    /**
     * Plays a sound when collapsing a node in the tree.
     * @see javax.swing.event.TreeExpansionListener#treeCollapsed(javax.swing.event.TreeExpansionEvent)
     */
    public void treeCollapsed(TreeExpansionEvent event) {
        try {
            SoundPlayer player = (SoundPlayer)PlayerFactory.getPlayerByExtension("wav");
            player.play(collapsedSound);
        } catch (PlayException e) {
        }
    }

    /**
     * Plays a sound when expanding a node in the tree.
     * @see javax.swing.event.TreeExpansionListener#treeExpanded(javax.swing.event.TreeExpansionEvent)
     */
    public void treeExpanded(TreeExpansionEvent event) {
        try {
            SoundPlayer player = (SoundPlayer)PlayerFactory.getPlayerByExtension("wav");
            player.play(expandedSound);
        } catch (PlayException e) {
        }
    }

    /**
     * Says one or several nodes have changed in the tree.
     * @see javax.swing.event.TreeModelListener#treeNodesChanged(javax.swing.event.TreeModelEvent)
     */
    public void treeNodesChanged(TreeModelEvent e) {
	Object[] changedChildren = e.getChildren();
	Object source = e.getSource();
	SpeechSynthesizer synthesizer = PlayerFactory.getSpeechSynthesizer();
	synthesizer.stop();
	for (Object changedChild : changedChildren) {
	    synthesizer.play(changedChild + " changed in " + source);
	}
    }

    /**
     * Says one or several nodes have been inserted in the tree.
     * @see javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event.TreeModelEvent)
     */
    public void treeNodesInserted(TreeModelEvent e) {
	Object[] insertedChildren = e.getChildren();
	Object source = e.getSource();
	SpeechSynthesizer synthesizer = PlayerFactory.getSpeechSynthesizer();
	synthesizer.stop();
	for (Object insertedChild : insertedChildren) {
	    synthesizer.play(insertedChild + " added to " + source);
	}
    }

    /**
     * Says one or several nodes have removed in the tree.
     * @see javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event.TreeModelEvent)
     */
    public void treeNodesRemoved(TreeModelEvent e) {
	Object[] removedChildren = e.getChildren();
	Object source = e.getSource();
	SpeechSynthesizer synthesizer = PlayerFactory.getSpeechSynthesizer();
	synthesizer.stop();
	for (Object removedChild : removedChildren) {
	    synthesizer.play(removedChild + " removed from " + source);
	}
    }

    public void treeStructureChanged(TreeModelEvent e) {
    }

    /**
     * Adds this UI as tree model listener when the tree model has changed, 
     * to be able to react when one or several nodes have changed in the tree.  
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	String propertyName = evt.getPropertyName();
	if (propertyName.equals(JTree.TREE_MODEL_PROPERTY)) {
	    TreeModel newTreeModel = (TreeModel)evt.getNewValue();
	    if (newTreeModel != null) {
		newTreeModel.addTreeModelListener(this);
	    }
	    TreeModel oldTreeModel = (TreeModel)evt.getOldValue();
	    if (oldTreeModel != null) {
		oldTreeModel.removeTreeModelListener(this);
	    }
	}
    }
}
