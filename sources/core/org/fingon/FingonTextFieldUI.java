package org.fingon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.JTextComponent;

import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

/**
 * 
 * @author Paul-Emile
 */
public class FingonTextFieldUI extends FingonTextUI implements ActionListener {
    
    /**
     * 
     */
    public FingonTextFieldUI() {
	super();
    }

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	return new FingonTextFieldUI();
    }
    
    @Override
    protected String getPropertyPrefix() {
	return "TextField";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	JTextComponent textComponent = (JTextComponent)e.getSource();
	if (!typedString.isEmpty()) {
	    try {
		SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
		synthesizer.stop();
		synthesizer.load(textComponent.getLocale());
		synthesizer.play(typedString);
	    } catch (SynthesisException e1) {}
	    typedString = "";
	}
    }
}
