package org.fingon;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ListUI;

import org.fingon.accessibility.AccessibilityRenderer;
import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

/**
 * Speaks the selected item(s) in the list.
 * @author Paul-Emile
 */
public class FingonListUI extends ListUI implements ListSelectionListener {
    /** the instance common to every component */
    private static FingonListUI instance;

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	if (instance == null) {
	    instance = new FingonListUI();
	}
	return instance;
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	JList list = (JList)c;
	InputMap inputMap = list.getInputMap();
	String helpKey = UIManager.getString("Fingon.helpKey");
	inputMap.put(KeyStroke.getKeyStroke(helpKey), "FingonUIHelp");
	ActionMap actionMap = list.getActionMap();
	actionMap.put("FingonUIHelp", AccessibilityRenderer.getInstance());
	list.addListSelectionListener(this);
	list.addFocusListener(AccessibilityRenderer.getInstance());
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JList list = (JList)c;
	InputMap inputMap = list.getInputMap();
	String helpKey = UIManager.getString("Fingon.helpKey");
	inputMap.remove(KeyStroke.getKeyStroke(helpKey));
	ActionMap actionMap = list.getActionMap();
	actionMap.remove("FingonUIHelp");
	list.removeListSelectionListener(this);
	list.removeFocusListener(AccessibilityRenderer.getInstance());
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
    }

    @Override
    public Rectangle getCellBounds(JList list, int index1, int index2) {
	return new Rectangle(0, 0);
    }

    @Override
    public Point indexToLocation(JList list, int index) {
	return null;
    }

    @Override
    public int locationToIndex(JList list, Point location) {
	return 0;
    }

    /**
     * 
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent e) {
	JList changedList = (JList)e.getSource();
	if (changedList.isShowing()) {
	    if (!e.getValueIsAdjusting()) {
    	    	Object[] selectedValues = changedList.getSelectedValues();
    	    	if (selectedValues != null && selectedValues.length > 0) {
    	    	    try {
    	    		SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
        	    	synthesizer.stop();
        	    	synthesizer.load(changedList.getLocale());
        	    	for (Object value : selectedValues) {
        	    	    synthesizer.play(value.toString());
        	    	}
    	    	    } catch (SynthesisException e1) {}
    	    	}
	    }
	}
    }
}
