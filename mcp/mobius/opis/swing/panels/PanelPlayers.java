package mcp.mobius.opis.swing.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.swing.widgets.JButtonAccess;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PanelPlayers extends JPanel implements ActionListener{
	private JTable table;
	private JButtonAccess btnCenter;
	private JButtonAccess btnTeleport;
	private JButtonAccess btnPull;

	/**
	 * Create the panel.
	 */
	public PanelPlayers() {
		setLayout(new MigLayout("", "[][][][][][grow]", "[][grow]"));
		
		btnCenter = new JButtonAccess("Center Map", AccessLevel.NONE);
		add(btnCenter, "cell 0 0");
		btnCenter.addActionListener(this);
		
		btnTeleport = new JButtonAccess("Teleport", AccessLevel.PRIVILEGED);
		add(btnTeleport, "cell 1 0");
		btnTeleport.addActionListener(this);
		
		btnPull = new JButtonAccess("Pull", AccessLevel.PRIVILEGED);
		add(btnPull, "cell 2 0");
		btnPull.addActionListener(this);
		
		JButton btnKill = new JButton("Kill");
		btnKill.setEnabled(false);
		add(btnKill, "cell 3 0");
		
		JButton btnMorph = new JButton("Morph");
		btnMorph.setEnabled(false);
		add(btnMorph, "cell 4 0");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1 6 1,grow");
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Name", "Dimension", "Coordinates"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Integer.class, Object.class
			};
			
			boolean[] columnEditables = new boolean[] {
					false, false, false
			};			
			
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.setAutoCreateRowSorter(true);		
		scrollPane.setViewportView(table);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );		
		
		for (int i = 0; i < table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);			
	}

	public JButton getBtnCenter()   {return btnCenter;}
	public JButton getBtnTeleport() {return btnTeleport;}
	public JButton getBtnPull()     {return btnPull;}
	public JTable getTable()        {return table;}

	@Override
	public void actionPerformed(ActionEvent e) {}
}
