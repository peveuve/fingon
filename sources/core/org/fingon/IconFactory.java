package org.fingon;

import java.net.URL;
import java.util.Locale;

import javax.swing.ImageIcon;

import org.fingon.synthesizer.VoiceDesc;

/**
 * Icon factory
 * @author Paul-Emile
 */
public class IconFactory {
    
    /**
     * 
     */
    public IconFactory() {
        super();
    }
    
    /**
     * return the flag's icon corresponding to the locale in parameter
     * @param locale
     * @return
     */
    public static ImageIcon getFlagIconByLocale(Locale locale) {
        URL iconUrl = null;
        if (locale.equals(Locale.FRENCH) || locale.equals(Locale.FRANCE)) {
            iconUrl = IconFactory.class.getResource("flag/France.png");
        } else if (locale.equals(Locale.ENGLISH) || locale.equals(Locale.US)) {
            iconUrl = IconFactory.class.getResource("flag/USA.png");
        } else if (locale.equals(Locale.UK)) {
            iconUrl = IconFactory.class.getResource("flag/UK.png");
        } else if (locale.equals(Locale.CANADA)) {
            iconUrl = IconFactory.class.getResource("flag/Canada.png");
        } else if (locale.equals(new Locale("en", "AU"))) {
            iconUrl = IconFactory.class.getResource("flag/Australia.png");
        } else if (locale.equals(new Locale("es")) || locale.equals(new Locale("es", "ES"))) {
            iconUrl = IconFactory.class.getResource("flag/Spain.png");
        } else if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
            iconUrl = IconFactory.class.getResource("flag/Germany.png");
        } else if (locale.equals(Locale.ITALIAN) || locale.equals(Locale.ITALY)) {
            iconUrl = IconFactory.class.getResource("flag/Italy.png");
        } else if (locale.equals(new Locale("pt")) || locale.equals(new Locale("pt", "PT"))) {
            iconUrl = IconFactory.class.getResource("flag/Portugal.png");
        } else if (locale.equals(new Locale("pt", "BR"))) {
            iconUrl = IconFactory.class.getResource("flag/Brazil.png");
        } else if (locale.equals(Locale.JAPAN) || locale.equals(Locale.JAPANESE)) {
            iconUrl = IconFactory.class.getResource("flag/Japan.png");
        } else if (locale.equals(Locale.KOREA) || locale.equals(Locale.KOREAN)) {
            iconUrl = IconFactory.class.getResource("flag/South Korea.png");
        } else if (locale.equals(Locale.CHINA) || locale.equals(Locale.CHINESE)) {
            iconUrl = IconFactory.class.getResource("flag/China.png");
        } else if (locale.equals(new Locale("fi")) || locale.equals(new Locale("fi", "FI"))) {
            iconUrl = IconFactory.class.getResource("flag/Finland.png");
        } else if (locale.equals(new Locale("ru")) || locale.equals(new Locale("ru", "RU"))) {
            iconUrl = IconFactory.class.getResource("flag/Russian Federation.png");
        } else if (locale.equals(new Locale("hi")) || locale.equals(new Locale("hi", "IN"))) {
            iconUrl = IconFactory.class.getResource("flag/India.png");
        } else {
            return null;
        }
        ImageIcon icon = new ImageIcon(iconUrl);
        return icon;
    }
    
    /**
     * 
     * @param voice
     * @return
     */
    public static ImageIcon getIconByVoice(VoiceDesc voice) {
	URL iconUrl = null;
        int gender = voice.getGender();
        int age = voice.getAge();
        
        //woman
        if (gender == 1) {
            //girl
            if (age == 1) {
        	iconUrl = IconFactory.class.getResource("person/woman.png");
            //teenage girl
            } else if (age == 2) {
        	iconUrl = IconFactory.class.getResource("person/woman.png");
            //adult woman
            } else if (age == 3 || age == 4 || age == 5) {
        	iconUrl = IconFactory.class.getResource("person/woman.png");
            //woman without age
            } else if (age == 6) {
        	iconUrl = IconFactory.class.getResource("person/woman.png");
            } else {
        	iconUrl = IconFactory.class.getResource("person/woman.png");
            }
        }
        //man
        else if (gender == 2) {
            //boy
            if (age == 1) {
        	iconUrl = IconFactory.class.getResource("person/boy.png");
            //teenage boy
            } else if (age == 2) {
        	iconUrl = IconFactory.class.getResource("person/man.png");
            //adult man
            } else if (age == 3 || age == 4 || age == 5) {
        	iconUrl = IconFactory.class.getResource("person/man.png");
            //man without age
            } else if (age == 6) {
        	iconUrl = IconFactory.class.getResource("person/man.png");
            } else {
        	iconUrl = IconFactory.class.getResource("person/man.png");
            }
        }
        //neutral (robot, ...)
        else {
            iconUrl = IconFactory.class.getResource("person/neutral.png");
        }
        ImageIcon voiceIcon = new ImageIcon(iconUrl);
        
        return voiceIcon;
    }
}
