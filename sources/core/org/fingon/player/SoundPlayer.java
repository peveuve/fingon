package org.fingon.player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFileFormat.Type;

import org.apache.log4j.Logger;
import org.fingon.player.PlayEvent;
import org.fingon.player.PlayException;
import org.fingon.player.PlayListener;
import org.fingon.player.Player;


/**
 * Music player (au, wav, aif, mp3 and ogg files) using Java sound API (full Java API).
 * Uses the Tritonus implementation to be able to play MP3 (JLayer) and Ogg Vorbis (JCraft).
 * Not thread-safe, use one instance for each play.
 * @author Paul-Emile
 */
public class SoundPlayer implements Player, Runnable {
    /** logger */
    private Logger logger = Logger.getLogger(SoundPlayer.class);
    /** line used for the current play */
    private DataLine currentLine;
    /** decoded audio stream */
    private InputStream decodedStream;
    /** decoded audio format */
    private AudioFormat decodedFormat;
    /** flag to stop the current play */
    private boolean stopped = true;
    /** flag to pause the current play */
    private boolean paused = false;
    /** flag to play in loop */
    private boolean loop = false;
    /** play listeners list */
    private List<PlayListener> listener;
    /** decoded audio data buffer size */
    private static final int BUFFER_SIZE = 1024;
    
    /**
     * Instantiates the first available mixer
     */
    public SoundPlayer() throws PlayException {
        super();
        listener = new ArrayList<PlayListener>();
    }

    /**
     * Plays a sound from an URL.
     * @param audioUrl 
     * @see org.fingon.player.Player#play(java.net.URL)
     */
    public void play(URL audioUrl) throws PlayException {
        loop = false;
	AudioInputStream encodedStream = null;
        try {
            encodedStream = AudioSystem.getAudioInputStream(audioUrl);
        } catch (UnsupportedAudioFileException e) {
            logger.error("Audio url format unsupported", e);
            throw new PlayException();
        } catch (IOException e) {
            logger.error("url "+audioUrl+" unavailable", e);
            throw new PlayException();
        }
        AudioFormat encodedFormat = encodedStream.getFormat();
        decodedFormat = decodeFormat(encodedFormat);
        try {
            decodedStream = AudioSystem.getAudioInputStream(decodedFormat, encodedStream);
            decodedStream = new BufferedInputStream(decodedStream);
        } catch (IllegalArgumentException e) {
            logger.error("audio conversion not supported.");
            logger.info("supported conversions: ");
            for (AudioFormat.Encoding encoding : AudioSystem.getTargetEncodings(encodedFormat)) {
        	logger.info("target encoding="+encoding);
            }
            throw new PlayException("audio conversion not supported.");
        }
        
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Plays a sound from an URL and wait for the end.
     * @param audioUrl  
     * @see org.fingon.player.Player#playAndWait(java.net.URL)
     */
    public void playAndWait(URL audioUrl) throws PlayException {
        loop = false;

	AudioInputStream encodedStream = null;
        try {
            encodedStream = AudioSystem.getAudioInputStream(audioUrl);
        }
        catch (UnsupportedAudioFileException e) {
            logger.error("Audio url format unsupported", e);
            throw new PlayException();
        }
        catch (IOException e) {
            logger.error("url "+audioUrl+" unavailable", e);
            throw new PlayException();
        }

        AudioFormat encodedFormat = encodedStream.getFormat();
        decodedFormat = decodeFormat(encodedFormat);
        decodedStream = AudioSystem.getAudioInputStream(decodedFormat, encodedStream);
        decodedStream = new BufferedInputStream(decodedStream);
        
        Thread thread = new Thread(this);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            logger.warn("play interrupted");
            //thread interrupted, lets continue what we did before
        }
    }

