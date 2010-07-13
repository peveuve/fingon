package org.fingon;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

/**
 * 
 * @author Paul-Emile
 */
public class FingonButtonUI extends ButtonUI implements ActionListener, ItemListener, MouseListener {
    /** the instance common to every component */
    private static FingonButtonUI instance;
    /** selected sound URL */
    private URL selectedSound;
    /** pressed sound URL */
    private URL pressedSound;
    /** unselected sound URL */
    private URL unselectedSound;

    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	AbstractButton button = (AbstractButton)c;
	button.addActionListener(this);
	button.addItemListener(this);
	button.addMouseListener(this);
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
	button.removeMouseListener(this);
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
        try {
            SoundPlayer player = (SoundPlayer)PlayerFactory.getPlayerByExtension("wav");
            player.play(pressedSound);
        } catch (PlayException e1) {}
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        try {
            SoundPlayer player = (SoundPlayer)PlayerFactory.getPlayerByExtension("wav");
            int state = e.getStateChange();
            if (state == ItemEvent.DESELECTED) {
        	player.play(unselectedSound);
            } else if (state == ItemEvent.SELECTED) {
        	player.play(selectedSound);
            }
        } catch (PlayException e1) {}
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
	AbstractButton componentGainingFocus = (AbstractButton)e.getSource();
	String text = componentGainingFocus.getText();
	if (text != null && !text.equals("")) {
            SpeechSynthesizer synthesizer = PlayerFactory.getSpeechSynthesizer();
            synthesizer.stop();
            synthesizer.play(text);
	}
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
