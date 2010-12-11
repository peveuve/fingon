package org.fingon;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.accessibility.AccessibleContext;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TabbedPaneUI;

import org.fingon.accessibility.AccessibilityRenderer;
import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

public class FingonTabbedPaneUI extends TabbedPaneUI implements ChangeListener, FocusListener {
    private static FingonTabbedPaneUI instance;
    /** 
     * Last accessibility summary sent to the speaker when a component get the focus.
     * The last one will always be the one losing the focus, and so the one to cancel.
     */
    private String accessibilitySummary;
    
    public FingonTabbedPaneUI(JTabbedPane c) {
    }

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	if (instance == null) {
	    instance = new FingonTabbedPaneUI((JTabbedPane)c);
	}
	return instance;
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	JTabbedPane tabbedPane = (JTabbedPane)c;
	InputMap inputMap = tabbedPane.getInputMap();
	inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "FingonUIHelp");
	ActionMap actionMap = tabbedPane.getActionMap();
	actionMap.put("FingonUIHelp", AccessibilityRenderer.getInstance());
	tabbedPane.addChangeListener(this);
	tabbedPane.addFocusListener(this);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JTabbedPane tabbedPane = (JTabbedPane)c;
	InputMap inputMap = tabbedPane.getInputMap();
	inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
	ActionMap actionMap = tabbedPane.getActionMap();
	actionMap.remove("FingonUIHelp");
	tabbedPane.removeChangeListener(this);
	tabbedPane.removeFocusListener(this);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
    }


    @Override
    public Rectangle getTabBounds(JTabbedPane arg0, int arg1) {
	return null;
    }

    @Override
    public int getTabRunCount(JTabbedPane pane) {
	return 0;
    }

    @Override
    public int tabForCoordinate(JTabbedPane pane, int x, int y) {
	return 0;
    }

    @Override
    public void stateChanged(ChangeEvent event) {
	JTabbedPane tabbedPane = (JTabbedPane)event.getSource();
	if (tabbedPane.isShowing()) {
	    int  selectedTabIndex = tabbedPane.getSelectedIndex();
	    if (selectedTabIndex != -1) {
    	    	String selectedTabTitle = tabbedPane.getTitleAt(selectedTabIndex);
    	    	if (selectedTabTitle != null) {
    	    	    try {
    	    		SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
    	    		synthesizer.stop();
    	    		synthesizer.load(tabbedPane.getLocale());
    	    		synthesizer.play(selectedTabTitle);
    	    	    } catch (SynthesisException ex) {}
    	    	}
	    }
	}
    }

    @Override
    public void focusGained(FocusEvent e) {
	AccessibleContext accessCtxt = e.getComponent().getAccessibleContext();
	accessibilitySummary = AccessibilityRenderer.getInstance().renderSummary(accessCtxt);
    }

    @Override
    public void focusLost(FocusEvent e) {
	try {
	    SpeechSynthesizer synthe = SpeechSynthesizerFactory.getSpeechSynthesizer();
	    synthe.cancel(accessibilitySummary);
	} catch (SynthesisException ex) {}
    }
}
