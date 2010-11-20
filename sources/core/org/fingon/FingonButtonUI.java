package org.fingon;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.accessibility.AccessibleContext;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.ComponentUI;

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
public class FingonButtonUI extends ButtonUI implements ActionListener, ItemListener, FocusListener {
    /** the instance common to every component */
    private static FingonButtonUI instance;

    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	AbstractButton button = (AbstractButton)c;
	InputMap inputMap = button.getInputMap();
	inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "FingonUIHelp");
	ActionMap actionMap = button.getActionMap();
	actionMap.put("FingonUIHelp", AccessibilityRenderer.getInstance());
	button.addActionListener(this);
	button.addItemListener(this);
	button.addFocusListener(this);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	AbstractButton button = (AbstractButton)c;
	InputMap inputMap = button.getInputMap();
	inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
	ActionMap actionMap = button.getActionMap();
	actionMap.remove("FingonUIHelp");
	button.removeActionListener(this);
	button.removeItemListener(this);
	button.removeFocusListener(this);
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
		    synthesizer.load(button.getLocale());
		    synthesizer.play(text);
		} catch (SynthesisException e1) {}
	    } else {
		URL pressedSound = (URL)button.getClientProperty("pressedSound");
		if (pressedSound == null) {
		    pressedSound = (URL)UIManager.get("Button.pressedSound");
		}
		if (pressedSound != null) {
		    try {
			SoundPlayer player = (SoundPlayer)PlayerFactory.getPlayerByExtension("wav");
			player.play(pressedSound);
		    } catch (PlayException e1) {}
		}
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
		    URL unselectedSound = (URL)button.getClientProperty("unselectedSound");
		    if (unselectedSound == null) {
			unselectedSound = (URL)UIManager.get("Button.unselectedSound");
		    }
		    if (unselectedSound != null) {
			player.play(unselectedSound);
		    }
		} else if (state == ItemEvent.SELECTED) {
		    URL selectedSound = (URL)button.getClientProperty("selectedSound");
		    if (selectedSound == null) {
			selectedSound = (URL)UIManager.get("Button.selectedSound");
		    }
		    if (selectedSound != null) {
			player.play(selectedSound);
		    }
		}
	    } catch (PlayException e1) {}
	}
    }

    @Override
    public void focusGained(FocusEvent e) {
	AccessibleContext accessCtxt = e.getComponent().getAccessibleContext();
	AccessibilityRenderer.getInstance().renderSummary(accessCtxt);
    }

    @Override
    public void focusLost(FocusEvent e) {
    }
}
