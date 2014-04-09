package mcp.mobius.opis.swing.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.data.holders.basetypes.SerialDouble;
import mcp.mobius.opis.data.holders.newtypes.DataEntity;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.gui.overlay.entperchunk.OverlayEntityPerChunk;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataRaw;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.actions.ActionRunOpis;
import mcp.mobius.opis.swing.actions.ActionTimingEntities;
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

public class PanelTimingEntities extends JPanelMsgHandler implements ITabPanel{
	private JButtonAccess btnRun;
	private JButtonAccess btnPull;
	private JButtonAccess btnTeleport;
	private JButtonAccess btnCenter;
	private JLabel lblSummary;

	/**
	 * Create the panel.
	 */
	public PanelTimingEntities() {
		setLayout(new MigLayout("", "[][][][grow][]", "[][grow][]"));
		
		btnCenter = new JButtonAccess("Center Map", AccessLevel.NONE);
		add(btnCenter, "cell 0 0");
		btnCenter.addActionListener(new ActionTimingEntities());
		
		btnTeleport = new JButtonAccess("Teleport", AccessLevel.PRIVILEGED);
		add(btnTeleport, "cell 1 0");
		btnTeleport.addActionListener(new ActionTimingEntities());
		
		btnPull = new JButtonAccess("Pull", AccessLevel.PRIVILEGED);
		add(btnPull, "cell 2 0");
		btnPull.addActionListener(new ActionTimingEntities());
		
		btnRun = new JButtonAccess("Run Opis", AccessLevel.PRIVILEGED);
		add(btnRun, "cell 4 0");
		btnRun.addActionListener(new ActionRunOpis());
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1 5 1,grow");
		
		table = new JTableStats();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Type", "ID", "Dim", "Pos", "Update Time", "Data"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Integer.class, Integer.class, Object.class, DataTiming.class, Integer.class
			};
			boolean[] columnEditables = new boolean[] {
					false, false, false, false, false, false
			};			
			@Override
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.setAutoCreateRowSorter(true);		
		scrollPane.setViewportView(table);
		
		lblSummary = new JLabel("New label");
		add(lblSummary, "cell 0 2 5 1,alignx center");
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );		
		
		for (int i = 0; i < table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);			

	}

	public JButton getBtnRun()      {return btnRun;}
	public JButton getBtnPull()     {return btnPull;}
	public JButton getBtnTeleport() {return btnTeleport;}
	public JButton getBtnCenter()   {return btnCenter;}
	public JLabel  getLblSummary()  {return lblSummary;}

	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {
		switch(msg){
		case LIST_TIMING_ENTITIES:{
			
			((JTableStats)this.getTable()).setTableData(rawdata.array);
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			int               row   = this.updateData(table, model, DataEntity.class);	
			
			for (Object o : rawdata.array){
				DataEntity data = (DataEntity)o;
				model.addRow(new Object[]  {data.name, 
											data.eid,
											data.pos.dim,
											String.format("[ %4d %4d %4d ]",data.pos.x,data.pos.y,data.pos.z), 
											data.update,
											data.npoints});
			}
			
			this.dataUpdated(table, model, row);			
			
			break;
		}
		case VALUE_TIMING_ENTITIES:{
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
	public String getTabTitle() {
		return "Entities timing";
	}

	@Override
	public String getTabRefName() {
		return "opis.timingents";
	}
	
	@Override
	public SelectedTab getSelectedTab() {
		return SelectedTab.TIMINGENTITES;
	}		
}
