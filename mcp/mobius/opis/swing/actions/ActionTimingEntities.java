package mcp.mobius.opis.swing.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import mapwriter.Mw;
import mapwriter.api.MwAPI;
import mapwriter.gui.MwGui;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.Packet_ReqData;
import mcp.mobius.opis.swing.SwingUI;
import mcp.mobius.opis.swing.widgets.JTableStats;

import javax.swing.JButton;

import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.TargetEntity;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
import mcp.mobius.opis.gui.overlay.entperchunk.OverlayEntityPerChunk;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;
import net.minecraft.client.Minecraft;

public class ActionTimingEntities implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		JTableStats table       = SwingUI.instance().getPanelTimingEntities().getTable();
		if (table == null || table.getSelectedRow() == -1) return;
		int indexData           = table.convertRowIndexToModel(table.getSelectedRow());
		StatsEntity data        = (StatsEntity)table.getStatistics().get(indexData);

		if (e.getSource() == SwingUI.instance().getPanelTimingEntities().getBtnCenter()){
            CoordinatesBlock coord = data.getCoordinates();
            PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.OVERLAY_CHUNK_ENTITIES));
            PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.LIST_CHUNK_ENTITIES, data.getChunk()));           
            MwAPI.setCurrentDataProvider(OverlayEntityPerChunk.instance());
            OverlayEntityPerChunk.instance().selectedChunk = coord.asCoordinatesChunk();
            Minecraft.getMinecraft().displayGuiScreen(new MwGui(Mw.instance, coord.dim, coord.x, coord.z));              			
		}				
		
		if (e.getSource() == SwingUI.instance().getPanelTimingEntities().getBtnTeleport()){
            int eid = data.getID();
            int dim = data.getCoordinates().dim;
            PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_TELEPORT_TO_ENTITY, new TargetEntity(eid, dim)));
            Minecraft.getMinecraft().setIngameFocus(); 
		}
		
		if (e.getSource() == SwingUI.instance().getPanelTimingEntities().getBtnPull()){
            int eid = data.getID();
            int dim = data.getCoordinates().dim;
            PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_TELEPORT_PULL_ENTITY, new TargetEntity(eid, dim)));    	
		}		
		
	}

}
