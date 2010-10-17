package org.fingon;

import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.util.Date;

import javax.accessibility.AccessibleContext;
import javax.swing.JComponent;
import javax.swing.JSpinner;
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
public class FingonSpinnerUI extends SpinnerUI implements ChangeListener, FocusListener {

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
	spinner.addChangeListener(this);
	spinner.addFocusListener(this);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JSpinner spinner = (JSpinner)c;
	spinner.removeChangeListener(this);
	spinner.removeFocusListener(this);
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
	    synthesizer.play(text);
	} catch (SynthesisException e1) {}
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
