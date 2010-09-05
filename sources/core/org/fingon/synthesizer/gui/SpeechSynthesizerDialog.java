package org.fingon.synthesizer.gui;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JSlider;
import javax.swing.SpringLayout;
import javax.swing.SpringUtilities;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.fingon.player.PlayEvent;
import org.fingon.player.PlayException;
import org.fingon.player.PlayListener;
import org.fingon.synthesizer.EngineDesc;
import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisEvent;
import org.fingon.synthesizer.SynthesisException;
import org.fingon.synthesizer.SynthesisListener;
import org.fingon.synthesizer.VoiceDesc;

/**
 * A dialog to manage the speech synthesizer.
 * @author Paul-Emile
 */
public class SpeechSynthesizerDialog extends JDialog implements SynthesisListener, ChangeListener, ItemListener, PlayListener {
    /** SpeechSynthesizerDialog.java long */
    private static final long serialVersionUID = -5621132632123579337L;

    /** logger */
    private Logger logger = Logger.getLogger(SpeechSynthesizerDialog.class);

    /**  */
    private JLabel languageLabel;

    /**  */
    private JComboBox listEngine;

    /**  */
    private JLabel voiceLabel;

    /**  */
    private JComboBox listVoices;

    /**  */
    private JLabel volumeLabel;

    /**  */
    private JSlider volumeSlide;

    /**  */
    private JLabel rateLabel;

    /**  */
    private JSlider rateSlide;

    /**  */
    private JLabel pitchLabel;

    /**  */
    private JSlider pitchSlide;

    /**  */
    private JLabel pitchRangeLabel;

    /**  */
    private JSlider pitchRangeSlide;

