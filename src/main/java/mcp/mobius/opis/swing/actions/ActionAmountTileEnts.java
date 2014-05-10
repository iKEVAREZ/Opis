package mcp.mobius.opis.swing.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.PacketReqData;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.SwingUI;
import mcp.mobius.opis.swing.panels.tracking.PanelAmountEntities;
import mcp.mobius.opis.swing.panels.tracking.PanelAmountTileEnts;
import mcp.mobius.opis.swing.widgets.JTableStats;

import javax.swing.JButton;

import mcp.mobius.opis.api.TabPanelRegistrar;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.SerialString;
import mcp.mobius.opis.data.holders.newtypes.DataBlockTileEntityPerClass;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;

public class ActionAmountTileEnts implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		PanelAmountTileEnts panel = (PanelAmountTileEnts)TabPanelRegistrar.INSTANCE.getTab(SelectedTab.AMOUNTTES);

		/*
		JTableStats table       = panel.getTable();
		if (table == null || table.getSelectedRow() == -1) return;
		int indexData           = table.convertRowIndexToModel(table.getSelectedRow());
		DataBlockTileEntityPerClass data       = (DataBlockTileEntityPerClass)table.getTableData().get(indexData);
		*/
		
		if (e.getSource() == panel.getBtnRefresh()){
			PacketManager.sendToServer(new PacketReqData(Message.LIST_AMOUNT_TILEENTS));
		}
		
	}
}
