package org.fingon.player;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

import org.apache.log4j.Logger;

public class MIDIPlayer implements Player {
    /** logger */
    private static Logger logger = Logger.getLogger(MIDIPlayer.class);
    /** MIDI sequencer to play MIDI sequences from files */
    private Sequencer sequencer;
    /** MIDI synthesizer */
    private Synthesizer synthesizer;
    /** receiver */
    private Receiver receiver;
    /** current channel */
    private MidiChannel channel;
    /** current channel index */
    private int channelIndex;

    public MIDIPlayer() throws PlayException {
        try {
            // for MIDI sequences playback
	    sequencer = MidiSystem.getSequencer();
	    
	    // for MIDI synthesis on the fly
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            
            MidiChannel[] channels = synthesizer.getChannels();
            
            // choose the first available channel
            for (int i=0; i<channels.length; i++) {
        	MidiChannel aChannel = channels[i];
        	if (aChannel != null) {
        	    channel = aChannel;
        	    channelIndex = i;
        	    break;
        	}
            }
            
            receiver = synthesizer.getReceiver();
        } catch (MidiUnavailableException e) {
            //should never arrives as long as there is Sun java sound implementation or Tritonus
            logger.error("No available MIDI device: "+e.getMessage());
            throw new PlayException("No available MIDI device");
        }
    }
    
    public void loadInstrument(int instrumentIndex) {
        Instrument[] instruments = synthesizer.getAvailableInstruments();
        if (instrumentIndex < instruments.length) {
            boolean loaded = synthesizer.loadInstrument(instruments[instrumentIndex]);
            if (loaded) {
        	channel.programChange(instrumentIndex);
            }
        }
    }
    
    /**
     * Starts playing a note of the given pitch with the loaded instrument.  
     * @param brightness the pitch of the note, from 0 to 127
     * @param velocity the volume of the note, from 0 to 127
     */
    public void startNote(int brightness, int velocity) {
        ShortMessage aStartMidiMessage = new ShortMessage();
        try {
	    aStartMidiMessage.setMessage(ShortMessage.NOTE_ON, channelIndex, brightness, velocity);
	    receiver.send(aStartMidiMessage, -1);
	} catch (InvalidMidiDataException e) {
	    logger.warn("wrong midi message (brightness="+brightness+"): "+e.getMessage());
	}
    }

    /**
     * Stops playing the note of the given pitch.  
     * @param brightness the pitch of the note, from 0 to 127
     * @param velocity the volume of the note, from 0 to 127
     */
    public void stopNote(int brightness, int velocity) {
        ShortMessage aStartMidiMessage = new ShortMessage();
        try {
	    aStartMidiMessage.setMessage(ShortMessage.NOTE_OFF, channelIndex, brightness, velocity);
	    receiver.send(aStartMidiMessage, -1);
	} catch (InvalidMidiDataException e) {
	    logger.warn("wrong midi message (brightness="+brightness+"): "+e.getMessage());
	}
    }
    
    @Override
    public boolean isExtensionSupported(String extension) {
	if (extension.equalsIgnoreCase("mid")) {
	    return true;
	} else if (extension.equalsIgnoreCase("midi")) {
	    return true;
	}
	return false;
    }

    @Override
    public void play(URL anUrl) throws PlayException {
	try {
	    Sequence sequence = MidiSystem.getSequence(anUrl);
	    if (!sequencer.isOpen()) {
		sequencer.open();
	    }
	    sequencer.setSequence(sequence);
	    sequencer.start();
	} catch (InvalidMidiDataException e) {
	    logger.error("The MIDI data pointed by the URL are invalid");
	    throw new PlayException("The MIDI data pointed by the URL are invalid");
	} catch (IOException e) {
	    logger.error("IO error when reading the MIDI data pointed by the URL");
	    throw new PlayException("IO error when reading the MIDI data pointed by the URL");
	} catch (MidiUnavailableException e) {
	    logger.error("sequencer unavailable");
	    throw new PlayException("sequencer unavailable");
	}
    }

    @Override
    public void playAndWait(URL anUrl) throws PlayException {
	try {
	    Sequence sequence = MidiSystem.getSequence(anUrl);
	    if (!sequencer.isOpen()) {
		sequencer.open();
	    }
	    sequencer.setSequence(sequence);
	    sequencer.start();
	} catch (InvalidMidiDataException e) {
	    logger.error("The MIDI data pointed by the URL are invalid");
	    throw new PlayException("The MIDI data pointed by the URL are invalid");
	} catch (IOException e) {
	    logger.error("IO error when reading the MIDI data pointed by the URL");
	    throw new PlayException("IO error when reading the MIDI data pointed by the URL");
	} catch (MidiUnavailableException e) {
	    logger.error("sequencer unavailable");
	    throw new PlayException("sequencer unavailable");
	}
    }

    @Override
    public void playLoop(URL anUrl) throws PlayException {
	try {
	    Sequence sequence = MidiSystem.getSequence(anUrl);
	    if (!sequencer.isOpen()) {
		sequencer.open();
	    }
	    sequencer.setSequence(sequence);
	    sequencer.setLoopStartPoint(0);
	    sequencer.setLoopEndPoint(-1);
	    sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
	    sequencer.start();
	} catch (InvalidMidiDataException e) {
	    logger.error("The MIDI data pointed by the URL are invalid");
	    throw new PlayException("The MIDI data pointed by the URL are invalid");
	} catch (IOException e) {
	    logger.error("IO error when reading the MIDI data pointed by the URL");
	    throw new PlayException("IO error when reading the MIDI data pointed by the URL");
	} catch (MidiUnavailableException e) {
	    logger.error("sequencer unavailable");
	    throw new PlayException("sequencer unavailable");
	}
    }

    @Override
    public void setOutput(File aFile) {
    }

    @Override
    public void setOutput() {
    }

    @Override
    public void addPlayListener(PlayListener listener) {
    }

    @Override
    public void removePlayListener(PlayListener listener) {
    }

    @Override
    public int getSpeed() throws PlayException {
	return (int)(sequencer.getTempoFactor()*50/1.0f);
    }

    @Override
    public void setSpeed(int speed) {
	float factor = speed*1.0f/50;
	sequencer.setTempoFactor(factor);
    }

    @Override
    public int getVolume() throws PlayException {
	return channel.getController(7);
    }

    @Override
    public void setVolume(int volume) {
	channel.controlChange(7, volume);
    }

    @Override
    public boolean isPaused() {
	return sequencer.isRunning();
    }

    @Override
    public boolean isRunning() {
	return sequencer.isOpen();
    }

    @Override
    public void next() {
	sequencer.setTickPosition(sequencer.getTickLength());
    }

    @Override
    public void previous() {
	sequencer.setTickPosition(0);
    }

    @Override
    public void pause() {
	sequencer.stop();
    }

    @Override
    public void resume() {
	sequencer.start();
    }

    @Override
    public void stop() {
	sequencer.stop();
	sequencer.setTickPosition(0);
    }

    /**
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
	super.finalize();
	sequencer.close();
	synthesizer.close();
	receiver.close();
    }
}
