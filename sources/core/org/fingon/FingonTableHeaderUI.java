package org.fingon;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

public class FingonTableHeaderUI extends TableHeaderUI implements TableColumnModelListener {
    private static FingonTableHeaderUI instance;
    
    public FingonTableHeaderUI(JTableHeader c) {
    }

    /**
     * Returns the instance of UI
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
	if (instance == null) {
	    instance = new FingonTableHeaderUI((JTableHeader)c);
	}
	return instance;
    }
    
    /**
     * @see javax.swing.plaf.ComponentUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c) {
	JTableHeader tableHeader = (JTableHeader)c;
	TableColumnModel columnModel = tableHeader.getColumnModel();
	if (columnModel != null) {
	    columnModel.addColumnModelListener(this);
	}
    }

    /**
     * @see javax.swing.plaf.ComponentUI#uninstallUI(javax.swing.JComponent)
     */
    @Override
    public void uninstallUI(JComponent c) {
	JTableHeader tableHeader = (JTableHeader)c;
	TableColumnModel columnModel = tableHeader.getColumnModel();
	if (columnModel != null) {
	    columnModel.removeColumnModelListener(this);
	}
    }

    /**
     * @see javax.swing.plaf.ComponentUI#update(java.awt.Graphics, javax.swing.JComponent)
     */
    @Override
    public void update(Graphics g, JComponent c) {
    }

    @Override
    public void columnAdded(TableColumnModelEvent e) {
	TableColumnModel model = (TableColumnModel)e.getSource();
	int toIndex = e.getToIndex();
	TableColumn column = model.getColumn(toIndex);
	Object headerValue = column.getHeaderValue();
	try {
	    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
	    synthesizer.stop();
	    synthesizer.play(headerValue.toString() + " column added");
	} catch (SynthesisException ex) {}
    }

    @Override
    public void columnMoved(TableColumnModelEvent e) {
	TableColumnModel model = (TableColumnModel)e.getSource();
	int toIndex = e.getToIndex();
	int fromIndex = e.getFromIndex();
	if (fromIndex != toIndex) {
	    TableColumn column = model.getColumn(toIndex);
	    Object headerValue = column.getHeaderValue();
	    try {
    	    	SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
    	    	synthesizer.stop();
    	    	synthesizer.play(headerValue.toString() + " column moved");
	    } catch (SynthesisException ex) {}
	}
    }

    @Override
    public void columnRemoved(TableColumnModelEvent e) {
	int fromIndex = e.getFromIndex();
	try {
	    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
	    synthesizer.stop();
	    synthesizer.play("column "+fromIndex+" removed");
	} catch (SynthesisException ex) {}
    }

    @Override
    public void columnMarginChanged(ChangeEvent e) {
    }

    @Override
    public void columnSelectionChanged(ListSelectionEvent e) {
    }
}
