package org.fingon.synthesizer;

import java.util.Locale;

import javax.speech.EngineModeDesc;

/**
 * Speech synthesizer engine description. Useful for display in combobox.
 * @author Paul-Emile
 */
public class EngineDesc {

    private String engineName;
    private String modeName;
    private Locale locale;
    
    public EngineDesc(EngineModeDesc engine) {
	this.engineName = engine.getEngineName();
	this.modeName = engine.getModeName();
	this.locale = engine.getLocale();
    }

    /**
     * @return engineName.
     */
    public String getEngineName() {
        return this.engineName;
    }

    /**
     * @param engineName engineName 
     */
    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    /**
     * @return modeName.
     */
    public String getModeName() {
        return this.modeName;
    }

    /**
     * @param modeName modeName 
     */
    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    /**
     * @return locale.
     */
    public Locale getLocale() {
        return this.locale;
    }

    /**
     * @param locale locale 
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	String desc = "";
        if (engineName.indexOf(modeName) != -1) {
            desc = engineName;
        } else if (modeName.indexOf(engineName) != -1) {
            desc = modeName;
        } else {
            desc = engineName + " " + modeName;
        }
	return locale.getDisplayName() + " " + desc;
    }
}
