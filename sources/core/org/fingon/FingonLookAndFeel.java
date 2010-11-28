package org.fingon;

import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import org.fingon.tray.FingonTrayIcon;

/**
 * The Fingon "sound and feel".
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
	UIManager.put("PasswordField.keyTypedSound", getClass().getResource("Menu_popup.wav"));
	uiDefaults.put("PasswordFieldUI", "org.fingon.FingonPasswordFieldUI");
	
	UIManager.put("Button.pressedSound", getClass().getResource("Menu_popup.wav"));
	UIManager.put("Button.selectedSound", getClass().getResource("Connect.wav"));
	UIManager.put("Button.unselectedSound", getClass().getResource("Disconnect.wav"));
	uiDefaults.put("ButtonUI", "org.fingon.FingonButtonUI");
	uiDefaults.put("ToggleButtonUI", "org.fingon.FingonButtonUI");
	uiDefaults.put("RadioButtonUI", "org.fingon.FingonButtonUI");
	uiDefaults.put("CheckBoxUI", "org.fingon.FingonButtonUI");
	uiDefaults.put("MenuUI", "org.fingon.FingonMenuItemUI");
	uiDefaults.put("MenuItemUI", "org.fingon.FingonMenuItemUI");
	uiDefaults.put("RadioButtonMenuItemUI", "org.fingon.FingonMenuItemUI");
	uiDefaults.put("CheckBoxMenuItemUI", "org.fingon.FingonMenuItemUI");

	UIManager.put("ProgressBar.backgroundMusic", getClass().getResource("Ground_Control_Human_Space_Pod.mp3"));
	UIManager.put("ProgressBar.intermediateSound", getClass().getResource("Default.wav"));
	UIManager.put("ProgressBar.finalSound", getClass().getResource("new_Mail.wav"));
	uiDefaults.put("ProgressBarUI", "org.fingon.FingonProgressBarUI");

	UIManager.put("OptionPane.informationSound", getClass().getResource("Balloon.wav"));
	UIManager.put("OptionPane.questionSound", getClass().getResource("question.wav"));
	UIManager.put("OptionPane.warningSound", getClass().getResource("Exclamation.wav"));
	UIManager.put("OptionPane.errorSound", getClass().getResource("error.wav"));
	uiDefaults.put("OptionPaneUI", "org.fingon.FingonOptionPaneUI");

	uiDefaults.put("ToolTipUI", "org.fingon.FingonToolTipUI");
	
	uiDefaults.put("ListUI", "org.fingon.FingonListUI");

	uiDefaults.put("ComboBoxUI", "org.fingon.FingonComboBoxUI");

	uiDefaults.put("TabbedPaneUI", "org.fingon.FingonTabbedPaneUI");

	uiDefaults.put("TableUI", "org.fingon.FingonTableUI");
	uiDefaults.put("TableHeaderUI", "org.fingon.FingonTableHeaderUI");

	uiDefaults.put("SpinnerUI", "org.fingon.FingonSpinnerUI");

	UIManager.put("Slider.instrument", Integer.valueOf(98));
	uiDefaults.put("SliderUI", "org.fingon.FingonSliderUI");

	UIManager.put("ScrollBar.instrument", Integer.valueOf(103));
	uiDefaults.put("ScrollBarUI", "org.fingon.FingonScrollBarUI");

	uiDefaults.put("ColorChooserUI", "org.fingon.FingonColorChooserUI");

	UIManager.put("Tree.expandedSound", getClass().getResource("login.wav"));
	UIManager.put("Tree.collapsedSound", getClass().getResource("logout.wav"));
	uiDefaults.put("TreeUI", "org.fingon.FingonTreeUI");

	UIManager.put("InternalFrame.openedSound", getClass().getResource("login.wav"));
	UIManager.put("InternalFrame.closedSound", getClass().getResource("logout.wav"));
	UIManager.put("InternalFrame.iconifiedSound", getClass().getResource("Connect.wav"));
	UIManager.put("InternalFrame.deiconifiedSound", getClass().getResource("Disconnect.wav"));
	uiDefaults.put("InternalFrameUI", "org.fingon.FingonInternalFrameUI");
	
	new FingonTrayIcon();
    }

    /**
     * @see javax.swing.LookAndFeel#getDescription()
     */
    @Override
    public String getDescription() {
	return "Fingon is an auxiliary look and feel which plugs an auditory user interface to your application";
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
