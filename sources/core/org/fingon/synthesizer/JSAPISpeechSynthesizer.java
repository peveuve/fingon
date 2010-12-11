package org.fingon.synthesizer;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.fingon.player.PlayEvent;
import org.fingon.player.PlayException;
import org.fingon.player.PlayListener;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineList;
import javax.speech.EngineModeDesc;
import javax.speech.EngineStateError;
import javax.speech.synthesis.SpeakableEvent;
import javax.speech.synthesis.SpeakableListener;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.SynthesizerProperties;
import javax.speech.synthesis.SynthesizerQueueItem;
import javax.speech.synthesis.Voice;

/**
 * A speech synthesizer using the Sun's Java Speech API.
 * Fits any JSAPI implementation.
 * @author Paul-Emile
 */
public class JSAPISpeechSynthesizer implements SpeechSynthesizer, SpeakableListener {
    /** logger */
    private Logger logger = Logger.getLogger(JSAPISpeechSynthesizer.class);
    /** speech synthesizer */
    protected Synthesizer synthe;
    /** flag to stop the current reading (break from the thread) */
    protected boolean stopped = true;
    /** flag to pause the current reading (wait for notify) */
    protected boolean paused = false;
    /** speech listeners list */
    protected List<SynthesisListener> listener;
    /** list of play listeners */
    protected List<PlayListener> playListener;

    /**
     * Load the default speech synthesizer
     */
    public JSAPISpeechSynthesizer() throws SynthesisException {
	listener = new ArrayList<SynthesisListener>();
	playListener = new ArrayList<PlayListener>();
	this.load();
    }

