package org.fingon;

import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.accessibility.AccessibleContext;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.fingon.accessibility.AccessibilityRenderer;
import org.fingon.synthesizer.SpeechSynthesizer;
import org.fingon.synthesizer.SpeechSynthesizerFactory;
import org.fingon.synthesizer.SynthesisException;

public class FingonTableHeaderUI extends TableHeaderUI implements TableColumnModelListener, FocusListener {
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
	InputMap inputMap = tableHeader.getInputMap();
	inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "FingonUIHelp");
	ActionMap actionMap = tableHeader.getActionMap();
	actionMap.put("FingonUIHelp", AccessibilityRenderer.getInstance());
	tableHeader.addFocusListener(this);
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
	InputMap inputMap = tableHeader.getInputMap();
	inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
	ActionMap actionMap = tableHeader.getActionMap();
	actionMap.remove("FingonUIHelp");
	tableHeader.removeFocusListener(this);
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
	Object[] columnAddedMsgArgs = {headerValue};
	try {
	    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();

	    ResourceBundle msg = ResourceBundle.getBundle("message", synthesizer.getEngineLocale());
	    String columnAddedMsgPattern = msg.getString("columnAdded");
	    MessageFormat columnAddedMsgFormat = new MessageFormat(columnAddedMsgPattern);
	    String columnAddedMsg = columnAddedMsgFormat.format(columnAddedMsgArgs);
	    
	    synthesizer.stop();
	    synthesizer.play(columnAddedMsg);
	} catch (SynthesisException ex) {}
    }

    @Override
    public void columnMoved(TableColumnModelEvent e) {
	TableColumnModel model = (TableColumnModel)e.getSource();
	int toIndex = e.getToIndex();
	int fromIndex = e.getFromIndex();
	if (fromIndex != toIndex) {
	    try {
    	    	SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
    	    	
    	    	ResourceBundle msg = ResourceBundle.getBundle("message", synthesizer.getEngineLocale());
    	    	TableColumn column = model.getColumn(toIndex);
    	    	TableColumn refColumn = null;
    	    	String columnMovedMsgPattern = null;
    	    	if (toIndex-1 >= 0) {
    	    	    refColumn = model.getColumn(toIndex-1);
    	    	    columnMovedMsgPattern = msg.getString("columnMovedAfter");
    	    	} else {
    	    	    refColumn = model.getColumn(toIndex+1);
    	    	    columnMovedMsgPattern = msg.getString("columnMovedBefore");
    	    	}
    	    	Object refHeaderValue = refColumn.getHeaderValue();
    	    	Object headerValue = column.getHeaderValue();
    	    	Object[] columnMovedMsgArgs = {headerValue, refHeaderValue};
    	    	MessageFormat columnMovedMsgFormat = new MessageFormat(columnMovedMsgPattern);
    	    	String columnMovedMsg = columnMovedMsgFormat.format(columnMovedMsgArgs);
    	    	
    	    	synthesizer.stop();
    	    	synthesizer.play(columnMovedMsg);
	    } catch (SynthesisException ex) {}
	}
    }

    @Override
    public void columnRemoved(TableColumnModelEvent e) {
	int fromIndex = e.getFromIndex();
	Object[] columnRemovedMsgArgs = {fromIndex};
	try {
	    SpeechSynthesizer synthesizer = SpeechSynthesizerFactory.getSpeechSynthesizer();
	    
	    ResourceBundle msg = ResourceBundle.getBundle("message", synthesizer.getEngineLocale());
	    String columnRemovedMsgPattern = msg.getString("columnRemoved");
	    MessageFormat columnRemovedMsgFormat = new MessageFormat(columnRemovedMsgPattern);
	    String columnRemovedMsg = columnRemovedMsgFormat.format(columnRemovedMsgArgs);
	    
	    synthesizer.stop();
	    synthesizer.play(columnRemovedMsg);
	} catch (SynthesisException ex) {}
    }

    @Override
    public void columnMarginChanged(ChangeEvent e) {
    }

    @Override
    public void columnSelectionChanged(ListSelectionEvent e) {
    }

    @Override
    public void focusGained(FocusEvent e) {
	AccessibleContext accessCtxt = e.getComponent().getAccessibleContext();
	AccessibilityRenderer.getInstance().renderSummary(accessCtxt);
    }

    @Override
    public void focusLost(FocusEvent e) {
    }
}
