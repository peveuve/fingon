package org.fingon;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.accessibility.AccessibleContext;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.KeyStroke;
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

import org.fingon.accessibility.AccessibilityRenderer;
import org.fingon.player.PlayException;
import org.fingon.player.PlayerFactory;
import org.fingon.player.SoundPlayer;
import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

/**
 * @author Paul-Emile
 * 
 */
public class FingonTreeUI extends TreeUI implements TreeSelectionListener, TreeExpansionListener, TreeModelListener, PropertyChangeListener, FocusListener {
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
	InputMap inputMap = tree.getInputMap();
	inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "FingonUIHelp");
	ActionMap actionMap = tree.getActionMap();
	actionMap.put("FingonUIHelp", AccessibilityRenderer.getInstance());
	tree.addTreeSelectionListener(this);
	tree.addTreeExpansionListener(this);
	tree.addPropertyChangeListener(this);
	tree.addFocusListener(this);
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
	InputMap inputMap = tree.getInputMap();
	inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
	ActionMap actionMap = tree.getActionMap();
	actionMap.remove("FingonUIHelp");
	tree.removeTreeSelectionListener(this);
	tree.removeTreeExpansionListener(this);
	tree.removePropertyChangeListener(this);
	tree.removeFocusListener(this);
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
	    try {
		SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
		synthesizer.stop();
		synthesizer.play(selectedComponent.toString());
	    } catch (SynthesisException e1) {}
	}
    }

    /**
     * Plays a sound when collapsing a node in the tree.
     * @see javax.swing.event.TreeExpansionListener#treeCollapsed(javax.swing.event.TreeExpansionEvent)
     */
    public void treeCollapsed(TreeExpansionEvent event) {
	JTree tree = (JTree)event.getSource();
	if (tree.isShowing()) {
	    if (collapsedSound != null) {
    	    	try {
    	    	    SoundPlayer player = (SoundPlayer)PlayerFactory.getPlayerByExtension("wav");
    	    	    player.play(collapsedSound);
    	    	} catch (PlayException e) {}
	    }
	}
    }

    /**
     * Plays a sound when expanding a node in the tree.
     * @see javax.swing.event.TreeExpansionListener#treeExpanded(javax.swing.event.TreeExpansionEvent)
     */
    public void treeExpanded(TreeExpansionEvent event) {
	JTree tree = (JTree)event.getSource();
	if (tree.isShowing()) {
	    if (expandedSound != null) {
    	    	try {
    	    	    SoundPlayer player = (SoundPlayer)PlayerFactory.getPlayerByExtension("wav");
    	    	    player.play(expandedSound);
    	    	} catch (PlayException e) {}
            }
	}
    }

    /**
     * Says one or several nodes have changed in the tree.
     * @see javax.swing.event.TreeModelListener#treeNodesChanged(javax.swing.event.TreeModelEvent)
     */
    public void treeNodesChanged(TreeModelEvent e) {
	Object[] changedChildren = e.getChildren();
	Object[] path = e.getPath();
	Object parentNode = path[path.length-1];
	
	StringBuilder nodesChangedList = new StringBuilder();
	for (Object changedChild : changedChildren) {
	    nodesChangedList.append(changedChild);
	    nodesChangedList.append(", ");
	}
	Object[] nodesChangedMsgArgs = {nodesChangedList, parentNode};

	try {
	    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();

	    ResourceBundle msg = ResourceBundle.getBundle("message", synthesizer.getEngineLocale());
	    String nodesChangedMsgPattern = msg.getString("nodesChanged");
	    MessageFormat nodesChangedMsgFormat = new MessageFormat(nodesChangedMsgPattern);
	    String nodesChangedMsg = nodesChangedMsgFormat.format(nodesChangedMsgArgs);
	    
	    synthesizer.stop();
	    synthesizer.play(nodesChangedMsg);
    	} catch (SynthesisException e1) {}
    }

    /**
     * Says one or several nodes have been inserted in the tree.
     * @see javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event.TreeModelEvent)
     */
    public void treeNodesInserted(TreeModelEvent e) {
	Object[] insertedChildren = e.getChildren();
	Object[] path = e.getPath();
	Object parentNode = path[path.length-1];
	
	StringBuilder nodesAddedList = new StringBuilder();
	for (Object insertedChild : insertedChildren) {
	    nodesAddedList.append(insertedChild);
	    nodesAddedList.append(", ");
	}
	Object[] nodesAddedMsgArgs = {nodesAddedList, parentNode};
	
	try {
	    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();

	    ResourceBundle msg = ResourceBundle.getBundle("message", synthesizer.getEngineLocale());
	    String nodesAddedMsgPattern = msg.getString("nodesAdded");
	    MessageFormat nodesAddedMsgFormat = new MessageFormat(nodesAddedMsgPattern);
	    String nodesAddedMsg = nodesAddedMsgFormat.format(nodesAddedMsgArgs);
	    
	    synthesizer.stop();
	    synthesizer.play(nodesAddedMsg);
    	} catch (SynthesisException e1) {}
    }

    /**
     * Says one or several nodes have removed in the tree.
     * @see javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event.TreeModelEvent)
     */
    public void treeNodesRemoved(TreeModelEvent e) {
	Object[] removedChildren = e.getChildren();
	Object[] path = e.getPath();
	Object parentNode = path[path.length-1];
	
	StringBuilder nodesRemovedList = new StringBuilder();
	for (Object removedChild : removedChildren) {
	    nodesRemovedList.append(removedChild);
	    nodesRemovedList.append(", ");
	}
	Object[] nodesRemovedMsgArgs = {nodesRemovedList, parentNode};
	
	try {
	    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();

	    ResourceBundle msg = ResourceBundle.getBundle("message", synthesizer.getEngineLocale());
	    String nodesRemovedMsgPattern = msg.getString("nodesRemoved");
	    MessageFormat nodesRemovedMsgFormat = new MessageFormat(nodesRemovedMsgPattern);
	    String nodesRemovedMsg = nodesRemovedMsgFormat.format(nodesRemovedMsgArgs);
	    
	    synthesizer.stop();
	    synthesizer.play(nodesRemovedMsg);
    	} catch (SynthesisException e1) {}
    }

    public void treeStructureChanged(TreeModelEvent e) {
    }

    /**
     * Adds this UI as tree model listener when the tree model has changed, 
     * to be able to react when one or several nodes changed in the tree.  
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

    @Override
    public void focusGained(FocusEvent e) {
	AccessibleContext accessCtxt = e.getComponent().getAccessibleContext();
	AccessibilityRenderer.getInstance().renderSummary(accessCtxt);
    }

    @Override
    public void focusLost(FocusEvent e) {
    }
}
