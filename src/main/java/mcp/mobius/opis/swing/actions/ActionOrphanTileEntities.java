package mcp.mobius.opis.swing.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.opis.api.TabPanelRegistrar;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.Packet_ReqData;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.panels.debug.PanelOrphanTileEntities;
import mcp.mobius.opis.swing.widgets.JTableStats;

public class ActionOrphanTileEntities implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		PanelOrphanTileEntities panel = (PanelOrphanTileEntities)TabPanelRegistrar.INSTANCE.getTab(SelectedTab.ORPHANTES);
		
		if (e.getSource() == panel.getBtnRefresh()){
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.LIST_ORPHAN_TILEENTS));
		}
		
	}	
	
}
