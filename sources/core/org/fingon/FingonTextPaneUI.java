package org.fingon;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 * 
 * @author Paul-Emile
 */
public class FingonTextPaneUI extends FingonTextUI {

    /**
     * 
     */
    public FingonTextPaneUI() {
	super();
    }

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	return new FingonTextPaneUI();
    }
    
    @Override
    protected String getPropertyPrefix() {
	return "TextPane";
    }
}