    /**
     * Play a sound from a clip in loop
     * @param audioUrl
     * @exception PlayException 
     */
    public void playLoop(URL audioUrl) throws PlayException {
	loop = true;
	AudioInputStream encodedStream = null;
        try {
            encodedStream = AudioSystem.getAudioInputStream(audioUrl);
        } catch (UnsupportedAudioFileException e) {
            logger.error("Audio url format unsupported", e);
            throw new PlayException();
        } catch (IOException e) {
            logger.error("url "+audioUrl+" unavailable", e);
            throw new PlayException();
        }
        AudioFormat encodedFormat = encodedStream.getFormat();
        decodedFormat = decodeFormat(encodedFormat);
        decodedStream = AudioSystem.getAudioInputStream(decodedFormat, encodedStream);
        decodedStream = new BufferedInputStream(decodedStream);
        
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Plays a sound from a buffered audio stream.
     */
    public void run() {
        stopped = false;
        DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, decodedFormat);
        boolean supportedLine = AudioSystem.isLineSupported(lineInfo);
        if (!supportedLine) {
            logger.error("line unsupported");
            return;
        }
        
        try {
            currentLine = (SourceDataLine)AudioSystem.getLine(lineInfo);
            ((SourceDataLine)currentLine).open(decodedFormat);
        }
        catch (LineUnavailableException e) {
            logger.error("line unavailable, already used?", e);
            return;
        }
        
        try {
            int volume = this.getVolume();
            PlayEvent volumeEvent = new PlayEvent(this);
            volumeEvent.setNewValue(volume);
            fireVolumeChangedEvent(volumeEvent);
        }
        catch (PlayException e) {
            logger.debug("can't fire volume control changed");
        }
        
        try {
            int sampleRate = this.getSpeed();
            PlayEvent sampleRateEvent = new PlayEvent(this);
            sampleRateEvent.setNewValue(sampleRate);
            fireSampleRateChangedEvent(sampleRateEvent);
        }
        catch (PlayException e1) {
            logger.debug("can't fire sample rate control changed");
        }

	PlayEvent event = new PlayEvent(this);
	this.fireReadingStartedEvent(event);
	
        //start playing the sound
        currentLine.start();
        
        byte[] data = new byte[BUFFER_SIZE];
        
        if (decodedStream.markSupported()) {
            float sampleRate = decodedFormat.getSampleRate();
            int sampleSize = decodedFormat.getSampleSizeInBits();
            Long duration = (Long)decodedFormat.getProperty("duration");
            if (sampleRate == AudioSystem.NOT_SPECIFIED || sampleSize == AudioSystem.NOT_SPECIFIED || duration == null) {
        	logger.debug("cannot calculate length of AudioInputStream with sample rate "+sampleRate+", sample size "+sampleSize+", duration "+duration);
                decodedStream.mark(8000000);// a minimum to loop through the progress bar music
            } else {
        	float streamLengthInBytes = sampleRate * sampleSize/8 * duration/1000000;
        	if (streamLengthInBytes > Integer.MAX_VALUE) {
        	    logger.debug("length of AudioInputStream exceeds 2^31, cannot properly reset stream!");
        	    decodedStream.mark(8000000);// a minimum to loop through the progress bar music
        	} else {
        	    logger.debug("length of audio stream buffer set: "+(int)streamLengthInBytes);
        	    decodedStream.mark((int)streamLengthInBytes);
        	}
            }
        } else {
	    logger.debug("mark unsupported for this audio stream");
        }
        
        do {
            int nBytesRead = 0;
            int nBytesWritten = 0;
            try {
                while (nBytesRead != -1) {
                    if (stopped) {
                        break;
                    }
                    synchronized (listener) {
                        if (paused) {
                            currentLine.stop();
                            try {
                        	listener.wait();
                            } catch (InterruptedException e) {
                                logger.info("waiting play interrupted", e);
                            }
                            currentLine.start();
                        }
                    }
                    nBytesRead = decodedStream.read(data, 0, data.length);
                    if (nBytesRead != -1) {
                        nBytesWritten = ((SourceDataLine)currentLine).write(data, 0, nBytesRead);
                        if (nBytesWritten != nBytesRead) {
                            logger.warn(nBytesRead+" bytes read, but "+nBytesWritten+" bytes played");
                        }
                    }
                }
                currentLine.drain();
                if (decodedStream.markSupported()) {
                    decodedStream.reset();
                }
            } catch (IOException e) {
                logger.error("error when reading audio stream", e);
            }
	    
        } while (loop && !stopped);
        
        currentLine.flush();
        currentLine.close();
        try {
	    decodedStream.close();
	} catch (IOException e) {}
        stopped = true;
	fireReadingEndedEvent(event);
    }

