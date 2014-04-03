package mcp.mobius.opis.swing.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.opis.api.TabPanelRegistrar;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.newtypes.DataDimension;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.Packet_ReqData;
import mcp.mobius.opis.swing.panels.PanelDimensions;
import mcp.mobius.opis.swing.widgets.JTableStats;

public class ActionDimensions implements ActionListener { 

	@Override
	public void actionPerformed(ActionEvent e) {
		PanelDimensions panel = (PanelDimensions)TabPanelRegistrar.INSTANCE.getTab("opis.dimensions");
		
		if (e.getSource() == panel.getBtnPurgeAll()){
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_KILL_HOSTILES_ALL));
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_PURGE_CHUNKS_ALL));
			
		} else {
			JTableStats table       = panel.getTable();
			if (table == null || table.getSelectedRow() == -1) return;
			int indexData           = table.convertRowIndexToModel(table.getSelectedRow());
			DataDimension data         = (DataDimension)table.getTableData().get(indexData);
			
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_KILL_HOSTILES_DIM, new SerialInt(data.dim)));
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_PURGE_CHUNKS_DIM, new SerialInt(data.dim)));			
		}
		
	}

}
