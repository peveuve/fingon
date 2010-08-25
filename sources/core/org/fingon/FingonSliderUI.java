package org.fingon;

import java.awt.Graphics;
import java.util.Dictionary;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.SliderUI;

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
	slider.addChangeListener(this);
	Integer instrumentIndex = UIManager.getInt("Slider.instrument");
	if (midiPlayer != null) {
	    midiPlayer.loadInstrument(instrumentIndex);
	}
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JSlider slider = (JSlider)c;
	slider.removeChangeListener(this);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
    }

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
		    Thread thread = new Thread(runnable);
		    thread.start();
		}
	    }
	}
    }
}
