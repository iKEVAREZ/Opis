package mcp.mobius.opis.data.client;

import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

import mcp.mobius.opis.gui.swing.SwingUI;

public class DataCache {

	private static DataCache _instance = new DataCache();
	public  static DataCache instance() { return _instance; };
	
	private HashMap<String, Integer> amountEntities = new HashMap<String, Integer>();
	
	public void setAmountEntities(HashMap<String, Integer> stats){
		this.amountEntities = stats;
		
		DefaultTableModel model = new DefaultTableModel(
				new Object[][] {
					{null, null},
				},
				new String[] {
					"Type", "Amount"
				}
			) {
				Class[] columnTypes = new Class[] {
					String.class, Integer.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			};
		
		for (int i = 0; i < model.getRowCount(); i++)
			model.removeRow(i);
		
		int totalEntities = 0;
		
		for (String type : DataCache.instance().getAmountEntities().keySet()){
			model.addRow(new Object[] {type, DataCache.instance().getAmountEntities().get(type)});
			totalEntities += DataCache.instance().getAmountEntities().get(type);
		}

		SwingUI.instance().getTableEntityList().setModel(model);
		SwingUI.instance().getLabelAmountValue().setText(String.valueOf(totalEntities));
		model.fireTableDataChanged();		
	}
	
	public HashMap<String, Integer> getAmountEntities(){
		return this.amountEntities;
	}
	
	
}
