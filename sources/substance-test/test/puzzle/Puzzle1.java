package test.puzzle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class Puzzle1 {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Puzzle 1");
				frame.setSize(new Dimension(300, 200));
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				final JTable table = new JTable();
				table.getSelectionModel().addListSelectionListener(
						new ListSelectionListener() {
							@Override
							public void valueChanged(ListSelectionEvent e) {
								if (e.getValueIsAdjusting())
									return;

								System.out.println("Table: "
										+ table.getRowCount() + ", model: "
										+ table.getModel().getRowCount());
							}
						});
				table.setModel(new DefaultTableModel(
						new Object[][] { new Object[] { "Steven", 10 } },
						new Object[] { "Name", "Value" }));
				table.setAutoCreateRowSorter(true);
				frame.add(table, BorderLayout.CENTER);

				JPanel controls = new JPanel(
						new FlowLayout(FlowLayout.TRAILING));

				JButton selectAllRowsBtn = new JButton("Select all rows");
				selectAllRowsBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						table.selectAll();
					}
				});
				controls.add(selectAllRowsBtn);

				JButton resetModelBtn = new JButton("Reset model");
				resetModelBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						table.setModel(new DefaultTableModel());
					}
				});
				controls.add(resetModelBtn);

				frame.add(controls, BorderLayout.SOUTH);

				frame.setVisible(true);
			}
		});
	}
}