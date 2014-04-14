package mcp.mobius.opis.swing.widgets;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import mapwriter.api.IMwDataProvider;
import mcp.mobius.opis.api.IMessageHandler;
import mcp.mobius.opis.data.holders.stats.StatAbstract;

public abstract class JPanelMsgHandler extends JPanel implements IMessageHandler {
	
	public JTableStats table;
	
	public JTableStats getTable(){
		return this.table;
	}
	
}
