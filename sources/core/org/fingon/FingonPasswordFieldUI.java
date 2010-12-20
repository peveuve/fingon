package org.fingon;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

import org.fingon.accessibility.AccessibilityRenderer;
import org.fingon.player.PlayException;
import org.fingon.player.Player;
import org.fingon.player.PlayerFactory;

/**
 * 
 * @author Paul-Emile
 */
public class FingonPasswordFieldUI extends BasicTextFieldUI implements KeyListener {
    private static FingonPasswordFieldUI instance;
    
    /**
     * 
     */
    public FingonPasswordFieldUI() {
	super();
    }

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	if (instance == null) {
	    instance = new FingonPasswordFieldUI();
	}
	return instance;
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	super.installUI(c);
	JPasswordField textc = (JPasswordField)c;
	InputMap inputMap = textc.getInputMap();
	String helpKey = UIManager.getString("Fingon.helpKey");
	inputMap.put(KeyStroke.getKeyStroke(helpKey), "FingonUIHelp");
	ActionMap actionMap = textc.getActionMap();
	actionMap.put("FingonUIHelp", AccessibilityRenderer.getInstance());
	textc.addKeyListener(this);
	textc.addFocusListener(AccessibilityRenderer.getInstance());
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	super.uninstallUI(c);
	JPasswordField textc = (JPasswordField)c;
	InputMap inputMap = textc.getInputMap();
	String helpKey = UIManager.getString("Fingon.helpKey");
	inputMap.remove(KeyStroke.getKeyStroke(helpKey));
	ActionMap actionMap = textc.getActionMap();
	actionMap.remove("FingonUIHelp");
	textc.removeKeyListener(this);
	textc.removeFocusListener(AccessibilityRenderer.getInstance());
    }

    /**
     * Do not clear the component view if it is opaque.
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
	
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent evt) {
	JPasswordField passwordField = (JPasswordField)evt.getSource();
	URL keyTypedSound = (URL)passwordField.getClientProperty("keyTypedSound");
	if (keyTypedSound == null) {
	    keyTypedSound = (URL)UIManager.get("PasswordField.keyTypedSound");
	}
	if (keyTypedSound != null) {
	    try {
		Player player = PlayerFactory.getPlayerByExtension("wav");
		player.play(keyTypedSound);
	    } catch (PlayException e) {}
	}
    }
}
