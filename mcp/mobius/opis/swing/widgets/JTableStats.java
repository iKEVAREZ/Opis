package mcp.mobius.opis.swing.widgets;

import java.util.ArrayList;

import javax.swing.JTable;

import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.stats.StatAbstract;

public class JTableStats extends JTable {

	protected ArrayList<ISerializable> statistics;
	
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
