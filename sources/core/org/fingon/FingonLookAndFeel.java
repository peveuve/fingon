package org.fingon;

import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * 
 * @author Paul-Emile
 */
public class FingonLookAndFeel extends LookAndFeel {
    /** UI defaults */
    private FingonUIDefaults uiDefaults;
    
    /**
     * 
     */
    public FingonLookAndFeel() {
	uiDefaults = new FingonUIDefaults();
	uiDefaults.put("TextFieldUI", "org.fingon.FingonTextFieldUI");
	uiDefaults.put("FormattedTextFieldUI", "org.fingon.FingonFormattedTextFieldUI");
	uiDefaults.put("TextAreaUI", "org.fingon.FingonTextAreaUI");
	uiDefaults.put("TextPaneUI", "org.fingon.FingonTextPaneUI");
	uiDefaults.put("EditorPaneUI", "org.fingon.FingonEditorPaneUI");
	UIManager.put("PasswordField.keyTyped", getClass().getResource("Menu popup.wav"));
	uiDefaults.put("PasswordFieldUI", "org.fingon.FingonPasswordFieldUI");
	
	UIManager.put("Button.pressedSound", getClass().getResource("Menu popup.wav"));
	UIManager.put("Button.selectedSound", getClass().getResource("Connect.wav"));
	UIManager.put("Button.unselectedSound", getClass().getResource("Disconnect.wav"));
	uiDefaults.put("ButtonUI", "org.fingon.FingonButtonUI");
	uiDefaults.put("MenuUI", "org.fingon.FingonButtonUI");
	uiDefaults.put("MenuItemUI", "org.fingon.FingonButtonUI");
	uiDefaults.put("RadioButtonMenuItemUI", "org.fingon.FingonButtonUI");
	uiDefaults.put("CheckBoxMenuItemUI", "org.fingon.FingonButtonUI");
	uiDefaults.put("ToggleButtonUI", "org.fingon.FingonButtonUI");
	uiDefaults.put("RadioButtonUI", "org.fingon.FingonButtonUI");
	uiDefaults.put("CheckBoxUI", "org.fingon.FingonButtonUI");

	UIManager.put("ProgressBarUI.backgroundMusic", getClass().getResource("Le seigneur des anneaux - Le retour du roi[1]_chapter60-01.mp3"));
	UIManager.put("ProgressBarUI.intermediateSound", getClass().getResource("Default.wav"));
	UIManager.put("ProgressBarUI.finalSound", getClass().getResource("new Mail.wav"));
	uiDefaults.put("ProgressBarUI", "org.fingon.FingonProgressBarUI");

	UIManager.put("OptionPane.informationSound", getClass().getResource("Balloon.wav"));
	UIManager.put("OptionPane.questionSound", getClass().getResource("question.wav"));
	UIManager.put("OptionPane.warningSound", getClass().getResource("Exclamation.wav"));
	UIManager.put("OptionPane.errorSound", getClass().getResource("error.wav"));
	uiDefaults.put("OptionPaneUI", "org.fingon.FingonOptionPaneUI");

	uiDefaults.put("ToolTipUI", "org.fingon.FingonToolTipUI");
	
	uiDefaults.put("ListUI", "org.fingon.FingonListUI");

	uiDefaults.put("TableUI", "org.fingon.FingonTableUI");

	uiDefaults.put("SliderUI", "org.fingon.FingonSliderUI");

	UIManager.put("Tree.expandedSound", getClass().getResource("login.wav"));
	UIManager.put("Tree.collapsedSound", getClass().getResource("logout.wav"));
	uiDefaults.put("TreeUI", "org.fingon.FingonTreeUI");
    }

    /**
     * @see javax.swing.LookAndFeel#getDescription()
     */
    @Override
    public String getDescription() {
	return "Fingon is an auxiliary look and feel which plugs a sound and feel in the current look and feel";
    }

    /**
     * @see javax.swing.LookAndFeel#getID()
     */
    @Override
    public String getID() {
	return "Fingon";
    }

    /**
     * @see javax.swing.LookAndFeel#getName()
     */
    @Override
    public String getName() {
	return "Fingon";
    }

    /**
     * @see javax.swing.LookAndFeel#isNativeLookAndFeel()
     */
    @Override
    public boolean isNativeLookAndFeel() {
	return false;
    }

    /**
     * @see javax.swing.LookAndFeel#isSupportedLookAndFeel()
     */
    @Override
    public boolean isSupportedLookAndFeel() {
	return true;
    }

    /**
     * @see javax.swing.LookAndFeel#getDefaults()
     */
    @Override
    public UIDefaults getDefaults() {
	return uiDefaults;
    }
}
