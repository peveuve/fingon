package org.fingon.accessibility;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleIcon;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.swing.AbstractAction;

import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

public class AccessibilityRenderer extends AbstractAction {
    /**
     * AccessibleContextRenderer.java long
     */
    private static final long serialVersionUID = 1L;
    private static AccessibilityRenderer instance;

    private AccessibilityRenderer() {
    	super();
    }

    public static AccessibilityRenderer getInstance() {
	if (instance == null) {
	    instance = new AccessibilityRenderer();
	}
	return instance;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvt) {
	Component source = (Component)actionEvt.getSource();
	AccessibleContext accessCtxt = source.getAccessibleContext();
	renderSummary(accessCtxt);
	renderHelp(accessCtxt);
    }

    public void renderSummary(AccessibleContext accessCtxt) {
	String name = accessCtxt.getAccessibleName();
	if (name == null) {
	    name = "";
	}
	StringBuilder iconsDesc = new StringBuilder("");
	AccessibleIcon[] icons = accessCtxt.getAccessibleIcon();
	if (icons != null) {
	    for (AccessibleIcon icon : icons) {
		String iconDesc = icon.getAccessibleIconDescription();
		try {
		    new URL(iconDesc);
		} catch (MalformedURLException ex) {
		    // only say the description if it is not an URL (the default?)
	    	    iconsDesc.append(iconDesc);
	    	    iconsDesc.append(" ");
		}
	    }
	}
	
	AccessibleRole role = accessCtxt.getAccessibleRole();
	AccessibleState[] accessStates = accessCtxt.getAccessibleStateSet().toArray();
	StringBuilder states = new StringBuilder("");
	
	try {
	    SpeechSynthesizer synthe = SpeechSynthesizerFactory.getSpeechSynthesizer();
	    synthe.load(accessCtxt.getLocale());
	    Locale locale = synthe.getEngineLocale();
	    
	    ResourceBundle label = ResourceBundle.getBundle("message", locale);
	    MessageFormat msg = new MessageFormat(label.getString("accessible"));
	    String[] msgArgs = new String[4];
	    msgArgs[0] = name;
	    msgArgs[1] = role.toDisplayString(locale);
	    for (AccessibleState state : accessStates) {
		if (!state.equals(AccessibleState.ENABLED) 
			&& !state.equals(AccessibleState.ARMED) 
			&& !state.equals(AccessibleState.FOCUSABLE) 
			&& !state.equals(AccessibleState.FOCUSED)
			&& !state.equals(AccessibleState.HORIZONTAL)
			&& !state.equals(AccessibleState.VERTICAL)
			&& !state.equals(AccessibleState.INDETERMINATE)
			&& !state.equals(AccessibleState.MANAGES_DESCENDANTS)
			&& !state.equals(AccessibleState.RESIZABLE)
			&& !state.equals(AccessibleState.TRANSIENT)
			&& !state.equals(AccessibleState.TRUNCATED)
			&& !state.equals(AccessibleState.OPAQUE)
			&& !state.equals(AccessibleState.SHOWING)
			&& !state.equals(AccessibleState.VISIBLE)) {
			states.append(state.toDisplayString(locale));
			states.append(" ");
		}
	    }
	    msgArgs[2] = states.toString();
	    msgArgs[3] = iconsDesc.toString();
	    String formatedMsg = msg.format(msgArgs);
	    synthe.play(formatedMsg);
	} catch (SynthesisException e) {}
    }
    
    public void renderHelp(AccessibleContext accessCtxt) {	
    	String desc = accessCtxt.getAccessibleDescription();
    	if (desc != null) {
	    try {
		SpeechSynthesizer synthe = SpeechSynthesizerFactory.getSpeechSynthesizer();
		synthe.load(accessCtxt.getLocale());
		synthe.play(desc);
	    } catch (SynthesisException e) {}
    	}
    }
}
