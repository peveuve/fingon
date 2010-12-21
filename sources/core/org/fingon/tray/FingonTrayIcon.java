package org.fingon.tray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.ResourceBundle;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import org.apache.log4j.Logger;
import org.fingon.IconFactory;
import org.fingon.synthesizer.EngineDesc;
import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisEvent;
import org.fingon.synthesizer.SynthesisException;
import org.fingon.synthesizer.SynthesisListener;
import org.fingon.synthesizer.VoiceDesc;
import org.fingon.synthesizer.gui.SpeechSynthesizerDialog;

/**
 * Display an icon in the system tray (Windows, Mac OS X and Linux).
 * @author Paul-Emile
 */
public class FingonTrayIcon implements ActionListener, SynthesisListener {
    /** logger */
    private Logger logger = Logger.getLogger(FingonTrayIcon.class);
    /** tray icon */
    private TrayIcon trayIcon;
    /** Speech synthesizer settings dialog */
    private JDialog settingDialog;
    
    /**
     * 
     */
    public FingonTrayIcon() {
	super();
	if (!SystemTray.isSupported()) {
	    return;
	}

	Image trayImage = null;
	boolean synthesizerAvailable = true;
	try {
	    SpeechSynthesizer speechSynthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
	    speechSynthesizer.addSpeechListener(this);
	    
	    EngineDesc engine = speechSynthesizer.getEngineDesc();
	    VoiceDesc voice = speechSynthesizer.getVoiceDesc();
	    trayImage = drawTrayImage(engine, voice);
	} catch (SynthesisException ex) {
	    logger.error("no speech synthesizer");
	    synthesizerAvailable = false;
	    trayImage = Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("org/fingon/person/delete_user.png"));
	}
	
	ResourceBundle label = ResourceBundle.getBundle("message");
        
        trayIcon = new TrayIcon(trayImage, label.getString("title"));
        trayIcon.addActionListener(this);
        PopupMenu popup = new PopupMenu(label.getString("title"));
        MenuItem speechSynthMenuItem = new MenuItem(label.getString("speechSynthSetting"));
        speechSynthMenuItem.addActionListener(this);
        popup.add(speechSynthMenuItem);
        trayIcon.setPopupMenu(popup);
        SystemTray tray = SystemTray.getSystemTray();
        try {
	    tray.add(trayIcon);
	    if (synthesizerAvailable) {
		String readyMsg = label.getString("ready");
		this.displayInfo(readyMsg);
	    } else {
		String readyMsg = label.getString("synthesizerError");
		this.displayError(readyMsg);
	    }
	} catch (AWTException ex) {
	    logger.error("the system tray is unavailable");
	}
    }
    
    private Image drawTrayImage(EngineDesc engine, VoiceDesc voice) {
        SystemTray tray = SystemTray.getSystemTray();
        Dimension trayIconSize = tray.getTrayIconSize();
        
	Locale locale = engine.getLocale();
	ImageIcon flagIcon = IconFactory.getFlagIconByLocale(locale);
	Image flagImage = flagIcon.getImage();
	
	ImageIcon personIcon = IconFactory.getIconByVoice(voice);
	Image personImage = personIcon.getImage();
	
	Image trayImage = new BufferedImage(trayIconSize.width, trayIconSize.height, BufferedImage.TYPE_INT_ARGB);
	Graphics trayGraphics = trayImage.getGraphics();
	// center the icons into the tray on the x axis
	int iconx = (trayIconSize.width - flagIcon.getIconWidth())/2;
	// draw the flag icon to the top of the tray
	trayGraphics.drawImage(flagImage, iconx, 0, null);
	// draw the person icon to the bottom of the tray
	int iconPersony = trayIconSize.height - personIcon.getIconHeight();
	trayGraphics.drawImage(personImage, iconx, iconPersony, null);
	trayGraphics.dispose();
	
	return trayImage;
    }
    
    /**
     * Displays an information message in a bubble stuck to the tray icon.
     * @param message
     */
    public void displayInfo(String message) {
	ResourceBundle label = ResourceBundle.getBundle("message");
        trayIcon.displayMessage(label.getString("title"), message, TrayIcon.MessageType.INFO);
    }

    /**
     * Displays an error message in a bubble stuck to the tray icon.
     * @param message
     */
    public void displayError(String message) {
	ResourceBundle label = ResourceBundle.getBundle("message");
        trayIcon.displayMessage(label.getString("title"), message, TrayIcon.MessageType.ERROR);
    }
    
    /**
     * When clicking the tray icon, open a small window to manage the speech synthesizer.
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
	if (settingDialog == null) {
	    settingDialog = new SpeechSynthesizerDialog();
	}
	settingDialog.setVisible(true);
    }

    /**
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
	super.finalize();
	if (settingDialog != null) {
	    settingDialog.dispose();
	}
	SystemTray.getSystemTray().remove(trayIcon);
    }

    @Override
    public void engineChanged(SynthesisEvent event) {
	EngineDesc engine = event.getEngine();
	VoiceDesc voice = event.getVoice();
	Image trayImage = drawTrayImage(engine, voice);
	trayIcon.setImage(trayImage);
    }

    @Override
    public void voiceChanged(SynthesisEvent event) {
	EngineDesc engine = event.getEngine();
	VoiceDesc voice = event.getVoice();
	Image trayImage = drawTrayImage(engine, voice);
	trayIcon.setImage(trayImage);
    }

    @Override
    public void pitchChanged(SynthesisEvent event) {
    }

    @Override
    public void pitchRangeChanged(SynthesisEvent event) {
    }

    @Override
    public void voicesListChanged(SynthesisEvent event) {
    }
}
