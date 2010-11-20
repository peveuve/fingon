package org.fingon;

import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ProgressBarUI;

import org.apache.log4j.Logger;
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
public class FingonProgressBarUI extends ProgressBarUI implements PropertyChangeListener {
    /** logger */
    private static Logger logger = Logger.getLogger(FingonProgressBarUI.class);
    /** music player */
    private SoundPlayer soundPlayer;
    /** music for indeterminate state */
    private URL indeterminateMusicUrl;
    /** sound for intermediate determinate state */
    private URL intermediateDeterminateUrl;
    /** sound for final determinate state */
    private URL finalDeterminateUrl;
    
    public FingonProgressBarUI() {
	super();
    }
    
    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	return new FingonProgressBarUI();
    }

    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	JProgressBar progress = (JProgressBar)c;
	progress.addPropertyChangeListener(this);
	indeterminateMusicUrl = (URL)UIManager.get("ProgressBarUI.backgroundMusic");
	intermediateDeterminateUrl = (URL)UIManager.get("ProgressBarUI.intermediateSound");
	finalDeterminateUrl = (URL)UIManager.get("ProgressBarUI.finalSound");
	try {
	    soundPlayer = (SoundPlayer)PlayerFactory.getPlayerByExtension("mp3");
	} catch (PlayException e) {
            logger.error(e.getMessage(), e);
	}
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JProgressBar progress = (JProgressBar)c;
	progress.removePropertyChangeListener(this);
	soundPlayer.stop();
	soundPlayer = null;
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics arg0, JComponent arg1) {
    }

    /**
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
	JProgressBar progressBar = (JProgressBar)evt.getSource();
	String prop = evt.getPropertyName();
        if ("indeterminate".equals(prop)) {
            if (soundPlayer != null) {
                if (progressBar.isIndeterminate()) {
                    if (indeterminateMusicUrl != null) {
                	// start playing music
                	try {
                	    soundPlayer.playLoop(indeterminateMusicUrl);
                	} catch (PlayException e) {}
                    }
                } else {
                    // stop playing music
                    soundPlayer.stop();
                }
            }
            //progressBar.repaint();
        } else if ("value".equals(prop)) {
            if (soundPlayer != null) {
                if (!progressBar.isIndeterminate()) {
                    int newValue = progressBar.getValue();
                    int maxValue = progressBar.getMaximum();
                    try {
                	if (newValue == maxValue) {
                	    if (finalDeterminateUrl != null) {
                		soundPlayer.play(finalDeterminateUrl);
                	    }
                	} else {
                	    if (intermediateDeterminateUrl != null) {
                		soundPlayer.play(intermediateDeterminateUrl);
                	    }
                	}
                    } catch (PlayException e) {}
                }
            }
        } else if ("string".equals(prop)) {
            if (progressBar.isStringPainted() && progressBar.isShowing()) {
        	String string = progressBar.getString();
        	try {
		    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
	    	    synthesizer.stop();
	    	    synthesizer.load(progressBar.getLocale());
		    synthesizer.play(string);
		} catch (SynthesisException e) {}
            }
        }
    }
}
