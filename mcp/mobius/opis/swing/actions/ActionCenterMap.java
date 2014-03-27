package mcp.mobius.opis.swing.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import net.minecraft.client.Minecraft;
import mapwriter.Mw;
import mapwriter.api.IMwDataProvider;
import mapwriter.api.MwAPI;
import mapwriter.gui.MwGui;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;

public class ActionCenterMap implements ActionListener {

	IMwDataProvider overlay;
	
	public ActionCenterMap(IMwDataProvider overlay){
		super();
		this.overlay = overlay;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JTableStats table       = ((JPanelMsgHandler)((JButton)e.getSource()).getParent()).getTable();
		int indexData           = table.convertRowIndexToModel(table.getSelectedRow());
		StatAbstract data       = table.getStatistics().get(indexData);
		CoordinatesBlock target = data.getTeleportTarget();
		
		MwAPI.setCurrentDataProvider(overlay);
		Minecraft.getMinecraft().displayGuiScreen(new MwGui(Mw.instance, target.dim, target.x, target.z));
	}

}
