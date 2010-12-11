package org.fingon;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.accessibility.AccessibleContext;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.ComponentUI;

import org.fingon.accessibility.AccessibilityRenderer;
import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

public class FingonComboBoxUI extends ComboBoxUI implements FocusListener, ActionListener {
    /** the instance common to every component */
    private static FingonComboBoxUI instance;
    /** last accessibility summary sent to the speaker */
    private String accessibilitySummary;

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	if (instance == null) {
	    instance = new FingonComboBoxUI();
	}
	return instance;
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	JComboBox combobox = (JComboBox)c;
	InputMap inputMap = combobox.getInputMap();
	inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "FingonUIHelp");
	ActionMap actionMap = combobox.getActionMap();
	actionMap.put("FingonUIHelp", AccessibilityRenderer.getInstance());
	combobox.addActionListener(this);
	combobox.addFocusListener(this);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JComboBox combobox = (JComboBox)c;
	InputMap inputMap = combobox.getInputMap();
	inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
	ActionMap actionMap = combobox.getActionMap();
	actionMap.remove("FingonUIHelp");
	combobox.removeActionListener(this);
	combobox.removeFocusListener(this);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
    }

    @Override
    public boolean isFocusTraversable(JComboBox arg0) {
	return false;
    }

    @Override
    public boolean isPopupVisible(JComboBox arg0) {
	return false;
    }

    @Override
    public void setPopupVisible(JComboBox arg0, boolean arg1) {
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

    @Override
    public void actionPerformed(ActionEvent evt) {
	JComboBox comboBox = (JComboBox)evt.getSource();
	if (comboBox.isShowing()) {
	    Object selectedItem = comboBox.getSelectedItem();
	    try {
	        SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
	        synthesizer.stop();
	        synthesizer.load(comboBox.getLocale());
    	        synthesizer.play(selectedItem.toString());
	    } catch (SynthesisException ex) {}
	}
    }
}
