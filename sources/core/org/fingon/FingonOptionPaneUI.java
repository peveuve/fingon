package org.fingon;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;

import org.fingon.accessibility.AccessibilityRenderer;
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

    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	JOptionPane optionPane = (JOptionPane)c;
	InputMap inputMap = optionPane.getInputMap();
	inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "FingonUIHelp");
	ActionMap actionMap = optionPane.getActionMap();
	actionMap.put("FingonUIHelp", AccessibilityRenderer.getInstance());
	optionPane.addAncestorListener(this);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JOptionPane optionPane = (JOptionPane)c;
	InputMap inputMap = optionPane.getInputMap();
	inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
	ActionMap actionMap = optionPane.getActionMap();
	actionMap.remove("FingonUIHelp");
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
            URL errorSound = (URL)optionPane.getClientProperty("errorSound");
            if (errorSound == null) {
    		errorSound = (URL)UIManager.get("OptionPane.errorSound");
            }
            soundUrl = errorSound;
            if (!message.endsWith("!")) {
        	message = message.concat("!");
            }
        } else if (messageType == JOptionPane.INFORMATION_MESSAGE) {
            URL informationSound = (URL)optionPane.getClientProperty("informationSound");
            if (informationSound == null) {
    		informationSound = (URL)UIManager.get("OptionPane.informationSound");
            }
            soundUrl = informationSound;
        } else if (messageType == JOptionPane.PLAIN_MESSAGE) {
            URL informationSound = (URL)optionPane.getClientProperty("informationSound");
            if (informationSound == null) {
    		informationSound = (URL)UIManager.get("OptionPane.informationSound");
            }
            soundUrl = informationSound;
        } else if (messageType == JOptionPane.QUESTION_MESSAGE) {
            URL questionSound = (URL)optionPane.getClientProperty("questionSound");
            if (questionSound == null) {
    		questionSound = (URL)UIManager.get("OptionPane.questionSound");
            }
            soundUrl = questionSound;
            if (!message.endsWith("?")) {
        	message = message.concat("?");
            }
        } else if (messageType == JOptionPane.WARNING_MESSAGE) {
            URL warningSound = (URL)optionPane.getClientProperty("warningSound");
            if (warningSound == null) {
    		warningSound = (URL)UIManager.get("OptionPane.warningSound");
            }
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
            synthesizer.load(optionPane.getLocale());
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
