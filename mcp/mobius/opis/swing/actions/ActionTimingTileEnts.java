package mcp.mobius.opis.swing.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import mapwriter.Mw;
import mapwriter.api.MwAPI;
import mapwriter.gui.MwGui;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.Packet_ReqData;
import mcp.mobius.opis.swing.SwingUI;
import mcp.mobius.opis.swing.widgets.JTableStats;

import javax.swing.JButton;

import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.gui.overlay.OverlayMeanTime;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;
import net.minecraft.client.Minecraft;

public class ActionTimingTileEnts implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		JTableStats table       = SwingUI.instance().getPanelTimingTileEnts().getTable();
		if (table == null || table.getSelectedRow() == -1) return;
		int indexData           = table.convertRowIndexToModel(table.getSelectedRow());
		StatsTileEntity data    = (StatsTileEntity)table.getStatistics().get(indexData);
		
		if (e.getSource() == SwingUI.instance().getPanelTimingTileEnts().getBtnCenter()){
            CoordinatesBlock coord = data.getCoordinates();
            OverlayMeanTime.instance().setSelectedChunk(coord.dim, coord.x >> 4, coord.z >> 4);
            MwAPI.setCurrentDataProvider(OverlayMeanTime.instance());
            Minecraft.getMinecraft().displayGuiScreen(new MwGui(Mw.instance, coord.dim, coord.x, coord.z));            			
		}				
		
		if (e.getSource() == SwingUI.instance().getPanelTimingTileEnts().getBtnTeleport()){
            CoordinatesBlock coord = data.getCoordinates();
            modOpis.selectedBlock = coord;
            PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_TELEPORT_BLOCK, coord));
            Minecraft.getMinecraft().setIngameFocus(); 
		}
	}

}
