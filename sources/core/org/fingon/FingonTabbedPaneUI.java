package org.fingon;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TabbedPaneUI;

import org.fingon.accessibility.AccessibilityRenderer;
import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

public class FingonTabbedPaneUI extends TabbedPaneUI implements ChangeListener {
    private static FingonTabbedPaneUI instance;
    
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
	String helpKey = UIManager.getString("Fingon.helpKey");
	inputMap.put(KeyStroke.getKeyStroke(helpKey), "FingonUIHelp");
	ActionMap actionMap = tabbedPane.getActionMap();
	actionMap.put("FingonUIHelp", AccessibilityRenderer.getInstance());
	tabbedPane.addChangeListener(this);
	tabbedPane.addFocusListener(AccessibilityRenderer.getInstance());
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JTabbedPane tabbedPane = (JTabbedPane)c;
	InputMap inputMap = tabbedPane.getInputMap();
	String helpKey = UIManager.getString("Fingon.helpKey");
	inputMap.remove(KeyStroke.getKeyStroke(helpKey));
	ActionMap actionMap = tabbedPane.getActionMap();
	actionMap.remove("FingonUIHelp");
	tabbedPane.removeChangeListener(this);
	tabbedPane.removeFocusListener(AccessibilityRenderer.getInstance());
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
}