    /**
     * 
     * @see org.fingon.player.Player#setOutput(java.io.File)
     */
    public void setOutput(File aFile) {
	logger.warn("output to a file is not implemented");
    }

    /**
     * 
     * @see org.fingon.player.Player#setOutput()
     */
    public void setOutput() {
    }
    
    /**
     * add a play listener
     * @param aListener
     */
    public void addPlayListener(PlayListener aListener) {
        if (this.listener.contains(aListener) == false) {
            this.listener.add(aListener);
        }
    }
    
    /**
     * remove a play listener from the list
     * @param aListener
     */
    public void removePlayListener(PlayListener aListener) {
        this.listener.remove(aListener);
    }
    
    /**
     * fire a sample rate changed event
     * @param progressEvent
     */
    private void fireSampleRateChangedEvent(PlayEvent sampleRateEvent) {
        for (int i=0; i<listener.size(); i++) {
            PlayListener aListener = listener.get(i);
            aListener.sampleRateChanged(sampleRateEvent);
        }
    }

    /**
     * fire a volume changed event
     * @param progressEvent
     */
    private void fireVolumeChangedEvent(PlayEvent volumeEvent) {
        for (int i=0; i<listener.size(); i++) {
            PlayListener aListener = listener.get(i);
            aListener.volumeChanged(volumeEvent);
        }
    }

    /**
     * Pause the current play
     */
    public void pause() {
        synchronized (listener) {
            paused = true;
        }
        PlayEvent event = new PlayEvent(this);
        fireReadingPausedEvent(event);
    }
    
    /**
     * resume the current play
     */
    public void resume() {
        synchronized (listener) {
            paused = false;
            listener.notify();
        }
        PlayEvent event = new PlayEvent(this);
        fireReadingResumedEvent(event);
    }

    /**
     * Stop the current play
     */
    public void stop() {
        stopped = true;
        PlayEvent event = new PlayEvent(this);
        fireReadingStoppedEvent(event);
    }

    /**
     * 
     * @see org.fingon.player.Player#next()
     */
    public void next() {
        stop();
        PlayEvent e = new PlayEvent(this);
        fireNextReadEvent(e);
    }

    /**
     * 
     * @see org.fingon.player.Player#previous()
     */
    public void previous() {
        if (decodedStream != null) {
            try {
        	if (decodedStream.markSupported()) {
        	    decodedStream.reset();
        	}
	    } catch (IOException e) {}
            PlayEvent e = new PlayEvent(this);
            firePreviousReadEvent(e);
        }
    }

    /**
     * Is the player running?
     * @return true if running, false otherwise
     */
    public boolean isRunning() {
        return !stopped;
    }

    /**
     * 
     * @see org.fingon.player.Player#isPaused()
     */
    public boolean isPaused() {
        synchronized (listener) {
            return paused;
        }
    }

    /**
     * get the volume of the current line
     * @return volume
     */
    public int getVolume() throws PlayException {
        try {
            FloatControl volumeControl = (FloatControl)currentLine.getControl(FloatControl.Type.MASTER_GAIN);
            return (int)(volumeControl.getValue()*4 + 100);
        } catch (IllegalArgumentException e) {
            logger.warn("Master gain control unsupported");
            try {
                BooleanControl muteControl = (BooleanControl)currentLine.getControl(BooleanControl.Type.MUTE);
                boolean mute = muteControl.getValue();
                int muteValue = mute ? 0 : 100;
                return muteValue;
            } catch (IllegalArgumentException e1) {
                logger.warn("mute control unsupported");
                throw new PlayException();
            }
        } catch (NullPointerException e) {
            logger.warn("no current line");
            throw new PlayException();
        }
    }

