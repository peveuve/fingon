package org.fingon;

import java.awt.Graphics;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.SpinnerUI;

import org.fingon.accessibility.AccessibilityRenderer;
import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

/**
 * 
 * @author Paul-Emile
 */
public class FingonSpinnerUI extends SpinnerUI implements ChangeListener {

    /** the instance common to every component */
    private static FingonSpinnerUI instance;

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	if (instance == null) {
	    instance = new FingonSpinnerUI();
	}
	return instance;
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	JSpinner spinner = (JSpinner)c;
	InputMap inputMap = spinner.getInputMap();
	String helpKey = UIManager.getString("Fingon.helpKey");
	inputMap.put(KeyStroke.getKeyStroke(helpKey), "FingonUIHelp");
	ActionMap actionMap = spinner.getActionMap();
	actionMap.put("FingonUIHelp", AccessibilityRenderer.getInstance());
	spinner.addChangeListener(this);
	spinner.addFocusListener(AccessibilityRenderer.getInstance());
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JSpinner spinner = (JSpinner)c;
	InputMap inputMap = spinner.getInputMap();
	String helpKey = UIManager.getString("Fingon.helpKey");
	inputMap.remove(KeyStroke.getKeyStroke(helpKey));
	ActionMap actionMap = spinner.getActionMap();
	actionMap.remove("FingonUIHelp");
	spinner.removeChangeListener(this);
	spinner.removeFocusListener(AccessibilityRenderer.getInstance());
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
    }

    @Override
    public void stateChanged(ChangeEvent ev) {
	JSpinner spinner = (JSpinner)ev.getSource();
	String text = null;
	Object value = spinner.getValue();
	if (value instanceof Date) {
	    DateFormat dateFmt = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
	    text = dateFmt.format((Date)value);
	} else {
	    text = value.toString();
	}
	try {
	    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
	    synthesizer.stop();
	    synthesizer.load(spinner.getLocale());
	    synthesizer.play(text);
	} catch (SynthesisException e1) {}
    }
}
