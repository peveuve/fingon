package org.fingon;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorChooserUI;
import javax.swing.plaf.ComponentUI;

import org.fingon.accessibility.AccessibilityRenderer;
import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

/**
 * 
 * @author Paul-Emile
 */
public class FingonColorChooserUI extends ColorChooserUI implements ChangeListener, PropertyChangeListener {
    private static FingonColorChooserUI instance;

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	if (instance == null) {
	    instance = new FingonColorChooserUI();
	}
	return instance;
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	JColorChooser colorChooser = (JColorChooser)c;
	InputMap inputMap = colorChooser.getInputMap();
	String helpKey = UIManager.getString("Fingon.helpKey");
	inputMap.put(KeyStroke.getKeyStroke(helpKey), "FingonUIHelp");
	ActionMap actionMap = colorChooser.getActionMap();
	actionMap.put("FingonUIHelp", AccessibilityRenderer.getInstance());
	ColorSelectionModel colorModel = colorChooser.getSelectionModel();
	if (colorModel != null) {
	    colorModel.addChangeListener(this);
	}
	colorChooser.addPropertyChangeListener(this);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JColorChooser colorChooser = (JColorChooser)c;
	InputMap inputMap = colorChooser.getInputMap();
	String helpKey = UIManager.getString("Fingon.helpKey");
	inputMap.remove(KeyStroke.getKeyStroke(helpKey));
	ActionMap actionMap = colorChooser.getActionMap();
	actionMap.remove("FingonUIHelp");
	ColorSelectionModel colorModel = colorChooser.getSelectionModel();
	if (colorModel != null) {
	    colorModel.removeChangeListener(this);
	}
	colorChooser.removePropertyChangeListener(this);
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
    }

    @Override
    public void stateChanged(ChangeEvent ev) {
	ColorSelectionModel colorModel = (ColorSelectionModel)ev.getSource();
	Color selectedColor = colorModel.getSelectedColor();

	try {
	    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
	    Locale locale = synthesizer.getEngineLocale();
	    ResourceBundle label = ResourceBundle.getBundle("message", locale);
	    String text = null;
	    if (selectedColor.equals(Color.BLACK)) {
    	    	text = label.getString("black");
	    } else if (selectedColor.equals(Color.WHITE)) {
    	    	text = label.getString("white");
	    } else if (selectedColor.equals(Color.RED)) {
    	    	text = label.getString("red");
	    } else if (selectedColor.equals(Color.GREEN)) {
    	    	text = label.getString("green");
	    } else if (selectedColor.equals(Color.BLUE)) {
    	    	text = label.getString("blue");
	    } else if (selectedColor.equals(Color.CYAN)) {
    	    	text = label.getString("cyan");
	    } else if (selectedColor.equals(Color.MAGENTA)) {
    	    	text = label.getString("magenta");
	    } else if (selectedColor.equals(Color.ORANGE)) {
    	    	text = label.getString("orange");
	    } else if (selectedColor.equals(Color.PINK)) {
    	    	text = label.getString("pink");
	    } else if (selectedColor.equals(Color.YELLOW)) {
    	    	text = label.getString("yellow");
	    } else {
    	    	int red = selectedColor.getRed();
    	    	int green = selectedColor.getGreen();
    	    	int blue = selectedColor.getBlue();
    	    	String pattern = label.getString("colorMostly");
    	    	MessageFormat msgFormat = new MessageFormat(pattern);
    	    
    	    	if (red > green && red > blue) {
    	    	    Object[] var = {label.getString("red")};
    	    	    text = msgFormat.format(var);
    	    	} else if (green > red && green > blue) {
    	    	    Object[] var = {label.getString("green")};
    	    	    text = msgFormat.format(var);
    	    	} else if (blue > red && blue > green) {
    	    	    Object[] var = {label.getString("blue")};
    	    	    text = msgFormat.format(var);
    	    	} else if (red == green && red > blue) {
    	    	    Object[] var = {label.getString("yellow")};
    	    	    text = msgFormat.format(var);
    	    	} else if (red > green && red == blue) {
    	    	    Object[] var = {label.getString("magenta")};
    	    	    text = msgFormat.format(var);
    	    	} else if (green == blue && green > red) {
    	    	    Object[] var = {label.getString("cyan")};
    	    	    text = msgFormat.format(var);
    	    	} else if (red == green && red == blue) {
    	    	    text = label.getString("gray");
    	    	}
	    }
	    
	    synthesizer.stop();
	    synthesizer.play(text);
	} catch (SynthesisException e1) {}
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	String propertyName = evt.getPropertyName();
	if (propertyName.equals(JColorChooser.SELECTION_MODEL_PROPERTY)) {
	    ColorSelectionModel newModel = (ColorSelectionModel)evt.getNewValue();
	    if (newModel != null) {
		newModel.addChangeListener(this);
	    }
	    ColorSelectionModel oldModel = (ColorSelectionModel)evt.getOldValue();
	    if (oldModel != null) {
		oldModel.removeChangeListener(this);
	    }
	}
    }
}
