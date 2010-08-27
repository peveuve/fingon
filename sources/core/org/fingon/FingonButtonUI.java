package org.fingon;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ButtonUI;
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
public class FingonButtonUI extends ButtonUI implements ActionListener, ItemListener {
    /** the instance common to every component */
    private static FingonButtonUI instance;
    /** selected sound URL */
    protected URL selectedSound;
    /** pressed sound URL */
    protected URL pressedSound;
    /** unselected sound URL */
    protected URL unselectedSound;

    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	AbstractButton button = (AbstractButton)c;
	button.addActionListener(this);
	button.addItemListener(this);
	pressedSound = (URL)UIManager.get("Button.pressedSound");
	selectedSound = (URL)UIManager.get("Button.selectedSound");
	unselectedSound = (URL)UIManager.get("Button.unselectedSound");
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	AbstractButton button = (AbstractButton)c;
	button.removeActionListener(this);
	button.removeItemListener(this);
    }

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	if (instance == null) {
	    instance = new FingonButtonUI();
	}
	return instance;
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
    }

    public void actionPerformed(ActionEvent e) {
	AbstractButton button = (AbstractButton)e.getSource();
	if (button.isShowing()) {
	    String text = button.getText();
	    if (text != null && !text.equals("")) {
		try {
		    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
		    synthesizer.stop();
		    synthesizer.play(text);
		} catch (SynthesisException e1) {}
	    } else if (pressedSound != null) {
		try {
		    SoundPlayer player = (SoundPlayer)PlayerFactory.getPlayerByExtension("wav");
		    player.play(pressedSound);
		} catch (PlayException e1) {}
	    }
	}
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
	AbstractButton button = (AbstractButton)e.getSource();
	if (button.isShowing()) {
	    try {
		SoundPlayer player = (SoundPlayer)PlayerFactory.getPlayerByExtension("wav");
		int state = e.getStateChange();
		if (state == ItemEvent.DESELECTED) {
		    if (unselectedSound != null) {
			player.play(unselectedSound);
		    }
		} else if (state == ItemEvent.SELECTED) {
		    if (selectedSound != null) {
			player.play(selectedSound);
		    }
		}
	    } catch (PlayException e1) {}
	}
    }
}
