package org.fingon.synthesizer;

import java.util.List;
import java.util.Locale;

import org.fingon.player.Player;


/**
 * A speaker integrating a speech synthesizer
 * @author Paul-Emile
 */
public interface SpeechSynthesizer extends Player {
    /** 
     * Puts the text in the text to speak queue and returns immediatly.
     * @param text 
     */
    public void play(String text);
    /** 
     * Puts the text in the text to speak queue and wait for it to be spoken.
     * @param text 
     */
    public void playAndWait(String text);
    /**
     * load engine and default voice
     */
    public void load() throws SynthesisException;
    /**
     * load the first engine and voice with this locale
     * @param locale 
     */
    public boolean load(Locale locale) throws SynthesisException;
    /**
     * 
     * @param name
     * @param mode
     * @param locale
     * @throws SynthesisException
     */
    public void loadEngine(String name, String mode, Locale locale) throws SynthesisException;
    /**
     * 
     * @param selectedVoice
     * @return
     */
    public boolean loadVoice(VoiceDesc selectedVoice);
    
    /** 
     * list the available engines (languages)
     */
    public List<EngineDesc> listAvailableEngines();
    /** 
     * list the available voices 
     */
    public List<VoiceDesc> listAvailableVoices();
    /** 
     * return the engine local 
     */
    public Locale getEngineLocale();
    /**
     * return the current engine descriptor
     * @return
     */
    public EngineDesc getEngineDesc();
    /**
     * Returns the current voice description
     * @return
     */
    public VoiceDesc getVoiceDesc();
    /**
     * add a speech listener
     * @param aListener
     */
    public void addSpeechListener(SynthesisListener aListener);
    /**
     * remove a speech listener from the list
     * @param aListener
     */
    public void removeSpeechListener(SynthesisListener aListener);
    /**
     * 
     * @return
     */
    public float getPitch();
    /**
     * 
     * @param pitch
     */
    public void setPitch(float pitch);
    /**
     * 
     * @return
     */
    public float getPitchRange();
    /**
     * 
     * @param pitchRange
     */
    public void setPitchRange(float pitchRange);
    /**
     * 
     * @param locale
     * @return
     */
    public boolean matchEngineLanguage(Locale locale);
}
