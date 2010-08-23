package org.fingon.synthesizer.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.fingon.IconFactory;
import org.fingon.synthesizer.VoiceDesc;


/**
 * @author Paul-Emile
 * 
 */
public class VoiceListCellRenderer extends JLabel implements ListCellRenderer {
    /** VoiceListCellRenderer.java long */
    private static final long serialVersionUID = 6069888711938049086L;

    /**
     * 
     */
    public VoiceListCellRenderer() {
        super();
    }

    /**
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            VoiceDesc voice = (VoiceDesc)value;
            String name = voice.getName();
            if (name.length() > 35) {
        	name = name.substring(0, 33);
        	name += "...";
            }
            setText(name);
            setIcon(IconFactory.getIconByVoice(voice));
        }
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        }
        else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }

}
