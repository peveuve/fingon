package org.fingon;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TableUI;
import javax.swing.table.TableModel;

import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

public class FingonTableUI extends TableUI implements ListSelectionListener {

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
	//ListSelectionModel changedSelection = (ListSelectionModel)e.getSource();
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
    	    		    synthesizer.play(cell.toString());
    	    		} catch (SynthesisException e1) {}
    	    	    }
    	    	}
	    }
	}
    }
}
