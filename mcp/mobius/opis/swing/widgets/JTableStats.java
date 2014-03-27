package mcp.mobius.opis.swing.widgets;

import java.util.ArrayList;

import javax.swing.JTable;

import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.stats.StatAbstract;

public class JTableStats extends JTable {

	protected ArrayList<StatAbstract> statistics;
	
	public ArrayList<StatAbstract> getStatistics(){
		return this.statistics;
	}
	
	/*
	public void setStatistics(ArrayList<StatAbstract> statistics){
		this.statistics = statistics;
	}
	*/
	
	public void setStatistics(ArrayList<ISerializable> statistics){
		this.statistics = new ArrayList<StatAbstract>();
		for (Object o : statistics){
			this.statistics.add((StatAbstract)o);
		}
	}	
	
}
