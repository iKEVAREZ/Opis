package mcp.mobius.opis.swing.panels.timingserver;

import javax.swing.JPanel;

import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.data.holders.newtypes.DataEvent;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataRaw;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.actions.ActionRunOpis;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PanelTimingEvents extends JPanelMsgHandler implements ITabPanel {
	private JButton btnRun;

	public PanelTimingEvents() {
		setLayout(new MigLayout("", "[grow][]", "[][grow]"));
		
		btnRun = new JButton("Run Opis");
		add(btnRun, "cell 1 0");
		btnRun.addActionListener(new ActionRunOpis());
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1 2 1,grow");
		
		table = new JTableStats(
				new String[] {"Event", "Class", "Handler", "Calls", "Timing"},
				new Class[]  {String.class, String.class, String.class, Long.class, DataTiming.class}
				);
		scrollPane.setViewportView(table);			
	}

	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {
		switch(msg){
		
		case LIST_TIMING_EVENTS:{
			
			((JTableStats)this.getTable()).setTableData(rawdata.array);
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			int               row   = this.clearTable(table, model, DataEvent.class);	
			
			for (Object o : rawdata.array){
				DataEvent data = (DataEvent)o;
				model.addRow(new Object[] {data.event, data.package_, data.handler, data.nCalls, data.update});
			}

			this.dataUpdated(table, model, row);			
			
			break;
		}		
		
		case STATUS_START:{
			this.getBtnRun().setText("Running...");
			break;
		}
		case STATUS_STOP:{
			this.getBtnRun().setText("Run Opis");
			break;
		}		
		case STATUS_RUNNING:{
			this.getBtnRun().setText("Running...");
			break;
		}		
		default:
			return false;
			
		}
		return true;
	}

	@Override
	public SelectedTab getSelectedTab() {
		return SelectedTab.TIMINGEVENTS;
	}

	public JButton getBtnRun() {
		return btnRun;
	}
}
