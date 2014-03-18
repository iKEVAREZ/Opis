package mcp.mobius.opis.gui.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

import java.awt.GridLayout;

import javax.swing.JLabel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.opis.network.client.Packet_ReqData;
import mcp.mobius.opis.network.enums.DataReq;

public class SwingUI extends JFrame implements  ActionListener, ItemListener{

	private static SwingUI _instance = new SwingUI();
	public  static SwingUI instance(){
		return _instance;
	}
	
	
	private JPanel contentPane;
	private JScrollPane scrollPaneEntityAmount;
	private JTable tableEntityList;
	private JPanel panelEntityAmount;
	private JLabel labelAmountValue;
	private JLabel labelAmountTotal;
	private JCheckBox chkBoxDisplayAll;
	private JButton btnRefreshEntityAmount;

	private JPanel panelTimingTE;
	private JPanel panelTimingEnt;
	private JPanel panelTimingHandler;
	private JScrollPane scrollPaneTimingEnt;
	private JTable tableTimingEnt;
	private JScrollPane scrollPaneTimingHandler;
	private JTable tableTimingHandler;
	private JTable tableTimingTE;
	
	public void showUI(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwingUI.instance().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
	}
	
	/**
	 * Create the frame.
	 */
	private SwingUI() {
		setTitle("Opis Control Panel");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 571, 383);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		panelEntityAmount = new JPanel();
		tabbedPane.addTab("Entity Amount", null, panelEntityAmount, null);
		GridBagLayout gbl_panelEntityAmount = new GridBagLayout();
		gbl_panelEntityAmount.columnWidths = new int[]{217, 217, 0};
		gbl_panelEntityAmount.rowHeights = new int[]{0, 120, 30, 0};
		gbl_panelEntityAmount.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panelEntityAmount.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelEntityAmount.setLayout(gbl_panelEntityAmount);
		
		chkBoxDisplayAll = new JCheckBox("Filter Entities");
		GridBagConstraints gbc_chkBoxDisplayAll = new GridBagConstraints();
		gbc_chkBoxDisplayAll.insets = new Insets(0, 0, 5, 5);
		gbc_chkBoxDisplayAll.gridx = 0;
		gbc_chkBoxDisplayAll.gridy = 0;
		chkBoxDisplayAll.addItemListener(this);
		panelEntityAmount.add(chkBoxDisplayAll, gbc_chkBoxDisplayAll);
		
		btnRefreshEntityAmount = new JButton("Refresh");
		GridBagConstraints gbc_btnRefreshEntityAmount = new GridBagConstraints();
		gbc_btnRefreshEntityAmount.insets = new Insets(0, 0, 5, 0);
		gbc_btnRefreshEntityAmount.gridx = 1;
		gbc_btnRefreshEntityAmount.gridy = 0;
		btnRefreshEntityAmount.addActionListener(this);
		panelEntityAmount.add(btnRefreshEntityAmount, gbc_btnRefreshEntityAmount);
		
		scrollPaneEntityAmount = new JScrollPane();
		GridBagConstraints gbc_scrollPaneEntityAmount = new GridBagConstraints();
		gbc_scrollPaneEntityAmount.gridwidth = 2;
		gbc_scrollPaneEntityAmount.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneEntityAmount.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneEntityAmount.gridx = 0;
		gbc_scrollPaneEntityAmount.gridy = 1;
		panelEntityAmount.add(scrollPaneEntityAmount, gbc_scrollPaneEntityAmount);
		
