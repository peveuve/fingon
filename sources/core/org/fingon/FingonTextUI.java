package org.fingon;

import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.accessibility.AccessibleContext;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.JTextComponent;

import org.fingon.accessibility.AccessibilityRenderer;
import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

/**
 * 
 * @author Paul-Emile
 */
public class FingonTextUI extends BasicTextUI implements KeyListener, CaretListener, FocusListener {
    protected String typedString = "";
    
    /**
     * 
     */
    public FingonTextUI() {
	super();
    }

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	return new FingonTextUI();
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	super.installUI(c);
	JTextComponent textc = (JTextComponent)c;
	InputMap inputMap = textc.getInputMap();
	inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "FingonUIHelp");
	ActionMap actionMap = textc.getActionMap();
	actionMap.put("FingonUIHelp", AccessibilityRenderer.getInstance());
	textc.addKeyListener(this);
	textc.addCaretListener(this);
	textc.addFocusListener(this);
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	super.uninstallUI(c);
	JTextComponent textc = (JTextComponent)c;
	InputMap inputMap = textc.getInputMap();
	inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
	ActionMap actionMap = textc.getActionMap();
	actionMap.remove("FingonUIHelp");
	textc.removeKeyListener(this);
	textc.removeCaretListener(this);
	textc.removeFocusListener(this);
    }

    /**
     * Do not clear the component view if it is opaque.
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
	
    }

    public void caretUpdate(CaretEvent e) {
	int dot = e.getDot();
	int mark = e.getMark();
	if (mark != dot) {
	    JTextComponent textComponent = (JTextComponent)e.getSource();
	    String selectedText = textComponent.getSelectedText();
	    try {
		SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
    	    	if (selectedText != null) {
    	    	    synthesizer.stop();
    	    	    synthesizer.load(textComponent.getLocale());
    	    	    synthesizer.play(selectedText);
    	    	}
	    } catch (SynthesisException e1) {}
	}
    }

    public void keyPressed(KeyEvent e) {
	int typedKeyCode = e.getKeyCode();
	char typedChar = e.getKeyChar();
	typedString = typedString + typedChar;
	if (typedKeyCode == KeyEvent.VK_SPACE || typedKeyCode == KeyEvent.VK_ENTER || typedKeyCode == KeyEvent.VK_TAB) {
	    if (!typedString.isEmpty()) {
    	    	JTextComponent textComponent = (JTextComponent)e.getSource();
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

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public void focusGained(FocusEvent e) {
	AccessibleContext accessCtxt = e.getComponent().getAccessibleContext();
	AccessibilityRenderer.getInstance().renderSummary(accessCtxt);
    }

    @Override
    public void focusLost(FocusEvent e) {
    }

    @Override
    protected String getPropertyPrefix() {
	return "TextComponent";
    }
}
