package mcp.mobius.opis.swing.widgets;

import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.stats.StatAbstract;

public class JTableStats extends JTable {

	protected ArrayList<ISerializable> statistics;
	
	public JTableStats(){
		super();
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setAutoCreateRowSorter(true);	
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );		
		
		for (int i = 0; i < this.getColumnCount(); i++)
			this.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);		
	}
	
	public ArrayList<ISerializable> getTableData(){
		return this.statistics;
	}
	
	public void setTableData(ArrayList<ISerializable> statistics){
		this.statistics = statistics;
	}
	
	public void clearTableData(){
		this.statistics = new ArrayList<ISerializable>();
	}

	public void addTableData(ArrayList<ISerializable> statistics){
		this.statistics.addAll(statistics);
	}
	
	/*
	public void setStatistics(ArrayList<ISerializable> statistics){
		this.statistics = new ArrayList<ISerializable>();
		for (Object o : statistics){
			this.statistics.add((StatAbstract)o);
		}
	}
	*/	
	
}
