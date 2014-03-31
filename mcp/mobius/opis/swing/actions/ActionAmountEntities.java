package mcp.mobius.opis.swing.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.Packet_ReqData;
import mcp.mobius.opis.swing.SwingUI;
import mcp.mobius.opis.swing.panels.PanelAmountEntities;
import mcp.mobius.opis.swing.widgets.JTableStats;

import javax.swing.JButton;

import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.opis.api.TabPanelRegistrar;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.SerialString;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;

public class ActionAmountEntities implements ActionListener, ItemListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		PanelAmountEntities panel = (PanelAmountEntities)TabPanelRegistrar.INSTANCE.getTab("opis.amountents");
		
		
		JTableStats table       = panel.getTable();
		if (table == null || table.getSelectedRow() == -1) return;
		int indexData           = table.convertRowIndexToModel(table.getSelectedRow());
		AmountHolder data       = (AmountHolder)table.getStatistics().get(indexData);
		
		if (e.getSource() == panel.getBtnKillAll()){
            PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_KILLALL, new SerialString(data.key)));
            PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.LIST_AMOUNT_ENTITIES));			
		}				
		
		if (e.getSource() == panel.getBtnRefresh()){
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.LIST_AMOUNT_ENTITIES));
		}
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
        if      (e.getStateChange() == ItemEvent.SELECTED){
            PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_FILTERING_TRUE));
        }
        else if (e.getStateChange() == ItemEvent.DESELECTED){
            PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_FILTERING_FALSE));
        }
        PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.LIST_AMOUNT_ENTITIES));
	}

}