    /**
     * 
     */
    public SpeechSynthesizerDialog() {
	super((Window) null);

	ResourceBundle label = ResourceBundle.getBundle("message");
	//Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("org/fingon/flag/USA.png"));
	//this.setIconImage(icon);
	
	this.setAlwaysOnTop(true);
	this.setTitle(label.getString("title"));
	if (UIManager.getLookAndFeel().getSupportsWindowDecorations()) {
	    this.setUndecorated(true);
	    this.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
	} else {
	    this.setUndecorated(false);
	}
	this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	
	try {
	    Class<?> awtUtilClass = Class.forName("com.sun.awt.AWTUtilities");
	    Method opacityMethod = awtUtilClass.getMethod("setWindowOpacity", Window.class, Float.TYPE);
	    opacityMethod.invoke(awtUtilClass, this, 0.9f);
	} catch (Exception e) {
	    // silently ignore this exception.
	}

	SpringLayout dialogLayout = new SpringLayout();
	this.getContentPane().setLayout(dialogLayout);

	languageLabel = new JLabel(label.getString("language"));
	this.getContentPane().add(languageLabel);

	listEngine = new JComboBox();
	listEngine.setEditable(false);
	listEngine.setMaximumSize(listEngine.getPreferredSize());
	listEngine.setName("engineList");
	EngineListCellRenderer engineRenderer = new EngineListCellRenderer();
	listEngine.setRenderer(engineRenderer);
	listEngine.addItemListener(this);
	this.getContentPane().add(listEngine);

	voiceLabel = new JLabel(label.getString("voice"));
	this.getContentPane().add(voiceLabel);

	listVoices = new JComboBox();
	listVoices.setEditable(false);
	listVoices.setMaximumSize(listVoices.getPreferredSize());
	listVoices.setName("voiceList");
	VoiceListCellRenderer voiceRenderer = new VoiceListCellRenderer();
	listVoices.setRenderer(voiceRenderer);
	listVoices.addItemListener(this);
	this.getContentPane().add(listVoices);

	volumeLabel = new JLabel(label.getString("volume"));
	this.getContentPane().add(volumeLabel);

	volumeSlide = new JSlider();
	volumeSlide.setName("volumeSlide");
	volumeSlide.setMinimum(0);
	volumeSlide.setMaximum(100);
	volumeSlide.setOrientation(JSlider.HORIZONTAL);
	volumeSlide.setMinorTickSpacing(10);
	volumeSlide.setSnapToTicks(true);
	volumeSlide.setPaintTicks(true);
	volumeSlide.addChangeListener(this);
	this.getContentPane().add(volumeSlide);

	rateLabel = new JLabel(label.getString("rate"));
	this.getContentPane().add(rateLabel);

	rateSlide = new JSlider();
	rateSlide.setName("rateSlide");
	rateSlide.setMinimum(0);
	rateSlide.setMaximum(100);
	rateSlide.setOrientation(JSlider.HORIZONTAL);
	rateSlide.setMinorTickSpacing(10);
	rateSlide.setSnapToTicks(true);
	rateSlide.setPaintTicks(true);
	rateSlide.addChangeListener(this);
	this.getContentPane().add(rateSlide);

	pitchLabel = new JLabel(label.getString("pitch"));
	this.getContentPane().add(pitchLabel);

	pitchSlide = new JSlider();
	pitchSlide.setName("pitchSlide");
	pitchSlide.setMinimum(0);
	pitchSlide.setMaximum(100);
	pitchSlide.setOrientation(JSlider.HORIZONTAL);
	pitchSlide.setMinorTickSpacing(10);
	pitchSlide.setSnapToTicks(true);
	pitchSlide.setPaintTicks(true);
	pitchSlide.addChangeListener(this);
	this.getContentPane().add(pitchSlide);

	pitchRangeLabel = new JLabel(label.getString("pitchRate"));
	this.getContentPane().add(pitchRangeLabel);

	pitchRangeSlide = new JSlider();
	pitchRangeSlide.setName("pitchRangeSlide");
	pitchRangeSlide.setMinimum(0);
	pitchRangeSlide.setMaximum(100);
	pitchRangeSlide.setOrientation(JSlider.HORIZONTAL);
	pitchRangeSlide.setMinorTickSpacing(10);
	pitchRangeSlide.setSnapToTicks(true);
	pitchRangeSlide.setPaintTicks(true);
	pitchRangeSlide.addChangeListener(this);
	this.getContentPane().add(pitchRangeSlide);

	SpringUtilities.makeCompactGrid(this.getContentPane(), // parent
		6, 2, // rows, cols
		3, 3, // initX, initY
		5, 5); // xPad, yPad

	try {
	    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
	    setControlValues(synthesizer);
	} catch (SynthesisException ex) {
	    setEnabled(false);
	}
	this.pack();

	Rectangle screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
	int x = screenBounds.width - this.getWidth() - 100;
	int y = 75;
	this.setLocation(x, y);
    }

    /**
     * initialize the panel with speaker values
     * @param aSpeechSynthesizer
     */
    public void setControlValues(SpeechSynthesizer aSpeechSynthesizer) {
	setEnabled(true);
	aSpeechSynthesizer.addSpeechListener(this);
	aSpeechSynthesizer.addPlayListener(this);
	
	List<EngineDesc> engines = aSpeechSynthesizer.listAvailableEngines();
	displayEngines(engines);
	EngineDesc engineDesc = aSpeechSynthesizer.getEngineDesc();
	selectEngine(engineDesc);

	float pitch = aSpeechSynthesizer.getPitch();
	pitchSlide.setValue((int) pitch);
	float pitchRange = aSpeechSynthesizer.getPitchRange();
	pitchRangeSlide.setValue((int) pitchRange);

	List<VoiceDesc> voices = aSpeechSynthesizer.listAvailableVoices();
	displayVoices(voices);
	VoiceDesc voiceDesc = aSpeechSynthesizer.getVoiceDesc();
	selectVoice(voiceDesc);
	
	try {
	    int volume = aSpeechSynthesizer.getVolume();
	    volumeSlide.setValue(volume);
	} catch (PlayException e) {}
	try {
	    int speed = aSpeechSynthesizer.getSpeed();
	    rateSlide.setValue(speed);
	} catch (PlayException e) {}
    }

