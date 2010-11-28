package org.fingon;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InternalFrameUI;

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
public class FingonInternalFrameUI extends InternalFrameUI implements InternalFrameListener {
    /** the instance common to every component */
    private static FingonInternalFrameUI instance;

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	if (instance == null) {
	    instance = new FingonInternalFrameUI();
	}
	return instance;
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	JInternalFrame internalFrame = (JInternalFrame)c;
	InputMap inputMap = internalFrame.getInputMap();
	inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "FingonUIHelp");
	ActionMap actionMap = internalFrame.getActionMap();
	actionMap.put("FingonUIHelp", AccessibilityRenderer.getInstance());
	internalFrame.addInternalFrameListener(this);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JInternalFrame internalFrame = (JInternalFrame)c;
	InputMap inputMap = internalFrame.getInputMap();
	inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
	ActionMap actionMap = internalFrame.getActionMap();
	actionMap.remove("FingonUIHelp");
	internalFrame.removeInternalFrameListener(this);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
    }

    /**
     * @see javax.swing.event.InternalFrameListener#internalFrameActivated(javax.swing.event.InternalFrameEvent)
     */
    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
	JInternalFrame internalFrame = (JInternalFrame)e.getSource();
	String frameTitle = internalFrame.getTitle();
	if (frameTitle != null && !frameTitle.isEmpty()) {
	    Object[] msgArgs = {frameTitle};
	    try {
    	    	SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
    	    	synthesizer.stop();
    	    	synthesizer.load(internalFrame.getLocale());
    	    	
    	    	ResourceBundle msgBundle = ResourceBundle.getBundle("message", synthesizer.getEngineLocale());
    	    	String msgPattern = msgBundle.getString("internalFrameActivated");
    	    	MessageFormat msgFormat = new MessageFormat(msgPattern);
    	    	String msg = msgFormat.format(msgArgs);
    	    	
    	    	synthesizer.play(msg);
	    } catch (SynthesisException e1) {}
	}
    }

    /**
     * @see javax.swing.event.InternalFrameListener#internalFrameDeactivated(javax.swing.event.InternalFrameEvent)
     */
    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
    }

    /**
     * @see javax.swing.event.InternalFrameListener#internalFrameOpened(javax.swing.event.InternalFrameEvent)
     */
    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
	JInternalFrame internalFrame = (JInternalFrame)e.getSource();
	URL openedSound = (URL)internalFrame.getClientProperty("openedSound");
	if (openedSound == null) {
	    openedSound = (URL)UIManager.get("InternalFrame.openedSound");
	}
	if (openedSound != null) {
	    try {
		SoundPlayer player = (SoundPlayer)PlayerFactory.getPlayerByExtension("wav");
    	    	player.play(openedSound);
    	    } catch (PlayException ex) {}
	}
    }

    /**
     * @see javax.swing.event.InternalFrameListener#internalFrameClosed(javax.swing.event.InternalFrameEvent)
     */
    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
	JInternalFrame internalFrame = (JInternalFrame)e.getSource();
	URL closedSound = (URL)internalFrame.getClientProperty("closedSound");
	if (closedSound == null) {
	    closedSound = (URL)UIManager.get("InternalFrame.closedSound");
	}
	if (closedSound != null) {
	    try {
		SoundPlayer player = (SoundPlayer)PlayerFactory.getPlayerByExtension("wav");
    	    	player.play(closedSound);
    	    } catch (PlayException ex) {}
	}
    }

    /**
     * @see javax.swing.event.InternalFrameListener#internalFrameClosing(javax.swing.event.InternalFrameEvent)
     */
    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
    }

    /**
     * @see javax.swing.event.InternalFrameListener#internalFrameIconified(javax.swing.event.InternalFrameEvent)
     */
    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
	JInternalFrame internalFrame = (JInternalFrame)e.getSource();
	URL iconifiedSound = (URL)internalFrame.getClientProperty("iconifiedSound");
	if (iconifiedSound == null) {
	    iconifiedSound = (URL)UIManager.get("InternalFrame.iconifiedSound");
	}
	if (iconifiedSound != null) {
	    try {
		SoundPlayer player = (SoundPlayer)PlayerFactory.getPlayerByExtension("wav");
    	    	player.play(iconifiedSound);
    	    } catch (PlayException ex) {}
	}
    }

    /**
     * @see javax.swing.event.InternalFrameListener#internalFrameDeiconified(javax.swing.event.InternalFrameEvent)
     */
    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
	JInternalFrame internalFrame = (JInternalFrame)e.getSource();
	URL deiconifiedSound = (URL)internalFrame.getClientProperty("deiconifiedSound");
	if (deiconifiedSound == null) {
	    deiconifiedSound = (URL)UIManager.get("InternalFrame.deiconifiedSound");
	}
	if (deiconifiedSound != null) {
	    try {
		SoundPlayer player = (SoundPlayer)PlayerFactory.getPlayerByExtension("wav");
    	    	player.play(deiconifiedSound);
    	    } catch (PlayException ex) {}
	}
    }
}
