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
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.SwingUI;
import mcp.mobius.opis.swing.panels.timingserver.PanelTimingTileEnts;
import mcp.mobius.opis.swing.widgets.JTableStats;

import javax.swing.JButton;

import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.opis.api.TabPanelRegistrar;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.newtypes.DataTileEntity;
import mcp.mobius.opis.gui.overlay.OverlayMeanTime;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;
import net.minecraft.client.Minecraft;

public class ActionTimingTileEnts implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		PanelTimingTileEnts panel = (PanelTimingTileEnts)TabPanelRegistrar.INSTANCE.getTab(SelectedTab.TIMINGTILEENTS);
		
		JTableStats table       = panel.getTable();
		if (table == null || table.getSelectedRow() == -1) return;
		int indexData           = table.convertRowIndexToModel(table.getSelectedRow());
		DataTileEntity data    = (DataTileEntity)table.getTableData().get(indexData);
		
		if (e.getSource() == panel.getBtnCenter()){
            CoordinatesBlock coord = data.pos;
            OverlayMeanTime.instance().setSelectedChunk(coord.dim, coord.x >> 4, coord.z >> 4);
            MwAPI.setCurrentDataProvider(OverlayMeanTime.instance());
            Minecraft.getMinecraft().displayGuiScreen(new MwGui(Mw.instance, coord.dim, coord.x, coord.z));            			
		}				
		
		if (e.getSource() == panel.getBtnTeleport()){
            CoordinatesBlock coord = data.pos;
            modOpis.selectedBlock = coord;
            PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_TELEPORT_BLOCK, coord));
            Minecraft.getMinecraft().setIngameFocus(); 
		}
		
		if (e.getSource() == panel.getBtnReset()){
			modOpis.selectedBlock = null;
		}		
	}

}
