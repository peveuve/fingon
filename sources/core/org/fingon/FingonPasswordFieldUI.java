package org.fingon;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

import org.fingon.player.PlayException;
import org.fingon.player.Player;
import org.fingon.player.PlayerFactory;

/**
 * 
 * @author Paul-Emile
 */
public class FingonPasswordFieldUI extends BasicTextFieldUI implements KeyListener {
    /** key typed sound URL */
    private URL keyTypedSound;

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
	return new FingonPasswordFieldUI();
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	super.installUI(c);
	keyTypedSound = (URL)UIManager.get("PasswordField.keyTyped");
	JPasswordField textc = (JPasswordField)c;
	textc.addKeyListener(this);
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	super.uninstallUI(c);
	JPasswordField textc = (JPasswordField)c;
	textc.removeKeyListener(this);
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
    public void keyTyped(KeyEvent arg0) {
	if (keyTypedSound != null) {
	    try {
		Player player = PlayerFactory.getPlayerByExtension("wav");
		player.play(keyTypedSound);
	    } catch (PlayException e) {}
	}
    }
}
