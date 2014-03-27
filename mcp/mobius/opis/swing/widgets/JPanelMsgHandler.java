package mcp.mobius.opis.swing.widgets;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import mapwriter.api.IMwDataProvider;
import mcp.mobius.opis.api.IMessageHandler;
import mcp.mobius.opis.data.holders.stats.StatAbstract;

public abstract class JPanelMsgHandler extends JPanel implements IMessageHandler {
	
	protected <U> int updateData(JTable table, DefaultTableModel model, Class<U> datatype){
		int row = table.getSelectedRow();
		
		if (model.getRowCount() > 0)
			for (int i = model.getRowCount() - 1; i >= 0; i--)
				model.removeRow(i);		
		
		return row;
	}
	
	protected void dataUpdated(JTable table, DefaultTableModel model, int row){
		model.fireTableDataChanged();
		
		try{
			table.setRowSelectionInterval(row, row);
		} catch (IllegalArgumentException e ){
			
		}		
	}
	
}
