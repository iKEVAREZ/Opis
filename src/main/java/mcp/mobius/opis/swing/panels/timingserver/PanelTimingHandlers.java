package mcp.mobius.opis.swing.panels.timingserver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.data.holders.basetypes.SerialDouble;
import mcp.mobius.opis.data.holders.newtypes.CachedString;
import mcp.mobius.opis.data.holders.newtypes.DataHandler;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.Packet_ReqData;
import mcp.mobius.opis.network.packets.server.NetDataRaw;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.actions.ActionRunOpis;
import mcp.mobius.opis.swing.widgets.JButtonAccess;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import cpw.mods.fml.common.network.PacketDispatcher;

public class PanelTimingHandlers extends JPanelMsgHandler implements ITabPanel{
	private JButtonAccess btnRun;
	private JLabel lblSummary;
	
	/**
	 * Create the panel.
	 */
	public PanelTimingHandlers() {
		setLayout(new MigLayout("", "[grow][]", "[][grow][]"));
		
		btnRun = new JButtonAccess("Run Opis", AccessLevel.PRIVILEGED);
		add(btnRun, "cell 1 0");
		btnRun.addActionListener(new ActionRunOpis());
		
		JScrollPane scrollPane = new JScrollPane();
		
		add(scrollPane, "cell 0 1 2 1,grow");
		
		table = new JTableStats(
				new String[] {"Name", "Update Time"},
				new Class[]  {CachedString.class, DataTiming.class}
				);
		scrollPane.setViewportView(table);			
		
		lblSummary = new JLabel("TmpText");
		add(lblSummary, "cell 0 2 2 1,alignx center");
	}

	public JButton getBtnRun()    {return btnRun;}
	public JLabel  getLblSummary(){return lblSummary;}

	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {
		switch(msg){
		case LIST_TIMING_HANDLERS:{
			
			((JTableStats)this.getTable()).setTableData(rawdata.array);
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			int               row   = this.getTable().clearTable(DataHandler.class);	
			
			for (Object o : rawdata.array){
				DataHandler data = (DataHandler)o;
				model.addRow(new Object[] {data.name, data.update});
			}

			this.getTable().dataUpdated(row);			
			
			break;
		}
		case VALUE_TIMING_HANDLERS:{
			this.getLblSummary().setText(String.format("Total update time : %s", ((DataTiming)rawdata.value).toString()));
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
		return SelectedTab.TIMINGHANDLERS;
	}	
}
