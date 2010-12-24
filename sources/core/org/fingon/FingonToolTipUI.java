package org.fingon;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ToolTipUI;

import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

/**
 * @author Paul-Emile
 * 
 */
public class FingonToolTipUI extends ToolTipUI implements AncestorListener {
    /** the instance common to every component */
    private static FingonToolTipUI instance;

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	if (instance == null) {
	    instance = new FingonToolTipUI();
	}
	return instance;
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	JToolTip tooltip = (JToolTip)c;
	tooltip.addAncestorListener(this);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JToolTip tooltip = (JToolTip)c;
	tooltip.removeAncestorListener(this);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
    }

    public void ancestorAdded(AncestorEvent event) {
	JToolTip tooltip = (JToolTip)event.getComponent();
	String tipText = tooltip.getTipText();
	if (tipText != null) {
	    tipText = tipText.replaceAll("<[^>]+>", "");
	    try {
		SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
		synthesizer.stop();
		synthesizer.load(tooltip.getLocale());
		synthesizer.play(tipText);
	    } catch (SynthesisException e1) {}
	}
    }

    public void ancestorMoved(AncestorEvent event) {
    }

    public void ancestorRemoved(AncestorEvent event) {
	try {
	    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
	    synthesizer.stop();
    	} catch (SynthesisException e1) {}
    }
}
