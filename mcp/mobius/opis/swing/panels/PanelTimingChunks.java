package mcp.mobius.opis.swing.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import mapwriter.Mw;
import mapwriter.api.MwAPI;
import mapwriter.gui.MwGui;
import mcp.mobius.opis.data.client.DataCache;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.gui.overlay.OverlayMeanTime;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.Packet_ReqData;
import mcp.mobius.opis.network.packets.server.NetDataRaw;
import mcp.mobius.opis.swing.widgets.JButtonAccess;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import net.miginfocom.swing.MigLayout;
import net.minecraft.client.Minecraft;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import cpw.mods.fml.common.network.PacketDispatcher;

public class PanelTimingChunks extends JPanelMsgHandler implements ActionListener{
	private JTable table;
	private JButtonAccess btnRun;
	private JButtonAccess btnTeleport;
	private JButtonAccess btnCenter;

	/**
	 * Create the panel.
	 */
	public PanelTimingChunks() {
		setLayout(new MigLayout("", "[][][grow][]", "[][grow]"));
		
		btnCenter = new JButtonAccess("Center Map", AccessLevel.NONE);
		add(btnCenter, "cell 0 0");
		btnCenter.addActionListener(this);
		
		btnTeleport = new JButtonAccess("Teleport", AccessLevel.PRIVILEGED);
		add(btnTeleport, "cell 1 0");
		btnTeleport.addActionListener(this);
		
		btnRun = new JButtonAccess("Run Opis", AccessLevel.PRIVILEGED);
		add(btnRun, "cell 3 0");
		btnRun.addActionListener(this);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1 4 1,grow");
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Dimension", "Position", "TileEntities", "Entities", "Update Time"
				}
			) {
				Class[] columnTypes = new Class[] {
					Integer.class, String.class, Integer.class, Integer.class, StatAbstract.class
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

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );		
		
		for (int i = 0; i < table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);		
		
	}

	public JTable  getTable()  {return table;}
	public JButton getBtnRun() {return btnRun;}
	public JButton getBtnTeleport() {return btnTeleport;}
	public JButton getBtnCenter()   {return btnCenter;}

	@Override
	public void actionPerformed(ActionEvent e) {
		// RUN OPIS Button
		//if (e.getSource() == this.getBtnRun()){
		//	PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_START));
		//}
		
		// TELEPORT Button on the TimingChunk Screen
		//else if ((e.getSource() == this.getBtnTeleport()) && (this.getTable().getSelectedRow() != -1)){
		//	int indexData = this.getTable().convertRowIndexToModel(this.getTable().getSelectedRow());
		//	StatsChunk data = DataCache.instance().getTimingChunks().get(indexData);
		//	PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_TELEPORT_CHUNK, data.getChunk()));
		//}

		// CENTER Button on the TimingChunk Screen
		//else if ((e.getSource() == this.getBtnCenter()) && (this.getTable().getSelectedRow() != -1)){
		//	int indexData = this.getTable().convertRowIndexToModel(this.getTable().getSelectedRow());
		//	StatsChunk data = DataCache.instance().getTimingChunks().get(indexData);
			
		//	OverlayMeanTime.instance().setSelectedChunk(data.getChunk().dim, data.getChunk().chunkX, data.getChunk().chunkZ);
		//	MwAPI.setCurrentDataProvider(OverlayMeanTime.instance());
		//	Minecraft.getMinecraft().displayGuiScreen(new MwGui(Mw.instance, data.getChunk().dim, data.getChunk().x + 8, data.getChunk().z + 8));			
		//}		
	}

	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {
		switch(msg){
		case LIST_TIMING_CHUNK:{
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			int               row   = this.updateData(table, model, StatsChunk.class);		
			
			for (Object o : rawdata.array){
				StatsChunk stat = (StatsChunk)o;
				model.addRow(new Object[]  {
						 stat.getChunk().dim,
						 String.format("[ %4d %4d ]", 	stat.getChunk().chunkX, stat.getChunk().chunkZ),
						 stat.tileEntities,
					     stat.entities,
					     stat});
			}
			
			this.dataUpdated(table, model, row);			
			
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