    /**
     * @see javax.swing.JComponent#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
	listEngine.setEnabled(enabled);
	listVoices.setEnabled(enabled);
	pitchSlide.setEnabled(enabled);
	pitchRangeSlide.setEnabled(enabled);
	volumeSlide.setEnabled(enabled);
	rateSlide.setEnabled(enabled);
    }

    /**
     * @param voices
     */
    public void displayVoices(List<VoiceDesc> voices) {
	ItemListener[] listener = listVoices.getItemListeners();
	// remove all the listeners not to fire item changed events
	for (int i = 0; i < listener.length; i++) {
	    listVoices.removeItemListener(listener[i]);
	}
	listVoices.removeAllItems();
	for (int i = 0; i < voices.size(); i++) {
	    VoiceDesc voice = voices.get(i);
	    listVoices.addItem(voice);
	}
	// add the listeners previously removed
	for (int i = 0; i < listener.length; i++) {
	    listVoices.addItemListener(listener[i]);
	}
    }

    /**
     * @param engines
     */
    public void displayEngines(List<EngineDesc> engines) {
	ItemListener[] listener = listEngine.getItemListeners();
	// remove all the listeners not to fire item changed events
	for (int i = 0; i < listener.length; i++) {
	    listEngine.removeItemListener(listener[i]);
	}
	listEngine.removeAllItems();
	for (int i = 0; i < engines.size(); i++) {
	    EngineDesc engine = engines.get(i);
	    listEngine.addItem(engine);
	}
	// add the listeners previously removed
	for (int i = 0; i < listener.length; i++) {
	    listEngine.addItemListener(listener[i]);
	}
    }

    /**
     * @see org.tramper.synthesizer.SynthesisListener#pitchChanged(org.tramper.synthesizer.SynthesisEvent)
     */
    public void pitchChanged(SynthesisEvent event) {
	float pitch = event.getPitch();
	pitchSlide.setValue((int) pitch);
    }

    /**
     * @see org.tramper.synthesizer.SynthesisListener#pitchRangeChanged(org.tramper.synthesizer.SynthesisEvent)
     */
    public void pitchRangeChanged(SynthesisEvent event) {
	float pitchRange = event.getPitchRange();
	pitchRangeSlide.setValue((int) pitchRange);
    }

    /**
     * @see org.tramper.synthesizer.SynthesisListener#voicesListChanged(org.tramper.synthesizer.SynthesisEvent)
     */
    public void voicesListChanged(SynthesisEvent event) {
	List<VoiceDesc> voicesList = event.getVoices();
	this.displayVoices(voicesList);
    }

    /**
     * @see org.tramper.synthesizer.SynthesisListener#engineChanged(org.tramper.synthesizer.SynthesisEvent)
     */
    public void engineChanged(SynthesisEvent event) {
	EngineDesc engine = event.getEngine();
	selectEngine(engine);
    }

    /**
     * @see org.tramper.synthesizer.SynthesisListener#voiceChanged(org.tramper.synthesizer.SynthesisEvent)
     */
    public void voiceChanged(SynthesisEvent event) {
	VoiceDesc voice = event.getVoice();
	selectVoice(voice);
    }

    /**
     * Select an engine in the list of engines
     * @param voice
     */
    public void selectEngine(EngineDesc engine) {
	String engineName = engine.getEngineName();
	Locale engineLocale = engine.getLocale();
	// if the desired engine is already selected, do nothing
	EngineDesc selectedEngine = (EngineDesc) listEngine.getSelectedItem();
	String selectedName = selectedEngine.getEngineName();
	Locale selectedLocale = selectedEngine.getLocale();
	if (selectedName.equalsIgnoreCase(engineName) && selectedLocale.equals(engineLocale)) {
	    return;
	}
	int itemCount = listEngine.getItemCount();
	for (int i = 0; i < itemCount; i++) {
	    EngineDesc anEngine = (EngineDesc) listEngine.getItemAt(i);
	    String aName = anEngine.getEngineName();
	    Locale aLocale = anEngine.getLocale();
	    if (aName.equalsIgnoreCase(engineName) && aLocale.equals(engineLocale)) {
		listEngine.setSelectedIndex(i);
	    }
	}
    }

