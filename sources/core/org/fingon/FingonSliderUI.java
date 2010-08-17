package org.fingon;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.SliderUI;

import org.fingon.player.MIDIPlayer;
import org.fingon.player.PlayException;
import org.fingon.player.PlayerFactory;

public class FingonSliderUI extends SliderUI implements ChangeListener {
    private MIDIPlayer midiPlayer;
    
    public FingonSliderUI() {
	try {
	    midiPlayer = (MIDIPlayer)PlayerFactory.getPlayerByExtension("mid");
	    midiPlayer.loadInstrument(MIDIPlayer.HARP);
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
	int maxValue = slider.getMaximum();
	int currentValue = slider.getValue();
	final int value = currentValue*127/maxValue;
	if (midiPlayer != null) {
	    Runnable runnable = new Runnable() {
		@Override
		public void run() {
		    midiPlayer.startNote(value);
		    try {
			Thread.sleep(400);
		    } catch (InterruptedException e) {}
		    midiPlayer.stopNote(value);
		}
	    };
	    Thread thread = new Thread(runnable);
	    thread.start();
	}
    }
}