    /**
     * Set the volume of the current line
     * @param volume
     */
    public void setVolume(int volume) {
        if (currentLine != null) {
            if (currentLine.isOpen()) {
                try {
                    FloatControl volumeControl = (FloatControl)currentLine.getControl(FloatControl.Type.MASTER_GAIN);
                    volumeControl.setValue(((float)volume - 100)/4);
                } catch (IllegalArgumentException e) {
                    logger.warn("master gain control unsupported or illegal value ("+volume+")");
                    try {
                        BooleanControl muteControl = (BooleanControl)currentLine.getControl(BooleanControl.Type.MUTE);
                        boolean mute = volume == 0 ? true : false;
                        muteControl.setValue(mute);
                    } catch (IllegalArgumentException e1) {
                        logger.warn("mute control unsupported or illegal value ("+volume+")");
                    }
                }
            }
        }
    }

    /**
     * get the sample rate of the current line
     * @return sample rate
     */
    public int getSpeed() throws PlayException {
	float naturalRate = currentLine.getFormat().getSampleRate();
        try {
            FloatControl sampleRateControl = (FloatControl)currentLine.getControl(FloatControl.Type.SAMPLE_RATE);
            float currentRate = sampleRateControl.getValue();
            return (int)(currentRate*50/naturalRate);
        } catch (IllegalArgumentException e) {
            logger.debug("sample rate control unsupported");
            throw new PlayException();
        } catch (NullPointerException e) {
            logger.debug("no current line");
            throw new PlayException();
        }
    }

    /**
     * Set the sample rate of the current line
     * @param speed
     */
    public void setSpeed(int speed) {
        if (currentLine != null) {
            if (currentLine.isOpen()) {
        	float naturalRate = currentLine.getFormat().getSampleRate();
                try {
                    FloatControl sampleRateControl = (FloatControl)currentLine.getControl(FloatControl.Type.SAMPLE_RATE);
                    float newRate = speed*naturalRate/50;
                    sampleRateControl.setValue(newRate);
                } catch (IllegalArgumentException e) {
                    logger.debug("sample rate control unsupported or illegal value ("+speed+")");
                }
            }
        }
    }

    /**
     * get the balance of the current line
     * @return balance
     */
    public int getBalance() throws PlayException {
        try {
            FloatControl balanceControl = (FloatControl)currentLine.getControl(FloatControl.Type.BALANCE);
            return (int)balanceControl.getValue()*100;
        } catch (IllegalArgumentException e) {
            logger.warn("balance control unsupported");
            try {
                FloatControl panControl = (FloatControl)currentLine.getControl(FloatControl.Type.PAN);
                return (int)panControl.getValue()*100;
            } catch (IllegalArgumentException e1) {
                logger.warn("pan control unsupported");
                throw new PlayException();
            }
        } catch (NullPointerException e) {
            logger.warn("no current line");
            throw new PlayException();
        }
    }

    /**
     * Set the balance of the current line
     * @param balance
     */
    public void setBalance(int balance) {
        if (currentLine != null) {
            if (currentLine.isOpen()) {
                try {
                    FloatControl balanceControl = (FloatControl)currentLine.getControl(FloatControl.Type.BALANCE);
                    balanceControl.setValue((float)balance/100);
                } catch (IllegalArgumentException e) {
                    logger.warn("balance control unsupported or illegal value ("+balance+")");
                    try {
                        FloatControl panControl = (FloatControl)currentLine.getControl(FloatControl.Type.PAN);
                        panControl.setValue((float)balance/100);
                    } catch (IllegalArgumentException e1) {
                        logger.warn("pan control unsupported or illegal value ("+balance+")");
                    }
                }
            }
        }
    }
    
