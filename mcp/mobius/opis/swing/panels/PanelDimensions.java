package mcp.mobius.opis.swing.panels;

import javax.swing.JPanel;

import mcp.mobius.opis.api.IMessageHandler;
import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.data.holders.newtypes.DataDimension;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.data.holders.newtypes.DataTimingMillisecond;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataRaw;
import mcp.mobius.opis.swing.actions.ActionDimensions;
import mcp.mobius.opis.swing.widgets.JButtonAccess;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableButton;
import mcp.mobius.opis.swing.widgets.JTableStats;
import net.miginfocom.swing.MigLayout;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;

public class PanelDimensions extends JPanelMsgHandler implements IMessageHandler,	ITabPanel {
	private JButtonAccess btnPurgeAll;
	
	public PanelDimensions() {
		setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		btnPurgeAll = new JButtonAccess("Purge All", AccessLevel.PRIVILEGED);
		add(btnPurgeAll, "cell 0 0");
		btnPurgeAll.addActionListener(new ActionDimensions());
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1,grow");
		
		table = new JTableStats();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null, null, null},
				//	{null, null, null, null, null, null, null, null, null},
			},
			new String[] {
				"Dim", "Name", "Players", "Forced chunks", "Loaded chunks", "Monsters", "Animals", "Stacks", "Update time", "Purge"
				//	"Dim", "Name", "Players", "Forced chunks", "Loaded chunks", "Monsters", "Animals", "Entities", "Purge"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, DataTimingMillisecond.class, JTableButton.class
				//Integer.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, JTableButton.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false,false,false,false, false, true
				//false, false, false, false, false,false,false,false, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		//table.setBackground(UIManager.getColor("windowBorder"));
		table.setBackground(this.getBackground());
		table.setAutoCreateRowSorter(true);	
		table.setShowGrid(false);
		scrollPane.setViewportView(table);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );		
		
		for (int i = 0; i < table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		
		JTableButton buttonColumn = new JTableButton(table, new ActionDimensions(), 9, AccessLevel.PRIVILEGED);
		//JTableButton buttonColumn = new JTableButton(table, new ActionDimensions(), 8, AccessLevel.PRIVILEGED);
	}

	

	@Override
	public String getTabTitle() { return "Dimensions"; }
	@Override
	public String getTabRefName() {	return "opis.dimensions"; }
	
	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {	
		switch(msg){
		case LIST_DIMENSION_DATA:{
			((JTableStats)this.getTable()).setTableData(rawdata.array);
			
			DefaultTableModel model = (DefaultTableModel)this.getTable().getModel();
			int               row   = this.updateData(table, model, DataDimension.class);	

			for (Object o : rawdata.array){
				DataDimension data = (DataDimension)o;
				model.addRow(new Object[] {
						data.dim,
						data.name,
						data.players,
						data.forced,
						data.loaded,
						data.mobs,
						data.neutral,
						data.itemstacks,
						data.update.asMillisecond(),
						"Purge"
				});
			}			
			this.dataUpdated(table, model, row);
			break;
		}
		default:
			return false;
			
		}
		return true;		
	}
	public JButton getBtnPurgeAll() {
		return btnPurgeAll;
	}
}