    /**
     * Select a voice in the list of voices
     * @param voice
     */
    public void selectVoice(VoiceDesc voice) {
	// if the desired engine is already selected, do nothing
	VoiceDesc selectedVoice = (VoiceDesc) listVoices.getSelectedItem();
	if (selectedVoice.getName().equalsIgnoreCase(voice.getName())) {
	    return;
	}
	int itemCount = listVoices.getItemCount();
	for (int i = 0; i < itemCount; i++) {
	    VoiceDesc aVoice = (VoiceDesc) listVoices.getItemAt(i);
	    if (aVoice.getName().equalsIgnoreCase(voice.getName())) {
		listVoices.setSelectedIndex(i);
	    }
	}
    }

    /**
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent event) {
	try {
	    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
	    Component source = (Component) event.getSource();
	    String name = source.getName();
	    if (name.equals("pitchSlide")) {
		JSlider slider = (JSlider) source;
		if (!slider.getValueIsAdjusting()) {
		    int value = slider.getValue();
		    if (synthesizer != null) {
			synthesizer.setPitch(value);
		    }
		}
	    } else if (name.equals("pitchRangeSlide")) {
		JSlider slider = (JSlider) source;
		if (!slider.getValueIsAdjusting()) {
		    int value = slider.getValue();
		    if (synthesizer != null) {
			synthesizer.setPitchRange(value);
		    }
		}
	    } else if (name.equals("volumeSlide")) {
		JSlider slider = (JSlider) source;
		if (!slider.getValueIsAdjusting()) {
		    int value = slider.getValue();
		    if (synthesizer != null) {
			synthesizer.setVolume(value);
		    }
		}
	    } else if (name.equals("rateSlide")) {
		JSlider slider = (JSlider) source;
		if (!slider.getValueIsAdjusting()) {
		    int value = slider.getValue();
		    if (synthesizer != null) {
			synthesizer.setSpeed(value);
		    }
		}
	    }
	} catch (SynthesisException e) {}
    }

    /**
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent event) {
	try {
	    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
	    Component source = (Component) event.getSource();
	    String name = source.getName();
	    if (name.equals("engineList")) {
		EngineDesc selected = (EngineDesc) event.getItem();
		int stateChange = event.getStateChange();
		if (stateChange == ItemEvent.SELECTED) {
		    try {
			if (synthesizer != null) {
			    synthesizer.loadEngine(selected.getEngineName(), selected.getModeName(), selected.getLocale());
			}
		    } catch (SynthesisException e) {
			logger.error("engine " + selected.getEngineName() + " loading failed", e);
		    }
		} else if (stateChange == ItemEvent.DESELECTED) {
		    logger.debug("deselect " + selected.getEngineName() + " from " + name);
		}
	    } else if (name.equals("voiceList")) {
		VoiceDesc selected = (VoiceDesc) event.getItem();
		int stateChange = event.getStateChange();
		if (stateChange == ItemEvent.SELECTED) {
		    if (synthesizer != null) {
			synthesizer.loadVoice(selected);
		    }
		} else if (stateChange == ItemEvent.DESELECTED) {
		    logger.debug("deselect " + selected.getName() + " from " + name);
		}
	    }
	} catch (SynthesisException e) {
	}
    }

    @Override
    public void nextRead(PlayEvent event) {
    }

    @Override
    public void previousRead(PlayEvent event) {
    }

    @Override
    public void readingEnded(PlayEvent event) {
    }

    @Override
    public void readingPaused(PlayEvent event) {
    }

    @Override
    public void readingResumed(PlayEvent event) {
    }

    @Override
    public void readingStarted(PlayEvent event) {
    }

    @Override
    public void readingStopped(PlayEvent event) {
    }

    @Override
    public void sampleRateChanged(PlayEvent event) {
	final long newValue = event.getNewValue();
	Runnable r = new Runnable() {
            public void run() {
        	int currentValue = rateSlide.getValue();
        	if (currentValue != newValue) {
        	    rateSlide.setValue((int)newValue);
        	}
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }

    @Override
    public void volumeChanged(PlayEvent event) {
	final long newValue = event.getNewValue();
	Runnable r = new Runnable() {
            public void run() {
        	int currentValue = volumeSlide.getValue();
        	if (currentValue != newValue) {
        	    volumeSlide.setValue((int)newValue);
        	}
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }
}
