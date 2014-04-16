package mcp.mobius.opis.swing.panels.debug;

import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.data.holders.newtypes.CachedString;
import mcp.mobius.opis.data.holders.newtypes.DataTileEntity;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataRaw;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.actions.ActionOrphanTileEntities;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class PanelThreads extends JPanelMsgHandler implements ITabPanel {

	public PanelThreads() {
		setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 0,grow");
		
		table = new JTableStats(
				new String[] {"Name"},
				new Class[]  {CachedString.class},
				new int[]    {SwingConstants.LEFT}
				);			
		scrollPane.setViewportView(table);
	}

	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {
		switch(msg){
		case LIST_THREADS:{
			
			((JTableStats)this.getTable()).setTableData(rawdata.array);
			
			DefaultTableModel model = (DefaultTableModel)this.getTable().getModel();
			int               row   = this.getTable().clearTable(CachedString.class);

			for (Object o : rawdata.array){
				CachedString data = (CachedString)o;
				model.addRow(new Object[]  { data });
			}
			
			this.getTable().dataUpdated(row);			
			
			break;
		}		
		
		default:
			return false;
			
		}
		return true;
	}

	@Override
	public SelectedTab getSelectedTab() {
		return SelectedTab.THREADS;
	}
}