    /**
     * return audio file type corresponding to the filename (based on the extension)
     * @param filename
     * @return
     * @throws PlayException
     */
    public static AudioFileFormat.Type getAudioTypeFromFileName(String filename) throws PlayException {
	Type[] types = AudioSystem.getAudioFileTypes();
	for (Type type : types) {
	    String extension = type.getExtension();
	    if (filename.toLowerCase().endsWith(extension.toLowerCase())) {
		return type;
	    }
	}
        throw new PlayException("unsupported audio encoding extension: "+filename);
    }

    private void fireReadingStartedEvent(PlayEvent event) {
	for (int i = 0; i < listener.size(); i++) {
	    PlayListener aListener = listener.get(i);
	    aListener.readingStarted(event);
	}
    }

    private void fireReadingPausedEvent(PlayEvent event) {
	for (int i = 0; i < listener.size(); i++) {
	    PlayListener aListener = listener.get(i);
	    aListener.readingPaused(event);
	}
    }

    private void fireReadingResumedEvent(PlayEvent event) {
	for (int i = 0; i < listener.size(); i++) {
	    PlayListener aListener = listener.get(i);
	    aListener.readingResumed(event);
	}
    }

    private void fireReadingStoppedEvent(PlayEvent event) {
	for (int i = 0; i < listener.size(); i++) {
	    PlayListener aListener = listener.get(i);
	    aListener.readingStopped(event);
	}
    }

    private void fireReadingEndedEvent(PlayEvent event) {
	for (int i = 0; i < listener.size(); i++) {
	    PlayListener aListener = listener.get(i);
	    aListener.readingEnded(event);
	}
    }

    private void fireNextReadEvent(PlayEvent event) {
	for (int i = 0; i < listener.size(); i++) {
	    PlayListener aListener = listener.get(i);
	    aListener.nextRead(event);
	}
    }

    private void firePreviousReadEvent(PlayEvent event) {
	for (int i = 0; i < listener.size(); i++) {
	    PlayListener aListener = listener.get(i);
	    aListener.previousRead(event);
	}
    }

    /**
     * Makes a decoded format from an encoded one.
     * @param encodedFormat 
     * @return 
     */
    public AudioFormat decodeFormat(AudioFormat encodedFormat) {
        logger.debug("encoded audio format: encoding="+encodedFormat.getEncoding()+",sample rate="+encodedFormat.getSampleRate()+",channels="+encodedFormat.getChannels());
        
        AudioFormat decodedFormat = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED, 	// Encoding to use
            encodedFormat.getSampleRate(),     // sample rate (same as base format)
            16,                         	// sample size in bits (thx to Javazoom)
            encodedFormat.getChannels(),       // # of Channels
            encodedFormat.getChannels()*2,     // Frame Size
            encodedFormat.getSampleRate(),     // Frame Rate
            false                       	// Big Endian
        );
        
        return decodedFormat;
    }

    /**
     * @return loop.
     */
    public boolean isLoop() {
        return this.loop;
    }

    /**
     * @param loop loop 
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public boolean isExtensionSupported(String extension) {
	Type[] types = AudioSystem.getAudioFileTypes();
	for (Type type : types) {
	    String anExtension = type.getExtension();
	    if (anExtension.equalsIgnoreCase(extension)) {
		return true;
	    }
	}
	if (extension.equalsIgnoreCase("mpa")) {
	    return true;
	}
	if (extension.equalsIgnoreCase("mp1")) {
	    return true;
	}
	if (extension.equalsIgnoreCase("mp2")) {
	    return true;
	}
	if (extension.equalsIgnoreCase("mp3")) {
	    return true;
	}
	if (extension.equalsIgnoreCase("ogg")) {
	    return true;
	}
	if (extension.equalsIgnoreCase("aiff")) {
	    return true;
	}
	if (extension.equalsIgnoreCase("aifc")) {
	    return true;
	}
	return false;
    }

    /**
     * Stop the current play and close the input stream.
     * @see java.lang.Object#finalize()
     */
    protected void finalize() throws Throwable {
        this.stop();
        decodedStream.close();
        super.finalize();
    }
}
