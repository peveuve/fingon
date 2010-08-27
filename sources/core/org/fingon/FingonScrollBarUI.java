package org.fingon;

import java.awt.Graphics;

import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ScrollBarUI;

import org.fingon.player.MIDIPlayer;
import org.fingon.player.PlayException;
import org.fingon.player.PlayerFactory;

/**
 * 
 * @author Paul-Emile
 */
public class FingonScrollBarUI extends ScrollBarUI implements ChangeListener {
    private MIDIPlayer midiPlayer;
    
    public FingonScrollBarUI() {
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
	return new FingonScrollBarUI();
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	JScrollBar scrollBar = (JScrollBar)c;
	BoundedRangeModel model = scrollBar.getModel();
	model.addChangeListener(this);
	Integer instrumentIndex = UIManager.getInt("ScrollBar.instrument");
	if (midiPlayer != null) {
	    midiPlayer.loadInstrument(instrumentIndex);
	}
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JScrollBar scrollBar = (JScrollBar)c;
	BoundedRangeModel model = scrollBar.getModel();
	model.removeChangeListener(this);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
    }

    @Override
    public void stateChanged(ChangeEvent ev) {
	BoundedRangeModel model = (BoundedRangeModel)ev.getSource();
	if (midiPlayer != null) {
	    int currentValue = model.getValue();
	    boolean adjusting = model.getValueIsAdjusting();
	    int maxValue = model.getMaximum();
	    final int value = currentValue*127/maxValue;
	    
	    if (adjusting) {
		final int velocity = 64;
	    	Runnable runnable = new Runnable() {
	    	    @Override
	    	    public void run() {
	    		midiPlayer.startNote(value, velocity);
	    		try {
	    		    Thread.sleep(200);
	    		} catch (InterruptedException e) {}
	    		midiPlayer.stopNote(value, velocity);
	    	    }
	    	};
		Thread thread = new Thread(runnable);
		thread.start();
	    } else {
		// apparently scrollbars values are changed continuously, 
		// so playing a note each time is unbearable 
	    }
	}
    }
}
