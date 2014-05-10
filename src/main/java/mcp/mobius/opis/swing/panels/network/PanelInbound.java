package mcp.mobius.opis.swing.panels.network;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.data.holders.newtypes.DataAmountRate;
import mcp.mobius.opis.data.holders.newtypes.DataByteRate;
import mcp.mobius.opis.data.holders.newtypes.DataByteSize;
import mcp.mobius.opis.data.holders.newtypes.DataPacket;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PanelInbound extends JPanelMsgHandler implements ITabPanel {

	public PanelInbound() {
		setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 0,grow");
		
		table = new JTableStats(
				new String[] {"Type", "ID", "Amount", "Rate", "Total Size"},
				new Class[]  {String.class, Integer.class, DataAmountRate.class, DataByteRate.class, DataByteSize.class},
				new int[]    {SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.RIGHT, SwingConstants.RIGHT, SwingConstants.RIGHT}
				);			
		scrollPane.setViewportView(table);
	}

	@Override
	public boolean handleMessage(Message msg, PacketBase rawdata) {
		switch(msg){
		case LIST_PACKETS_INBOUND:{
			
			((JTableStats)this.getTable()).setTableData(rawdata.array);
			
			DefaultTableModel model = (DefaultTableModel)this.getTable().getModel();
			int               row   = this.getTable().clearTable(DataPacket.class);

			for (Object o : rawdata.array){
				DataPacket packet = (DataPacket)o;
				if (packet.type.equals("<UNUSED>")) continue;
				model.addRow(new Object[]  {
					packet.type,
					packet.id,
					packet.amount,
					packet.rate,
					packet.size    
					 });
			}
			
			this.getTable().dataUpdated(row);			
			
			break;
		}		
		
		default:
			return false;
			
		}
		return true;
	}

	@Override
	public SelectedTab getSelectedTab() {
		return SelectedTab.PACKETINBOUND;
	}	
	
}
