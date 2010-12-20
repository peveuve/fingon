package org.fingon;

import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TableUI;
import javax.swing.table.TableModel;

import org.fingon.accessibility.AccessibilityRenderer;
import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

public class FingonTableUI extends TableUI implements ListSelectionListener, PropertyChangeListener {

    private JTable table;
    
    public FingonTableUI(JTable c) {
	this.table = c;
    }

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	return new FingonTableUI((JTable)c);
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	JTable table = (JTable)c;
	InputMap inputMap = table.getInputMap();
	String helpKey = UIManager.getString("Fingon.helpKey");
	inputMap.put(KeyStroke.getKeyStroke(helpKey), "FingonUIHelp");
	ActionMap actionMap = table.getActionMap();
	actionMap.put("FingonUIHelp", AccessibilityRenderer.getInstance());
	table.addFocusListener(AccessibilityRenderer.getInstance());
	table.addPropertyChangeListener(this);
	ListSelectionModel selectionModel = table.getSelectionModel();
	if (selectionModel != null) {
	    selectionModel.addListSelectionListener(this);
	}
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JTable table = (JTable)c;
	InputMap inputMap = table.getInputMap();
	String helpKey = UIManager.getString("Fingon.helpKey");
	inputMap.remove(KeyStroke.getKeyStroke(helpKey));
	ActionMap actionMap = table.getActionMap();
	actionMap.remove("FingonUIHelp");
	table.removeFocusListener(AccessibilityRenderer.getInstance());
	table.removePropertyChangeListener(this);
	ListSelectionModel selectionModel = table.getSelectionModel();
	if (selectionModel != null) {
	    selectionModel.removeListSelectionListener(this);
	}
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
    }

    /**
     * 
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent e) {
	if (table.isShowing()) {
	    if (!e.getValueIsAdjusting()) {
    	    	int selectedRowIndex = table.getSelectedRow();
    	    	if (selectedRowIndex != -1) {
    	    	    int selectedColumnIndex = table.getSelectedColumn();
    	    	    if (selectedColumnIndex == -1) {
    	    		selectedColumnIndex = 0;
    	    	    }
    	    	    TableModel model = table.getModel();
    	    	    Object cell = model.getValueAt(selectedRowIndex, selectedColumnIndex);
    	    	    if (cell != null) {
    	    		try {
    	    		    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
    	    		    synthesizer.stop();
    	    		    synthesizer.load(table.getLocale());
    	    		    synthesizer.play(cell.toString());
    	    		} catch (SynthesisException e1) {}
    	    	    }
    	    	}
	    }
	}
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	String propertyName = evt.getPropertyName();
	if (propertyName.equals("selectionModel")) {
	    ListSelectionModel newModel = (ListSelectionModel)evt.getNewValue();
	    if (newModel != null) {
		newModel.addListSelectionListener(this);
	    }
	    ListSelectionModel oldModel = (ListSelectionModel)evt.getOldValue();
	    if (oldModel != null) {
		oldModel.removeListSelectionListener(this);
	    }
	}
    }
}
