package org.fingon;

import java.awt.Graphics;
import java.util.Dictionary;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.SliderUI;

import org.fingon.accessibility.AccessibilityRenderer;
import org.fingon.player.MIDIPlayer;
import org.fingon.player.PlayException;
import org.fingon.player.PlayerFactory;
import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

public class FingonSliderUI extends SliderUI implements ChangeListener {
    private MIDIPlayer midiPlayer;
    
    public FingonSliderUI() {
	try {
	    midiPlayer = (MIDIPlayer)PlayerFactory.getPlayerByExtension("mid");
	} catch (PlayException e) {
	    // no fallback, test the midiPlayer is null
	}
    }

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	return new FingonSliderUI();
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	JSlider slider = (JSlider)c;
	InputMap inputMap = slider.getInputMap();
	String helpKey = UIManager.getString("Fingon.helpKey");
	inputMap.put(KeyStroke.getKeyStroke(helpKey), "FingonUIHelp");
	ActionMap actionMap = slider.getActionMap();
	actionMap.put("FingonUIHelp", AccessibilityRenderer.getInstance());
	slider.addChangeListener(this);
	slider.addFocusListener(AccessibilityRenderer.getInstance());
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JSlider slider = (JSlider)c;
	InputMap inputMap = slider.getInputMap();
	String helpKey = UIManager.getString("Fingon.helpKey");
	inputMap.remove(KeyStroke.getKeyStroke(helpKey));
	ActionMap actionMap = slider.getActionMap();
	actionMap.remove("FingonUIHelp");
	slider.removeChangeListener(this);
	slider.removeFocusListener(AccessibilityRenderer.getInstance());
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void stateChanged(ChangeEvent event) {
	JSlider slider = (JSlider)event.getSource();
	if (slider.isShowing()) {
	    int currentValue = slider.getValue();
	    if (slider.getPaintLabels()) {
		Dictionary<Integer, JComponent> labelTable = slider.getLabelTable();
		JComponent label = labelTable.get(currentValue);
		if (label != null && (label instanceof JLabel) ) {
		    try {
    	    	        SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
    	    	        synthesizer.stop();
    	    	        synthesizer.load(slider.getLocale());
    	    	        synthesizer.play(((JLabel)label).getText());
    	    	    } catch (SynthesisException ex) {}
		}
	    }
	    if (slider.getPaintTicks()) {
		int volume = 0;
		int minorTickSpacing = slider.getMinorTickSpacing();
		int majorTickSpacing = slider.getMajorTickSpacing();
		int minValue = slider.getMinimum();
		if ( (majorTickSpacing != 0) && (currentValue - minValue)%majorTickSpacing == 0) {
		    volume = 127;
		} else if ( (minorTickSpacing != 0) && (currentValue - minValue)%minorTickSpacing == 0) {
		    volume = 64;
		}
		int maxValue = slider.getMaximum();
		final int value = currentValue*127/maxValue;
		final int velocity = volume;
		Integer instrumentIndex = (Integer)slider.getClientProperty("instrument");
		if (instrumentIndex == null) {
		    instrumentIndex = UIManager.getInt("Slider.instrument");
		}
		if (midiPlayer != null && instrumentIndex != null) {
		    midiPlayer.loadInstrument(instrumentIndex);
		}
		if (midiPlayer != null) {
		    Runnable runnable = new Runnable() {
			@Override
			public void run() {
			    midiPlayer.startNote(value, velocity);
			    try {
    		    	    	Thread.sleep(400);
			    } catch (InterruptedException e) {}
			    midiPlayer.stopNote(value, velocity);
			}
		    };
		    Thread thread = new Thread(runnable, "slider tick MIDI note");
		    thread.start();
		}
	    }
	}
    }
}
