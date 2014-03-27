package mcp.mobius.opis.swing.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.Packet_ReqData;
import mcp.mobius.opis.swing.widgets.JTableStats;

import javax.swing.JButton;

import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;

public class ActionTimingChunks implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		JTableStats table       = ((JPanelMsgHandler)((JButton)e.getSource()).getParent()).getTable();
		if (table == null || table.getSelectedRow() == -1) return;
		int indexData           = table.convertRowIndexToModel(table.getSelectedRow());
		StatAbstract data       = table.getStatistics().get(indexData);

	}

}
