package org.fingon.synthesizer;

import java.util.EventObject;
import java.util.List;


/**
 * Speech event
 * @author Paul-Emile
 */
public class SynthesisEvent extends EventObject {
    /** SynthesisEvent.java long */
    private static final long serialVersionUID = -5587469302452675745L;
    /** pitch  */
    private float pitch;
    /** pitch  */
    private float pitchRange;
    /** engine  */
    private EngineDesc engine;
    /** voice  */
    private VoiceDesc voice;
    /** voices list */
    private List<VoiceDesc> voices;

    /**
     * 
     */
    public SynthesisEvent(Object source) {
        super(source);
    }

    /**
     * @return pitch.
     */
    public float getPitch() {
        return this.pitch;
    }

    /**
     * @param pitch pitch 
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * @return pitchRange.
     */
    public float getPitchRange() {
        return this.pitchRange;
    }

    /**
     * @param pitchRange pitchRange 
     */
    public void setPitchRange(float pitchRange) {
        this.pitchRange = pitchRange;
    }

    /**
     * @return voices.
     */
    public List<VoiceDesc> getVoices() {
        return this.voices;
    }

    /**
     * @param voices voices 
     */
    public void setVoices(List<VoiceDesc> voices) {
        this.voices = voices;
    }

    /**
     * @return engine.
     */
    public EngineDesc getEngine() {
        return this.engine;
    }

    /**
     * @param engine  
     */
    public void setEngine(EngineDesc engine) {
        this.engine = engine;
    }

    /**
     * @return voice.
     */
    public VoiceDesc getVoice() {
        return this.voice;
    }

    /**
     * @param voice voice 
     */
    public void setVoice(VoiceDesc voice) {
        this.voice = voice;
    }

}