		tableEntityList = new JTable();
		tableEntityList.setModel(new DefaultTableModel(
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
			boolean[] columnEditables = new boolean[] {
				false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tableEntityList.getColumnModel().getColumn(0).setPreferredWidth(231);
		tableEntityList.getColumnModel().getColumn(1).setPreferredWidth(237);
		tableEntityList.setAutoCreateRowSorter(true);
		scrollPaneEntityAmount.setViewportView(tableEntityList);
		
		labelAmountTotal = new JLabel("Total");
		labelAmountTotal.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_labelAmountTotal = new GridBagConstraints();
		gbc_labelAmountTotal.fill = GridBagConstraints.BOTH;
		gbc_labelAmountTotal.insets = new Insets(0, 0, 0, 5);
		gbc_labelAmountTotal.gridx = 0;
		gbc_labelAmountTotal.gridy = 2;
		panelEntityAmount.add(labelAmountTotal, gbc_labelAmountTotal);
		
		labelAmountValue = new JLabel("0");
		labelAmountValue.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_labelAmountValue = new GridBagConstraints();
		gbc_labelAmountValue.fill = GridBagConstraints.BOTH;
		gbc_labelAmountValue.gridx = 1;
		gbc_labelAmountValue.gridy = 2;
		panelEntityAmount.add(labelAmountValue, gbc_labelAmountValue);
		
		panelTimingTE = new JPanel();
		tabbedPane.addTab("TileEntity Timing", null, panelTimingTE, null);
		panelTimingTE.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPaneTimingTE = new JScrollPane();
		panelTimingTE.add(scrollPaneTimingTE);
		
		tableTimingTE = new JTable();
		tableTimingTE.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
			},
			new String[] {
				"Type", "Mod", "Dim", "Pos", "Update Time"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, Integer.class, Object.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tableTimingTE.setAutoCreateRowSorter(true);
		scrollPaneTimingTE.setViewportView(tableTimingTE);
		
		panelTimingEnt = new JPanel();
		tabbedPane.addTab("EntityTiming", null, panelTimingEnt, null);
		panelTimingEnt.setLayout(new GridLayout(0, 1, 0, 0));
		
		scrollPaneTimingEnt = new JScrollPane();
		panelTimingEnt.add(scrollPaneTimingEnt);
		
		tableTimingEnt = new JTable();
		tableTimingEnt.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null},
			},
			new String[] {
				"Type", "ID", "Dim", "Pos", "Update Time", "Data"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Integer.class, Integer.class, Object.class, String.class, Integer.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tableTimingEnt.setAutoCreateRowSorter(true);
		scrollPaneTimingEnt.setViewportView(tableTimingEnt);
		
		panelTimingHandler = new JPanel();
		tabbedPane.addTab("Handlers Timing", null, panelTimingHandler, null);
		panelTimingHandler.setLayout(new GridLayout(0, 1, 0, 0));
		
		scrollPaneTimingHandler = new JScrollPane();
		panelTimingHandler.add(scrollPaneTimingHandler);
		
		tableTimingHandler = new JTable();
		tableTimingHandler.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
			},
			new String[] {
				"Name", "Update Time"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tableTimingHandler.setAutoCreateRowSorter(true);
		scrollPaneTimingHandler.setViewportView(tableTimingHandler);
	}

	public JTable getTableEntityList() {
		return tableEntityList;
	}
	public JLabel getLabelAmountValue() {
		return labelAmountValue;
	}
	public JTable getTableTimingTE() {
		return tableTimingTE;
	}
	public JTable getTableTimingEnt() {
		return tableTimingEnt;
	}
	public JTable getTableTimingHandler() {
		return tableTimingHandler;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnRefreshEntityAmount){
			this.requestAmoutEntityUpdate();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getItem() == chkBoxDisplayAll){
			if      (e.getStateChange() == ItemEvent.SELECTED){
				PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND, DataReq.FILTERING, DataReq.TRUE));
				this.requestAmoutEntityUpdate();
			}
			else if (e.getStateChange() == ItemEvent.DESELECTED){
				PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND, DataReq.FILTERING, DataReq.FALSE));
				this.requestAmoutEntityUpdate();				
			}
		}
	}
	
	private void requestAmoutEntityUpdate(){
		PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.LIST, DataReq.AMOUNT, DataReq.ENTITIES));
	}

}
