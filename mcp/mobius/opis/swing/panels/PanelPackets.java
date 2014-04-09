package mcp.mobius.opis.swing.panels;

import javax.swing.JPanel;

import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.data.holders.newtypes.DataPacket;
import mcp.mobius.opis.data.holders.newtypes.DataPacket250;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataRaw;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;
import net.miginfocom.swing.MigLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTabbedPane;

public class PanelPackets extends JPanelMsgHandler implements ITabPanel {
	private JTableStats tableOutbound;
	private JTableStats tableInbound;
	private JTableStats tablePacket250Outbound;
	private JTableStats tablePacket250Inbound;

	public PanelPackets() {
		setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, "cell 0 0,grow");
		
		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab("Outbound", null, scrollPane, null);
		
		tableOutbound = new JTableStats();
		tableOutbound.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
			},
			new String[] {
				"Type", "ID", "Amount", "Total Size"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Integer.class, Long.class, Long.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tableOutbound.setAutoCreateRowSorter(true);	
		scrollPane.setViewportView(tableOutbound);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		tabbedPane.addTab("Inbound", null, scrollPane_1, null);
		
		tableInbound = new JTableStats();
		tableInbound.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null, null},
				},
				new String[] {
					"Type", "ID", "Amount", "Total Size"
				}
			) {
				Class[] columnTypes = new Class[] {
					String.class, Integer.class, Long.class, Long.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
		tableInbound.setAutoCreateRowSorter(true);			
		scrollPane_1.setViewportView(tableInbound);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		tabbedPane.addTab("Outbound 250", null, scrollPane_2, null);
		
		tablePacket250Outbound = new JTableStats();
		tablePacket250Outbound.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null},
			},
			new String[] {
				"Channel", "Amount", "Total Size"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Integer.class, Integer.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tablePacket250Outbound.setAutoCreateRowSorter(true);	
		scrollPane_2.setViewportView(tablePacket250Outbound);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		tabbedPane.addTab("Inbound 250", null, scrollPane_3, null);
		
		tablePacket250Inbound = new JTableStats();
		tablePacket250Inbound.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null},
				},
				new String[] {
					"Channel", "Amount", "Total Size"
				}
			) {
				Class[] columnTypes = new Class[] {
					String.class, Integer.class, Integer.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
		tablePacket250Inbound.setAutoCreateRowSorter(true);		
		scrollPane_3.setViewportView(tablePacket250Inbound);
		
	}

	@Override
	public String getTabTitle() { return "Packets";	}
	@Override
	public String getTabRefName() {	return "opis.packets";	}

	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {
		switch(msg){
		case LIST_PACKETS_OUTBOUND:{
			
			((JTableStats)this.getTableOutbound()).setTableData(rawdata.array);
			
			DefaultTableModel model = (DefaultTableModel)this.getTableOutbound().getModel();
			int               row   = this.updateData(tableOutbound, model, DataPacket.class);

			for (Object o : rawdata.array){
				DataPacket packet = (DataPacket)o;
				if (packet.type.equals("<UNUSED>")) continue;
				model.addRow(new Object[]  {
					packet.type,
					packet.id,
					packet.amount,
					packet.size  
					 });
			}
			
			this.dataUpdated(tableOutbound, model, row);			
			
			break;
		}
		
		case LIST_PACKETS_INBOUND:{
			
			((JTableStats)this.getTableInbound()).setTableData(rawdata.array);
			
			DefaultTableModel model = (DefaultTableModel)this.getTableInbound().getModel();
			int               row   = this.updateData(tableInbound, model, DataPacket.class);

			for (Object o : rawdata.array){
				DataPacket packet = (DataPacket)o;
				if (packet.type.equals("<UNUSED>")) continue;
				model.addRow(new Object[]  {
					packet.type,
					packet.id,
					packet.amount,
					packet.size  
					 });
			}
			
			this.dataUpdated(tableInbound, model, row);			
			
			break;
		}		
		
		case LIST_PACKETS_OUTBOUND_250:{
			
			((JTableStats)this.getTablePacket250Outbound()).setTableData(rawdata.array);
			
			DefaultTableModel model = (DefaultTableModel)this.getTablePacket250Outbound().getModel();
			int               row   = this.updateData(tablePacket250Outbound, model, DataPacket.class);

			for (Object o : rawdata.array){
				DataPacket250 packet = (DataPacket250)o;
				model.addRow(new Object[]  {
					packet.channel,
					packet.amount,
					packet.size  
					 });
			}
			
			this.dataUpdated(tablePacket250Outbound, model, row);			
			
			break;
		}		
		
		case LIST_PACKETS_INBOUND_250:{
			
			((JTableStats)this.getTablePacket250Inbound()).setTableData(rawdata.array);
			
			DefaultTableModel model = (DefaultTableModel)this.getTablePacket250Inbound().getModel();
			int               row   = this.updateData(tablePacket250Inbound, model, DataPacket.class);

			for (Object o : rawdata.array){
				DataPacket250 packet = (DataPacket250)o;
				model.addRow(new Object[]  {
					packet.channel,
					packet.amount,
					packet.size  
					 });
			}
			
			this.dataUpdated(tablePacket250Inbound, model, row);			
			
			break;
		}			
		
		default:
			return false;
			
		}
		return true;
	}
	public JTableStats getTablePacket250Outbound() {
		return tablePacket250Outbound;
	}
	public JTableStats getTableInbound() {
		return tableInbound;
	}
	public JTableStats getTableOutbound() {
		return tableOutbound;
	}
	public JTable getTablePacket250Inbound() {
		return tablePacket250Inbound;
	}
	
	@Override
	public Message getFocusMessage() {
		return Message.SWING_TAB_PACKETS;
	}		
}
