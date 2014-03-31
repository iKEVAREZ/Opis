package mcp.mobius.opis.swing.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataRaw;
import mcp.mobius.opis.swing.actions.ActionAmountEntities;
import mcp.mobius.opis.swing.widgets.JButtonAccess;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;
import net.miginfocom.swing.MigLayout;

import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PanelAmountEntities extends JPanelMsgHandler implements ITabPanel{
	private JCheckBox chckbxFilter;
	private JButtonAccess btnKillAll;
	private JButtonAccess btnRefresh;
	private JLabel lblSummary;

	/**
	 * Create the panel.
	 */
	public PanelAmountEntities() {
		setLayout(new MigLayout("", "[][][grow][]", "[][grow][]"));
		
		chckbxFilter = new JCheckBox("Filter Entities");
		add(chckbxFilter, "cell 0 0");
		chckbxFilter.addItemListener(new ActionAmountEntities());
		
		btnKillAll = new JButtonAccess("Kill All", AccessLevel.PRIVILEGED);
		add(btnKillAll, "cell 1 0");
		btnKillAll.addActionListener(new ActionAmountEntities());
		
		btnRefresh = new JButtonAccess("Refresh", AccessLevel.NONE);
		add(btnRefresh, "cell 3 0");
		btnRefresh.addActionListener(new ActionAmountEntities());
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1 4 1,grow");
		
		table = new JTableStats();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Type", "Amount"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Integer.class
			};
			boolean[] columnEditables = new boolean[] {
					false, false
			};			
			@Override
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.setAutoCreateRowSorter(true);		
		scrollPane.setViewportView(table);
		
		lblSummary = new JLabel("New label");
		add(lblSummary, "cell 0 2 4 1,alignx center");

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );		
		
		for (int i = 0; i < table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);		
		
	}

	public JCheckBox getChckbxFilter() {return chckbxFilter;}
	public JButton   getBtnKillAll()   {return btnKillAll;}
	public JButton   getBtnRefresh()   {return btnRefresh;}
	public JLabel    getLblSummary()   {return lblSummary;}	
	
	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {
		switch(msg){
		case LIST_AMOUNT_ENTITIES:{
			((JTableStats)this.getTable()).setStatistics(rawdata.array);
			
			DefaultTableModel model = (DefaultTableModel)this.getTable().getModel();
			int               row   = this.updateData(table, model, AmountHolder.class);	
			int totalEntities = 0;

			for (Object o : rawdata.array){
				AmountHolder entity = (AmountHolder)o;
				model.addRow(new Object[] {entity.key, entity.value});
				totalEntities += entity.value;
			}			
			
			this.getLblSummary().setText("Total : " + String.valueOf(totalEntities));
			this.dataUpdated(table, model, row);
			break;
		}
		default:
			return false;
			
		}
		return true;
	}

	@Override
	public String getTabTitle() {
		return "Entities amount";
	}

	@Override
	public String getTabRefName() {
		return "opis.amountents";
	}
}
