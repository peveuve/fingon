package org.fingon;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleState;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.plaf.ComponentUI;

import org.fingon.player.PlayException;
import org.fingon.player.PlayerFactory;
import org.fingon.player.SoundPlayer;
import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

/**
 * 
 * @author Paul-Emile
 */
public class FingonMenuItemUI extends FingonButtonUI implements PropertyChangeListener {
    private JMenuItem menuItem;

    public FingonMenuItemUI() {
	super();
    }
    
    /**
     * 
     * @see org.fingon.FingonButtonUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	super.installUI(c);
	menuItem = (JMenuItem)c;
	menuItem.getAccessibleContext().addPropertyChangeListener(this);
    }

    /**
     * 
     * @see org.fingon.FingonButtonUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	super.uninstallUI(c);
	menuItem = (JMenuItem)c;
	menuItem.getAccessibleContext().removePropertyChangeListener(this);
    }

    /**
     * 
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	return new FingonMenuItemUI();
    }
    
    /**
     * 
     * @see org.fingon.FingonButtonUI#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
	AbstractButton button = (AbstractButton)e.getSource();
	if (button.isShowing()) {
	    if (pressedSound != null) {
		try {
		    SoundPlayer player = (SoundPlayer)PlayerFactory.getPlayerByExtension("wav");
		    player.play(pressedSound);
		} catch (PlayException e1) {}
	    }
	}
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals(AccessibleContext.ACCESSIBLE_STATE_PROPERTY)) {
	    Object newValue = evt.getNewValue();
	    if (AccessibleState.FOCUSED.equals(newValue)) {
		String text = menuItem.getText();
		if (text != null && !text.equals("")) {
		    try {
			SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
		        synthesizer.stop();
		        synthesizer.play(text);
		    } catch (SynthesisException e1) {}
		}
	    }
	}
    }
}
