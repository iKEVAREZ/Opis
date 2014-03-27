package mcp.mobius.opis.swing.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.Packet_ReqData;
import mcp.mobius.opis.swing.widgets.JTableStats;

import javax.swing.JButton;

import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;


public class ActionTeleport implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		JTableStats table       = ((JPanelMsgHandler)((JButton)e.getSource()).getParent()).getTable();
		int indexData           = table.convertRowIndexToModel(table.getSelectedRow());
		StatAbstract data       = table.getStatistics().get(indexData);
		CoordinatesBlock target = data.getTeleportTarget();

		PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_TELEPORT_BLOCK, target));
	}

}
