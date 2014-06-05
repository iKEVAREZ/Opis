package mcp.mobius.opis.swing.widgets;

import javax.swing.JPanel;
import mcp.mobius.opis.api.IMessageHandler;

public abstract class JPanelMsgHandler extends JPanel implements IMessageHandler {
	
	public JTableStats table;
	
	public JTableStats getTable(){
		return this.table;
	}
	
}
