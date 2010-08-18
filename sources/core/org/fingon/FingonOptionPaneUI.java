package org.fingon;

import java.awt.Graphics;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;

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
public class FingonOptionPaneUI extends BasicOptionPaneUI implements AncestorListener {
    /** the instance common to every component */
    private static FingonOptionPaneUI instance;
    /** information sound URL */
    private URL informationSound;
    /** question sound URL */
    private URL questionSound;
    /** warning sound URL */
    private URL warningSound;
    /** error sound URL */
    private URL errorSound;

    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	JOptionPane optionPane = (JOptionPane)c;
	optionPane.addAncestorListener(this);
	this.installDefaults();
    }

    /**
     * @see javax.swing.plaf.basic.BasicOptionPaneUI#installDefaults()
     */
    @Override
    protected void installDefaults() {
	informationSound = (URL)UIManager.get("OptionPane.informationSound");
	questionSound = (URL)UIManager.get("OptionPane.questionSound");
	warningSound = (URL)UIManager.get("OptionPane.warningSound");
	errorSound = (URL)UIManager.get("OptionPane.errorSound");
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JOptionPane optionPane = (JOptionPane)c;
	optionPane.removeAncestorListener(this);
    }

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	if (instance == null) {
	    instance = new FingonOptionPaneUI();
	}
	return instance;
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
    }

    public void ancestorAdded(AncestorEvent event) {
	JOptionPane optionPane = (JOptionPane)event.getComponent();

        int messageType = optionPane.getMessageType();
	String message = optionPane.getMessage().toString();
	URL soundUrl = null;
        if (messageType == JOptionPane.ERROR_MESSAGE) {
            soundUrl = errorSound;
            if (!message.endsWith("!")) {
        	message = message.concat("!");
            }
        } else if (messageType == JOptionPane.INFORMATION_MESSAGE) {
            soundUrl = informationSound;
        } else if (messageType == JOptionPane.PLAIN_MESSAGE) {
            soundUrl = informationSound;
        } else if (messageType == JOptionPane.QUESTION_MESSAGE) {
            soundUrl = questionSound;
            if (!message.endsWith("?")) {
        	message = message.concat("?");
            }
        } else if (messageType == JOptionPane.WARNING_MESSAGE) {
            soundUrl = warningSound;
            if (!message.endsWith("!")) {
        	message = message.concat("!");
            }
        }
        if (soundUrl != null) {
            try {
        	SoundPlayer player = (SoundPlayer)PlayerFactory.getPlayerByExtension("wav");
        	player.playAndWait(soundUrl);
            } catch (PlayException e1) {}
        }
        
        try {
            SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
            synthesizer.stop();
            synthesizer.play(message);
	} catch (SynthesisException e1) {}
    }

    public void ancestorMoved(AncestorEvent event) {
    }

    public void ancestorRemoved(AncestorEvent event) {
	try {
	    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
	    synthesizer.stop();
	} catch (SynthesisException e1) {}
    }
}
