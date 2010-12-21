package org.fingon;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.accessibility.Accessible;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.ComponentUI;

import org.fingon.accessibility.AccessibilityRenderer;
import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

public class FingonComboBoxUI extends ComboBoxUI implements ActionListener {
    /** the instance common to every component */
    private static FingonComboBoxUI instance;

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
	String helpKey = UIManager.getString("Fingon.helpKey");
	inputMap.put(KeyStroke.getKeyStroke(helpKey), "FingonUIHelp");
	ActionMap actionMap = combobox.getActionMap();
	actionMap.put("FingonUIHelp", AccessibilityRenderer.getInstance());
	combobox.addActionListener(this);
	combobox.addFocusListener(AccessibilityRenderer.getInstance());
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JComboBox combobox = (JComboBox)c;
	InputMap inputMap = combobox.getInputMap();
	String helpKey = UIManager.getString("Fingon.helpKey");
	inputMap.remove(KeyStroke.getKeyStroke(helpKey));
	ActionMap actionMap = combobox.getActionMap();
	actionMap.remove("FingonUIHelp");
	combobox.removeActionListener(this);
	combobox.removeFocusListener(AccessibilityRenderer.getInstance());
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

    /**
     * Overridden to avoid a StackOverflowError when getting the focus (do not call super.getAccessibleChild()).
     * @see javax.swing.plaf.ComponentUI#getAccessibleChild(javax.swing.JComponent, int)
     */
    @Override
    public Accessible getAccessibleChild(JComponent c, int i) {
	return null;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
	JComboBox comboBox = (JComboBox)evt.getSource();
	if (comboBox.isShowing()) {
	    Object selectedItem = comboBox.getSelectedItem();
	    if (selectedItem != null) {
    	    	try {
    	    	    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
    	    	    synthesizer.stop();
    	    	    synthesizer.load(comboBox.getLocale());
        	    synthesizer.play(selectedItem.toString());
    	    	} catch (SynthesisException ex) {}
	    }
	}
    }
}
