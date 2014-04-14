package mcp.mobius.opis.swing.panels.timingclient;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;
import mcp.mobius.opis.api.IMessageHandler;
import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.data.holders.newtypes.DataEntityRender;
import mcp.mobius.opis.data.holders.newtypes.DataHandler;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataRaw;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.actions.ActionRunOpisClient;
import mcp.mobius.opis.swing.widgets.JTableStats;

import javax.swing.JLabel;

public class PanelRenderHandlers extends JPanel implements ITabPanel, IMessageHandler{
	private JTableStats table;
	private JButton btnRunRender;

	/**
	 * Create the panel.
	 */
	public PanelRenderHandlers() {
		setLayout(new MigLayout("", "[grow][grow][]", "[][grow][]"));
		
		btnRunRender = new JButton("Run Render");
		add(btnRunRender, "cell 2 0");
		btnRunRender.addActionListener(new ActionRunOpisClient());
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1 3 1,grow");
		
		table = new JTableStats(
				new String[] {"Name", "Update Time"},
				new Class[]  {String.class, DataTiming.class}
				);			
		scrollPane.setViewportView(table);		
	}

	public void setTable(ArrayList<DataHandler> data){
		
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		int               row   = this.updateData(table, model, DataHandler.class);		
		
		for (DataHandler o : data){
			model.addRow(new Object[] {o.name, o.update});
		}

		this.dataUpdated(table, model, row);			
	}
	
	public JTableStats getTable() {
		return table;
	}
	
	public <U> int updateData(JTable table, DefaultTableModel model, Class<U> datatype){
		int row = table.getSelectedRow();
		
		if (model.getRowCount() > 0)
			for (int i = model.getRowCount() - 1; i >= 0; i--)
				model.removeRow(i);		
		
		return row;
	}
	
	public void dataUpdated(JTable table, DefaultTableModel model, int row){
		model.fireTableDataChanged();
		
		try{
			table.setRowSelectionInterval(row, row);
		} catch (IllegalArgumentException e ){
			
		}		
	}	
	public JButton getBtnRunRender() {
		return btnRunRender;
	}

	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public SelectedTab getSelectedTab() {
		return SelectedTab.RENDERHANDLERS;
	}	
}