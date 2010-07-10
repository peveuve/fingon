package org.fingon.synthesizer;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;

import org.apache.log4j.Logger;
import org.fingon.player.PlayException;
import org.fingon.player.SoundPlayer;
import org.fingon.synthesizer.JSAPISpeechSynthesizer;
import org.fingon.synthesizer.SynthesisException;

import com.cloudgarden.audio.AudioFileSink;
import com.cloudgarden.speech.CGAudioManager;

/**
 * A speech synthesizer implemented with the CloudGarden's synthesizer Talking Java.
 * @author Paul-Emile
 */
public class TalkingJavaSpeechSynthesizer extends JSAPISpeechSynthesizer {
    /** logger */
    private Logger logger = Logger.getLogger(TalkingJavaSpeechSynthesizer.class);

    /**
     * @throws SynthesisException
     */
    public TalkingJavaSpeechSynthesizer() throws SynthesisException {
	super();
    }

    /**
     * 
     * @see org.fingon.player.Player#setOutput(java.io.File)
     */
    public void setOutput(File aFile) {
	CGAudioManager audioMgr = (CGAudioManager)synthe.getAudioManager();
	AudioFileFormat.Type type = null;
	try {
	    type = SoundPlayer.getAudioTypeFromFileName(aFile.getName());
	} catch (PlayException e1) {
	    logger.error(e1);
	    type = AudioFileFormat.Type.WAVE;
	}
	AudioFormat fmt = new AudioFormat(22000,16,1,true,false);
	AudioFileSink fileSink = new AudioFileSink(aFile, fmt, type);
	try {
	    audioMgr.setSink(fileSink);
	} catch (IOException e) {
	    logger.error(e);
	}
    }

    /**
     * 
     * @see org.fingon.player.Player#setOutput()
     */
    public void setOutput() {
	CGAudioManager audioMgr = (CGAudioManager)synthe.getAudioManager();
	try {
	    audioMgr.setSink(null);
	} catch (IOException e) {
	    logger.error(e);
	}
    }
}
