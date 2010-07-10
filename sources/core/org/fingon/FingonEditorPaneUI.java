package org.fingon;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 * 
 * @author Paul-Emile
 */
public class FingonEditorPaneUI extends FingonTextFieldUI {

    /**
     * 
     */
    public FingonEditorPaneUI() {
	super();
    }

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	return new FingonEditorPaneUI();
    }
    
    @Override
    protected String getPropertyPrefix() {
	return "EditorPane";
    }
}