    /**
     * Tries to read the URL as a stream of characters, sentence by sentence.
     * Sentences are delimited by the point character.
     * @param anUrl an URL pointing a characters resource
     * @throws PlayException
     */
    public void play(URL anUrl) throws PlayException {
	Reader r = null;
	try {
	    InputStream in = anUrl.openStream();
	    r = new BufferedReader(new InputStreamReader(in));
	    StreamTokenizer tokenizer = new StreamTokenizer(r);
	    tokenizer.resetSyntax();
	    tokenizer.eolIsSignificant(false);
	    tokenizer.whitespaceChars('.', '.');
	    while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
		switch (tokenizer.ttype) {
		case StreamTokenizer.TT_WORD:
		    play(tokenizer.sval);
		    break;
		}
	    }
	} catch (IOException e) {
	    logger.error(e);
	} finally {
	    if (r != null) {
		try {
		    r.close();
		} catch (IOException e) {}
	    }
	}
    }
    
    /**
     * Play a media from an URL and wait for it's ending
     * @param anUrl
     * @exception PlayException 
     */
    public void playAndWait(URL anUrl) throws PlayException {
	Reader r = null;
	try {
	    InputStream in = anUrl.openStream();
	    r = new BufferedReader(new InputStreamReader(in));
	    StreamTokenizer tokenizer = new StreamTokenizer(r);
	    tokenizer.resetSyntax();
	    tokenizer.eolIsSignificant(false);
	    tokenizer.whitespaceChars('.', '.');
	    while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
		switch (tokenizer.ttype) {
		case StreamTokenizer.TT_WORD:
		    playAndWait(tokenizer.sval);
		    break;
		}
	    }
	} catch (IOException e) {
	    logger.error(e);
	} finally {
	    if (r != null) {
		try {
		    r.close();
		} catch (IOException e) {}
	    }
	}
    }
    
    /**
     * Plays a sound from a clip in loop.
     * @param anUrl 
     * @exception PlayException 
     */
    public void playLoop(URL anUrl) throws PlayException {
	
    }
    
    /**
     * Loads the first engine with the default locale and the first voice of
     * this engine. if there is no engine with default locale, select English.
     */
    public void load() throws SynthesisException {
	// try to find an engine with the default locale
	Locale defaultLocale = Locale.getDefault();
	boolean engineFound = this.load(defaultLocale);
	// if not found, try to find an engine with the English locale
	if (!engineFound) {
	    engineFound = this.load(Locale.ENGLISH);
	}
	// if not found, load the first engine available
	if (!engineFound) {
	    EngineList list = getAvailableSynthesizers();
	    if (list.size() > 0) {
                SynthesizerModeDesc eng = (SynthesizerModeDesc) list.get(0);
                Locale engineLocale = eng.getLocale();
                loadEngine(eng.getEngineName(), eng.getModeName(), engineLocale);
                Voice[] voices = eng.getVoices();
                
                VoiceDesc voiceDesc = new VoiceDesc();
                voiceDesc.setName(voices[0].getName());
                voiceDesc.setGender(voices[0].getGender());
                voiceDesc.setAge(voices[0].getAge());
                loadVoice(voiceDesc);
	    } else {
		throw new SynthesisException();
	    }
	}
    }

    /**
     * Loads an engine for the given locale if possible.
     * @param locale
     * @throws SynthesisException
     */
    public boolean load(Locale locale) throws SynthesisException {
	String currentEngine = null;
	if (synthe != null) {
	    EngineModeDesc engineDesc = synthe.getEngineModeDesc();
	    Locale currentLocale = engineDesc.getLocale();
	    if (currentLocale.equals(locale)) {
		return true;
	    }
	    currentEngine = engineDesc.getEngineName();
	}
	EngineList list = getAvailableSynthesizers();
	for (int i = 0; i < list.size(); i++) {
	    SynthesizerModeDesc eng = (SynthesizerModeDesc) list.get(i);
	    Locale engineLocale = eng.getLocale();
	    String engineName = eng.getEngineName();
	    String engineMode = eng.getModeName();
	    if (locale.getLanguage().equals(engineLocale.getLanguage())) {
		if (currentEngine == null || engineName.equals(currentEngine)) {
                    loadEngine(engineName, engineMode, engineLocale);
                    Voice[] voices = eng.getVoices();
                    
                    VoiceDesc voiceDesc = new VoiceDesc();
                    voiceDesc.setName(voices[0].getName());
                    voiceDesc.setGender(voices[0].getGender());
                    voiceDesc.setAge(voices[0].getAge());
                    loadVoice(voiceDesc);
                    return true;
		}
	    }
	}
	return false;
    }

    /**
     * add a speech listener
     * @param aListener
     */
    public void addSpeechListener(SynthesisListener aListener) {
	if (this.listener.contains(aListener) == false) {
	    this.listener.add(aListener);
	}
    }

    /**
     * remove a speech listener from the list
     * @param aListener
     */
    public void removeSpeechListener(SynthesisListener aListener) {
	this.listener.remove(aListener);
    }

    /**
     * fire a pitch changed event
     * @param event
     */
    protected void firePitchChanged(SynthesisEvent event) {
	for (int i = 0; i < listener.size(); i++) {
	    SynthesisListener aListener = listener.get(i);
	    aListener.pitchChanged(event);
	}
    }

    /**
     * fire a pitch range changed event
     * @param event
     */
    protected void firePitchRangeChanged(SynthesisEvent event) {
	for (int i = 0; i < listener.size(); i++) {
	    SynthesisListener aListener = listener.get(i);
	    aListener.pitchRangeChanged(event);
	}
    }

    /**
     * fire a voices list changed event
     * @param event
     */
    protected void fireVoicesListChanged(SynthesisEvent event) {
	for (int i = 0; i < listener.size(); i++) {
	    SynthesisListener aListener = listener.get(i);
	    aListener.voicesListChanged(event);
	}
    }

    /**
     * fire a engines list changed event
     * @param event
     */
    protected void fireEngineChanged(SynthesisEvent event) {
	for (int i = 0; i < listener.size(); i++) {
	    SynthesisListener aListener = listener.get(i);
	    aListener.engineChanged(event);
	}
    }

    /**
     * Fires a event when the voice has changed
     * @param event
     */
    protected void fireVoiceChanged(SynthesisEvent event) {
	for (int i = 0; i < listener.size(); i++) {
	    SynthesisListener aListener = listener.get(i);
	    aListener.voiceChanged(event);
	}
    }

    protected EngineList getAvailableSynthesizers() {
	return Central.availableSynthesizers(null);
    }
    
    /**
     * Lists all the available engines but the time engines.
     * @return
     */
    public List<EngineDesc> listAvailableEngines() {
	List<EngineDesc> availableEngines = new ArrayList<EngineDesc>();

	EngineList list = getAvailableSynthesizers();
	logger.debug(list.size() + " engines available");
	for (int i = 0; i < list.size(); i++) {
	    SynthesizerModeDesc engineDesc = (SynthesizerModeDesc) list.get(i);
	    String mode = engineDesc.getModeName();
	    if (mode.equals("time")) {
		logger.info("time engine skipped");
		continue;
	    }
	    EngineDesc engine = new EngineDesc(engineDesc);
	    availableEngines.add(engine);
	}

	return availableEngines;
    }

    /**
     * Lists all the available voices for the current engine
     * @return
     */
    public List<VoiceDesc> listAvailableVoices() {
	List<VoiceDesc> availableVoices = new ArrayList<VoiceDesc>();

	SynthesizerModeDesc desc = (SynthesizerModeDesc) synthe.getEngineModeDesc();
	Voice[] voices = desc.getVoices();
	for (int i = 0; i < voices.length; i++) {
	    VoiceDesc voiceDesc = new VoiceDesc();
	    String voiceName = voices[i].getName();
	    voiceDesc.setName(voiceName);
	    String voiceStyle = voices[i].getStyle();
	    voiceDesc.setStyle(voiceStyle);
	    int voiceAge = voices[i].getAge();
	    voiceDesc.setAge(voiceAge);
	    int voiceGender = voices[i].getGender();
	    voiceDesc.setGender(voiceGender);

	    availableVoices.add(voiceDesc);
	}

	return availableVoices;
    }

    /**
     * Load an engine using it's name, mode and locale. Some engines can
     * have the same name and/or mode.
     * @param name engine name
     * @param mode engine mode
     * @param locale engine locale
     * @throws SynthesisException
     */
    public void loadEngine(String name, String mode, Locale locale) throws SynthesisException {
	SynthesizerModeDesc required = null;

	EngineList list = getAvailableSynthesizers();
	for (int i = 0; i < list.size(); i++) {
	    SynthesizerModeDesc eng = (SynthesizerModeDesc) list.get(i);
	    String engineName = eng.getEngineName();
	    String modeName = eng.getModeName();
	    Locale engineLocale = eng.getLocale();
	    if (engineName.equals(name) && modeName.equals(mode) && engineLocale.equals(locale)) {
		required = eng;
		break;
	    }
	}
	try {
	    logger.debug("try load engine " + name);
	    synthe = Central.createSynthesizer(required);
	} catch (IllegalArgumentException e) {
	    logger.error("No engine available for the specified locale, gender and age", e);
	    throw new SynthesisException("No engine available for the specified locale, gender and age");
	} catch (EngineException e) {
	    logger.error("Impossible to create engine for the specified locale, gender and age", e);
	    throw new SynthesisException("Impossible to create engine for the specified locale, gender and age");
	}

	if (synthe == null) {
	    logger.warn("synthetizer required not created");
	    throw new SynthesisException("synthetizer required not created");
	}
        try {
            synthe.allocate();
        } catch (EngineException e) {
            logger.error("engine can't be allocated", e);
            throw new SynthesisException("engine can't be allocated");
        } catch (EngineStateError e) {
            logger.error("engine is not in the right state", e);
            throw new SynthesisException("engine is not in the right state");
        }
            
        float volume = synthe.getSynthesizerProperties().getVolume();
        if (volume == 0) {
            try {
		synthe.getSynthesizerProperties().setVolume(1f);
            } catch (PropertyVetoException e) {
	        logger.error("error when setting synthesizer volume to 1.0", e);
            }
        }
            
        SynthesisEvent synthesisEvent = new SynthesisEvent(this);
        EngineModeDesc engineDesc = synthe.getEngineModeDesc();
        EngineDesc engine = new EngineDesc(engineDesc);
        synthesisEvent.setEngine(engine);
	VoiceDesc voice = this.getVoiceDesc();
	synthesisEvent.setVoice(voice);
	float pitch = this.getPitch();
	synthesisEvent.setPitch(pitch);
	float pitchRange = this.getPitchRange();
	synthesisEvent.setPitchRange(pitchRange);
	List<VoiceDesc> voices = this.listAvailableVoices();
	synthesisEvent.setVoices(voices);
        this.fireEngineChanged(synthesisEvent);
	this.firePitchChanged(synthesisEvent);
	this.firePitchRangeChanged(synthesisEvent);
	this.fireVoicesListChanged(synthesisEvent);
	this.fireVoiceChanged(synthesisEvent);
    }

    /**
     * Try to load the voice with the description in parameter. 
     * Check the name first, then gender and age, gender alone, and age alone next.
     * @param selectedVoice desired voice description
     */
    public boolean loadVoice(VoiceDesc selectedVoice) {
	logger.debug("try load voice " + selectedVoice);
	Voice voice = null;
	SynthesizerModeDesc desc = (SynthesizerModeDesc) synthe.getEngineModeDesc();
	Voice[] voices = desc.getVoices();
	String name = selectedVoice.getName();
	if (name != null) {
            for (int i = 0; i < voices.length; i++) {
        	String testedVoiceName = voices[i].getName();
                if (testedVoiceName.equals(name)) {
                    voice = voices[i];
                    logger.debug("voice found : " + voice.getName());
                    break;
                }
            }
	}
	if (voice == null) {
            int age = selectedVoice.getAge();
            int gender = selectedVoice.getGender();
            for (int i = 0; i < voices.length; i++) {
                if (voices[i].getGender() == gender && voices[i].getAge() == age) {
                    voice = voices[i];
                    logger.debug("voice found : " + voice.getName());
                    break;
                }
            }
            if (voice == null) {
                for (int i = 0; i < voices.length; i++) {
                    if (voices[i].getGender() == gender) {
                        voice = voices[i];
                        logger.debug("voice found : " + voice.getName());
                        break;
                    }
                }
        	if (voice == null) {
                    for (int i = 0; i < voices.length; i++) {
                        if (voices[i].getAge() == age) {
                            voice = voices[i];
                            logger.debug("voice found : " + voice.getName());
                            break;
                        }
                    }
                    if (voice == null) {
                        return false;
                    }
        	}
            }
	}

	if (isPaused()) {
	    resume();
	    synthe.cancel();
	}
	SynthesizerProperties syntheProp = synthe.getSynthesizerProperties();
	try {
	    syntheProp.setVoice(voice);

	    SynthesisEvent synthesisEvent = new SynthesisEvent(this);
	    float pitch = this.getPitch();
	    synthesisEvent.setPitch(pitch);
	    float pitchRange = this.getPitchRange();
	    synthesisEvent.setPitchRange(pitchRange);
	    selectedVoice.setName(voice.getName());
	    selectedVoice.setAge(voice.getAge());
	    selectedVoice.setGender(voice.getGender());
	    EngineDesc engine = this.getEngineDesc();
	    synthesisEvent.setEngine(engine);
	    synthesisEvent.setVoice(selectedVoice);
	    
	    this.firePitchChanged(synthesisEvent);
	    this.firePitchRangeChanged(synthesisEvent);
	    this.fireVoiceChanged(synthesisEvent);
	    
	    return true;
	} catch (PropertyVetoException e) {
	    logger.error("impossible to select the voice " + selectedVoice, e);
	    return false;
	}
    }

    /**
     * return the language of the synthesizer
     * @return the engine locale
     */
    public Locale getEngineLocale() {
	EngineModeDesc engineDesc = synthe.getEngineModeDesc();
	return engineDesc.getLocale();
    }

    /**
     * compare the language of the engine with the language in parameter
     * @param locale a locale
     * @return true if equal, false otherwise
     */
    public boolean matchEngineLanguage(Locale locale) {
	Locale engineLocale = getEngineLocale();
	String engineLanguage = engineLocale.getLanguage();
	String otherLanguage = locale.getLanguage();
	if (engineLanguage.equals(otherLanguage)) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Speaks the text in parameter.
     * @param text text to speak
     */
    public void play(String text) {
	// it is necessary to set a speakable listener, so TalkingJava
	// will be able to cancel the SynthesizerQueueItem generated
	try {
	    synthe.speakPlainText(text, this);
	} catch (EngineStateError e) {
	    logger.error("engine state error : " + e.getMessage());
	}
    }

    /**
     * Speaks the text in parameter and wait for the end before returning.
     * @param text
     */
    public void playAndWait(String text) {
	// it is necessary to set a speakable listener, so TalkingJava
	// will be able to cancel the SynthesizerQueueItem generated
	try {
	    synthe.speakPlainText(text, this);
	    synthe.waitEngineState(Synthesizer.QUEUE_EMPTY);
	} catch (EngineStateError e) {
	    logger.error("engine state error : " + e.getMessage());
	} catch (InterruptedException e) {
            logger.warn("speak interrupted : " + e.getMessage());
        }
    }
    
    /**
     * Stops the current speech: clear the synthesizer queue.
     */
    public void stop() {
	try {
	    synthe.cancelAll();
	} catch (EngineStateError e) {
	    logger.error("engine state error : " + e.getMessage());
	} catch (Exception e) {
	    // a NullPointerException occurs sometimes with FreeTTS during the start
	    logger.error(e);
	}
	
	PlayEvent event = new PlayEvent(this);
	fireReadingStoppedEvent(event);
    }

    /**
     * Cancels this text from the text to speak queue.
     * @see org.fingon.synthesizer.SpeechSynthesizer#cancel(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public void cancel(String text) {
	// TalkingJava and FreeTTS can only cancel a SynthesizerQueueItem, not the text itself
	Enumeration<SynthesizerQueueItem> enumQueue = synthe.enumerateQueue();
	SynthesizerQueueItem itemToCancel = null;
	while (enumQueue.hasMoreElements()) {
	    SynthesizerQueueItem aQueuedItem = enumQueue.nextElement();
	    Object aQueuedText = aQueuedItem.getSource();
	    if (aQueuedText.equals(text)) {
		itemToCancel = aQueuedItem;
		break;
	    }
	}
	if (itemToCancel != null) {
	    try {
		synthe.cancel(itemToCancel);
	    } catch (EngineStateError e) {
        	logger.error("engine state error : " + e.getMessage());
	    } catch (IllegalArgumentException e) {
		// an IllegalArgumentException occurs if the text has already been spoken
		// and was removed from the queue. In this case, nothing to do.
		logger.error("the text was not cancel and remove from the queue: " + e.getMessage());
	    }
	}
    }
    
    /**
     * Pause the current speech : set a flag and pause the engine
     */
    public void pause() {
	try {
	    synthe.pause();
	} catch (EngineStateError e) {
	    logger.error("engine state error : " + e.getMessage());
	}
	PlayEvent event = new PlayEvent(this);
	fireReadingPausedEvent(event);
    }

    /**
     * Resume the current speech : reset a flag and resume the engine
     */
    public void resume() {
	try {
	    synthe.resume();
	} catch (EngineStateError e) {
	    logger.error("engine state error while resuming : " + e.getMessage());
	} catch (AudioException e) {
	    logger.error("audio : " + e.getMessage());
	}
	PlayEvent event = new PlayEvent(this);
	fireReadingResumedEvent(event);
    }

    /**
     * 
     * @see org.fingon.player.Player#isRunning()
     */
    public boolean isRunning() {
	return (synthe.testEngineState(Synthesizer.QUEUE_NOT_EMPTY) && !synthe.testEngineState(Synthesizer.PAUSED));
    }
    
    /**
     * 
     * @see org.fingon.player.Player#isPaused()
     */
    public boolean isPaused() {
	return synthe.testEngineState(Synthesizer.PAUSED);
    }
    
    /**
     * read the next speakable
     */
    public void next() {
	try {
	    synthe.cancel();
	} catch (EngineStateError e) {
	    logger.error("engine state error : " + e.getMessage());
	}
	PlayEvent event = new PlayEvent(this);
	fireNextReadEvent(event);
    }

    /**
     * Read the previous speakable
     */
    public void previous() {
	try {
	    synthe.cancel();
	} catch (EngineStateError e) {
	    logger.error("engine state error : " + e.getMessage());
	}
	PlayEvent event = new PlayEvent(this);
	firePreviousReadEvent(event);
    }

    /**
     * 
     * @see org.fingon.synthesizer.SpeechSynthesizer#getEngineDesc()
     */
    public EngineDesc getEngineDesc() {
	EngineModeDesc engineMode = synthe.getEngineModeDesc();
	EngineDesc engineDesc = new EngineDesc(engineMode);
	return engineDesc;
    }
    
    /**
     * 
     * @see org.fingon.synthesizer.SpeechSynthesizer#getVoiceDesc()
     */
    public VoiceDesc getVoiceDesc() {
	VoiceDesc voiceDesc = new VoiceDesc();
	SynthesizerProperties syntheProp = synthe.getSynthesizerProperties();
	Voice voice = syntheProp.getVoice();
	voiceDesc.setName(voice.getName());
	voiceDesc.setAge(voice.getAge());
	voiceDesc.setGender(voice.getGender());
	return voiceDesc;
    }
    
    private void fireReadingStartedEvent(PlayEvent event) {
	for (int i = 0; i < playListener.size(); i++) {
	    PlayListener aListener = playListener.get(i);
	    aListener.readingStarted(event);
	}
    }

    private void fireReadingPausedEvent(PlayEvent event) {
	for (int i = 0; i < playListener.size(); i++) {
	    PlayListener aListener = playListener.get(i);
	    aListener.readingPaused(event);
	}
    }

    private void fireReadingResumedEvent(PlayEvent event) {
	for (int i = 0; i < playListener.size(); i++) {
	    PlayListener aListener = playListener.get(i);
	    aListener.readingResumed(event);
	}
    }

    private void fireReadingStoppedEvent(PlayEvent event) {
	for (int i = 0; i < playListener.size(); i++) {
	    PlayListener aListener = playListener.get(i);
	    aListener.readingStopped(event);
	}
    }

    private void fireReadingEndedEvent(PlayEvent event) {
	for (int i = 0; i < playListener.size(); i++) {
	    PlayListener aListener = playListener.get(i);
	    aListener.readingEnded(event);
	}
    }

    private void fireNextReadEvent(PlayEvent event) {
	for (int i = 0; i < playListener.size(); i++) {
	    PlayListener aListener = playListener.get(i);
	    aListener.nextRead(event);
	}
    }

    private void firePreviousReadEvent(PlayEvent event) {
	for (int i = 0; i < playListener.size(); i++) {
	    PlayListener aListener = playListener.get(i);
	    aListener.previousRead(event);
	}
    }
    
    protected void fireVolumeChangedEvent(PlayEvent event) {
	for (int i = 0; i < playListener.size(); i++) {
	    PlayListener aListener = playListener.get(i);
	    aListener.volumeChanged(event);
	}
    }

    private void fireSampleRateChangedEvent(PlayEvent event) {
	for (int i = 0; i < playListener.size(); i++) {
	    PlayListener aListener = playListener.get(i);
	    aListener.sampleRateChanged(event);
	}
    }

    public void addPlayListener(PlayListener listener) {
	if (!playListener.contains(listener)) {
	    playListener.add(listener);
	}
    }

    public void removePlayListener(PlayListener listener) {
	playListener.remove(listener);
    }

    /**
     * 
     * @see org.fingon.player.Player#getSpeed()
     */
    public int getSpeed() throws PlayException {
	SynthesizerProperties prop = (SynthesizerProperties) synthe.getEngineProperties();
	return (int) prop.getSpeakingRate()/4;
    }

    /**
     * 
     * @see org.fingon.player.Player#setSpeed(int)
     */
    public void setSpeed(int sampleRate) {
	SynthesizerProperties prop = (SynthesizerProperties) synthe.getEngineProperties();
	try {
	    prop.setSpeakingRate((float) sampleRate*4);
	    
	    PlayEvent event = new PlayEvent(this);
	    event.setNewValue(sampleRate);
	    fireSampleRateChangedEvent(event);
	} catch (PropertyVetoException pe) {
	    logger.error("Bad speaking rate value : " + pe.getMessage());
	}
    }

    /**
     * 
     * @see org.fingon.player.Player#getVolume()
     */
    public int getVolume() throws PlayException {
	SynthesizerProperties prop = (SynthesizerProperties) synthe.getEngineProperties();
	return (int) prop.getVolume()*100;
    }

    /**
     * 
     * @see org.fingon.player.Player#setVolume(int)
     */
    public void setVolume(int volume) {
	SynthesizerProperties prop = (SynthesizerProperties) synthe.getEngineProperties();
	try {
	    prop.setVolume((float) volume / 100);
	    
	    PlayEvent event = new PlayEvent(this);
	    event.setNewValue(volume);
	    fireVolumeChangedEvent(event);
	} catch (PropertyVetoException pe) {
	    logger.error("Bad volume value : " + pe.getMessage());
	}
    }

    /**
     * 
     * @see org.fingon.synthesizer.SpeechSynthesizer#getPitch()
     */
    public float getPitch() {
	SynthesizerProperties prop = (SynthesizerProperties) synthe.getEngineProperties();
	return prop.getPitch()*100/300;
    }

    /**
     * 
     * @see org.fingon.synthesizer.SpeechSynthesizer#setPitch(float)
     */
    public void setPitch(float pitch) {
	SynthesizerProperties prop = (SynthesizerProperties) synthe.getEngineProperties();
	try {
	    prop.setPitch(pitch/100*300);

	    SynthesisEvent pitchEvent = new SynthesisEvent(this);
	    pitchEvent.setPitch(pitch);
	    this.firePitchChanged(pitchEvent);

	} catch (PropertyVetoException e) {
	    logger.error("Bad pitch value : " + e.getMessage());
	}
    }

    /**
     * 
     * @see org.fingon.synthesizer.SpeechSynthesizer#getPitchRange()
     */
    public float getPitchRange() {
	SynthesizerProperties prop = (SynthesizerProperties) synthe.getEngineProperties();
	float pitch = prop.getPitch();
	return prop.getPitchRange()*100/(pitch*80/100);
    }

    /**
     * 
     * @see org.fingon.synthesizer.SpeechSynthesizer#setPitchRange(float)
     */
    public void setPitchRange(float pitchRange) {
	SynthesizerProperties prop = (SynthesizerProperties) synthe.getEngineProperties();
	float pitch = prop.getPitch();
	try {
	    prop.setPitchRange(pitchRange*(pitch*80/100)/100);

	    SynthesisEvent synthesisEvent = new SynthesisEvent(this);
	    synthesisEvent.setPitchRange(pitchRange);
	    this.firePitchRangeChanged(synthesisEvent);
	    
	} catch (PropertyVetoException e) {
	    logger.error("Bad pitch range value : " + e.getMessage());
	}
    }

    /**
     * Unload the current synthesizer
     */
    public void unload() {
	if (synthe != null) {
	    try {
		synthe.deallocate();
		synthe.waitEngineState(Synthesizer.DEALLOCATED);
	    } catch (EngineException e) {
		logger.error("engine can't be deallocated : " + e.getMessage());
	    } catch (EngineStateError e) {
		logger.error("engine is not in the right state : " + e.getMessage());
	    } catch (IllegalArgumentException e) {
		logger.error("wrong waiting state : " + e.getMessage());
	    } catch (InterruptedException e) {
		logger.error("deallocation interrupted : " + e.getMessage());
	    }
	}
    }

    /**
     * To be overloaded in the subclass
     * @see org.fingon.player.Player#setOutput(java.io.File)
     */
    public void setOutput(File aFile) {
    }

    /**
     * To be overloaded in the subclass
     * @see org.fingon.player.Player#setOutput()
     */
    public void setOutput() {
    }

    /**
     * 
     * @see org.fingon.player.Player#isExtensionSupported(java.lang.String)
     */
    public boolean isExtensionSupported(String extension) {
	if (extension.equalsIgnoreCase("htm")) {
	    return true;
	}
	if (extension.equalsIgnoreCase("html")) {
	    return true;
	}
	if (extension.equalsIgnoreCase("xhtml")) {
	    return true;
	}
	if (extension.equalsIgnoreCase("rdf")) {
	    return true;
	}
	if (extension.equalsIgnoreCase("rss")) {
	    return true;
	}
	if (extension.equalsIgnoreCase("atom")) {
	    return true;
	}
	if (extension.equalsIgnoreCase("opml")) {
	    return true;
	}
	return false;
    }

    @Override
    public void markerReached(SpeakableEvent arg0) {
    }

    @Override
    public void speakableCancelled(SpeakableEvent arg0) {
    }

    @Override
    public void speakableEnded(SpeakableEvent arg0) {
    }

    @Override
    public void speakablePaused(SpeakableEvent arg0) {
    }

    @Override
    public void speakableResumed(SpeakableEvent arg0) {
    }

    @Override
    public void speakableStarted(SpeakableEvent arg0) {
    }

    @Override
    public void topOfQueue(SpeakableEvent arg0) {
    }

    @Override
    public void wordStarted(SpeakableEvent arg0) {
    }
    
    /**
     * Unload the engine before destroying the object
     */
    protected void finalize() throws Throwable {
	unload();
    }
}
