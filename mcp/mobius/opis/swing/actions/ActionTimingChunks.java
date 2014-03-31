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
import mcp.mobius.opis.swing.panels.PanelTimingChunks;
import mcp.mobius.opis.swing.widgets.JTableStats;

import javax.swing.JButton;

import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.opis.api.TabPanelRegistrar;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.gui.overlay.OverlayMeanTime;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;
import net.minecraft.client.Minecraft;

public class ActionTimingChunks implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		PanelTimingChunks panel = (PanelTimingChunks)TabPanelRegistrar.INSTANCE.getTab("opis.timingchunks");
		
		JTableStats table       = panel.getTable();
		if (table == null || table.getSelectedRow() == -1) return;
		int indexData           = table.convertRowIndexToModel(table.getSelectedRow());
		StatsChunk data         = (StatsChunk)table.getStatistics().get(indexData);

		if (e.getSource() == panel.getBtnCenter()){
            OverlayMeanTime.instance().setSelectedChunk(data.getChunk().dim, data.getChunk().chunkX, data.getChunk().chunkZ);
            MwAPI.setCurrentDataProvider(OverlayMeanTime.instance());
            Minecraft.getMinecraft().displayGuiScreen(new MwGui(Mw.instance, data.getChunk().dim, data.getChunk().x + 8, data.getChunk().z + 8));   			
		}				
		
		if (e.getSource() == panel.getBtnTeleport()){
            PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_TELEPORT_CHUNK, data.getChunk()));
		}
	}
}
