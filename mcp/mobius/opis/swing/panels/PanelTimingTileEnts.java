package mcp.mobius.opis.swing.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import mcp.mobius.opis.data.holders.basetypes.SerialDouble;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.helpers.ModIdentification;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataRaw;
import mcp.mobius.opis.swing.widgets.JButtonAccess;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import net.miginfocom.swing.MigLayout;
import net.minecraft.item.ItemStack;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PanelTimingTileEnts extends JPanelMsgHandler implements ActionListener{
	private JTable table;
	private JButtonAccess btnCenter;
	private JButtonAccess btnTeleport;
	private JButtonAccess btnReset;
	private JButtonAccess btnRun;
	private JLabel lblSummary;

	/**
	 * Create the panel.
	 */
	public PanelTimingTileEnts() {
		setLayout(new MigLayout("", "[][][][grow][]", "[][grow][]"));
		
		btnCenter = new JButtonAccess("Center Map", AccessLevel.NONE);
		add(btnCenter, "cell 0 0");
		btnCenter.addActionListener(this);
		
		btnTeleport = new JButtonAccess("Teleport", AccessLevel.PRIVILEGED);
		add(btnTeleport, "cell 1 0");
		btnTeleport.addActionListener(this);
		
		btnReset = new JButtonAccess("Reset Highlight", AccessLevel.PRIVILEGED);
		btnReset.setEnabled(false);
		add(btnReset, "cell 2 0");
		btnReset.addActionListener(this);
		
		btnRun = new JButtonAccess("Run Opis", AccessLevel.PRIVILEGED);
		add(btnRun, "cell 4 0");
		btnRun.addActionListener(this);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1 5 1,grow");
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Type", "Mod", "Dim", "Pos", "Update Time"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, Integer.class, Object.class, StatAbstract.class
			};
			boolean[] columnEditables = new boolean[] {
					false, false, false, false, false
			};			
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.setAutoCreateRowSorter(true);		
		scrollPane.setViewportView(table);
		
		lblSummary = new JLabel("New label");
		add(lblSummary, "cell 0 2 5 1,alignx center");
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );		
		
		for (int i = 0; i < table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);			

	}

	public JButton getBtnCenter()  {return btnCenter;}
	public JButton getBtnTeleport(){return btnTeleport;}
	public JButton getBtnReset()   {return btnReset;}
	public JButton getBtnRun()     {return btnRun;}
	public JTable  getTable()      {return table;}
	public JLabel  getLblSummary() {return lblSummary;}

	@Override
	public void actionPerformed(ActionEvent e) {}

	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {
		switch(msg){
		case LIST_TIMING_TILEENTS:{
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			int               row   = this.updateData(table, model, StatsTileEntity.class);

			for (Object o : rawdata.array){
				StatsTileEntity stat = (StatsTileEntity)o;
				ItemStack is;
				String name  = String.format("te.%d.%d", stat.getID(), stat.getMeta());
				String modID = "<UNKNOWN>";
				
				try{
					is = new ItemStack(stat.getID(), 1, stat.getMeta());
					name  = is.getDisplayName();
					modID = ModIdentification.idFromStack(is);
				}  catch (Exception e) {	}			
				
				model.addRow(new Object[]  {
						 name,
						 modID,
					     stat.getCoordinates().dim,
					     String.format("[ %4d %4d %4d ]", 	stat.getCoordinates().x, stat.getCoordinates().y, stat.getCoordinates().z),  
					     stat});
			}
			
			this.dataUpdated(table, model, row);			
			
			break;
		}
		case VALUE_TIMING_TILEENTS:{
			this.getLblSummary().setText(String.format("Total update time : %.3f µs", ((SerialDouble)rawdata.value).value));	
			break;
		}
		case STATUS_START:{
			this.getBtnRun().setText("Running...");
			break;
		}
		case STATUS_STOP:{
			this.getBtnRun().setText("Run Opis");
			break;
		}		
		case STATUS_RUNNING:{
			this.getBtnRun().setText("Running...");
			break;
		}		
		default:
			return false;
			
		}
		return true;
	}
}
