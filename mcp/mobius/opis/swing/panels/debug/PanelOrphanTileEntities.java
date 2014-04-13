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
import javax.swing.table.DefaultTableModel;

public class PanelOrphanTileEntities extends JPanelMsgHandler implements ITabPanel {

	private JButton btnRefresh;
	public PanelOrphanTileEntities() {
		setLayout(new MigLayout("", "[grow][]", "[][grow]"));
		
		btnRefresh = new JButton("Refresh");
		add(btnRefresh, "cell 1 0");
		btnRefresh.addActionListener(new ActionOrphanTileEntities());
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1 2 1,grow");
		
		table = new JTableStats();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Class", "Hash", "Is valid?", "Type", "Dimension", "Coordinates"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, Boolean.class, CachedString.class, Integer.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.setAutoCreateRowSorter(true);			
		scrollPane.setViewportView(table);
	}

	int row;
	
	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {
		switch(msg){
		case LIST_ORPHAN_TILEENTS:{
			
			((JTableStats)this.getTable()).addTableData(rawdata.array);
			DefaultTableModel model = (DefaultTableModel)this.getTable().getModel();
			//int               row   = this.updateData(table, model, DataTileEntity.class);

			for (Object o : rawdata.array){
				DataTileEntity data = (DataTileEntity)o;
				model.addRow(new Object[]  {
					data.clazz,
					String.format("0x%x", data.hashCode),
					data.isValid,
					data.cause,
					data.pos.dim,
				     String.format("[ %4d %4d %4d ]", data.pos.x, data.pos.y, data.pos.z),  
					 });
			}
			
			this.dataUpdated(table, model, row);			
			
			break;
		}		
		
		case LIST_ORPHAN_TILEENTS_CLEAR:{
			((JTableStats)this.getTable()).clearTableData();
			DefaultTableModel model = (DefaultTableModel)this.getTable().getModel();
			row = this.clearTable(table, model, DataTileEntity.class);			
		}
		
		default:
			return false;
			
		}
		return true;
	}

	@Override
	public SelectedTab getSelectedTab() {
		return SelectedTab.ORPHANTES;
	}

	public JButton getBtnRefresh() {
		return btnRefresh;
	}
}
