package org.fingon;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 * 
 * @author Paul-Emile
 */
public class FingonFormattedTextFieldUI extends FingonTextUI {

    /**
     * 
     */
    public FingonFormattedTextFieldUI() {
	super();
    }

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	return new FingonFormattedTextFieldUI();
    }
    
    @Override
    protected String getPropertyPrefix() {
	return "FormattedTextField";
    }
}
