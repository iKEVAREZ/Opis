package mcp.mobius.opis.gui.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import cpw.mods.fml.common.network.PacketDispatcher;
import mapwriter.Mw;
import mapwriter.api.MwAPI;
import mapwriter.gui.MwGui;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.client.DataCache;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.SerialString;
import mcp.mobius.opis.data.holders.basetypes.TargetEntity;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.network.client.Packet_ReqData;
import mcp.mobius.opis.network.enums.DataReq;
import mcp.mobius.opis.overlay.OverlayMeanTime;
import mcp.mobius.opis.overlay.entperchunk.OverlayEntityPerChunk;
import net.minecraft.client.Minecraft;

import javax.swing.ListSelectionModel;
import javax.swing.JSeparator;

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
	private JPanel panelTimingChunk;
	private JScrollPane scrollPaneTimingChunk;
	private JTable tableTimingChunk;
	private JButton btnTimingTECenterMap;
	private JButton btnTimingTETeleport;
	private JButton btnTimingEntTeleport;
	private JButton btnTimingEntCenterMap;
	private JButton btnTimingEntRefresh;
	private JButton btnTimingTERefresh;
	private JButton btnTimingHandlerRefresh;
	private JButton btnTimingChunkRefresh;
	private JLabel lblTimingTEValue;
	private JLabel lblTimingEntValue;
	private JLabel lblTimingHandlerValue;
	private JPanel panelSummary;
	private JLabel lblSummary_3;
	private JLabel lblSummary_4;
	private JLabel lblSummary_5;
	private JLabel lblSummary_6;
	private JLabel lblSummaryTimingTileEnts;
	private JLabel lblSummaryTimingHandlers;
	private JLabel lblSummaryTimingEntities;
	private JLabel lblSummaryTimingTotal;
	private JLabel lblSummary_7;
	private JLabel lblSummary_8;
	private JLabel lblSummary_9;
	private JLabel lblSummary_10;
	private JLabel lblSummaryAmountTileEnts;
	private JLabel lblSummaryAmountEntities;
	private JLabel lblSummaryAmountHandlers;
	private JLabel lblSummary_1;
	private JLabel label;
	private JButton btnSummaryReset;
	private JButton btnTimingEntKillTarget;
	private JButton btnAmountKillAll;
	private JLabel lblSummary_11;
	private JLabel lblSummary_12;
	private JLabel lblSummaryTimingTick;
	private JLabel lblSummaryTickChart;
	private JLabel lblSummary_13;
	private JLabel lblSummaryTimingWorldTick;
	private JLabel lblSummary_14;
	
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
		setBounds(100, 100, 745, 413);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		panelEntityAmount = new JPanel();
		tabbedPane.addTab("Entity Amount", null, panelEntityAmount, null);
		GridBagLayout gbl_panelEntityAmount = new GridBagLayout();
		gbl_panelEntityAmount.columnWidths = new int[]{217, 407, 217, 0};
		gbl_panelEntityAmount.rowHeights = new int[]{0, 402, 30, 0};
		gbl_panelEntityAmount.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panelEntityAmount.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelEntityAmount.setLayout(gbl_panelEntityAmount);
		
		chkBoxDisplayAll = new JCheckBox("Filter Entities");
		GridBagConstraints gbc_chkBoxDisplayAll = new GridBagConstraints();
		gbc_chkBoxDisplayAll.anchor = GridBagConstraints.WEST;
		gbc_chkBoxDisplayAll.insets = new Insets(0, 0, 5, 5);
		gbc_chkBoxDisplayAll.gridx = 0;
		gbc_chkBoxDisplayAll.gridy = 0;
		chkBoxDisplayAll.addItemListener(this);
		panelEntityAmount.add(chkBoxDisplayAll, gbc_chkBoxDisplayAll);
		
		btnRefreshEntityAmount = new JButton("Refresh");
		GridBagConstraints gbc_btnRefreshEntityAmount = new GridBagConstraints();
		gbc_btnRefreshEntityAmount.anchor = GridBagConstraints.EAST;
		gbc_btnRefreshEntityAmount.insets = new Insets(0, 0, 5, 0);
		gbc_btnRefreshEntityAmount.gridx = 2;
		gbc_btnRefreshEntityAmount.gridy = 0;
		btnRefreshEntityAmount.addActionListener(this);
		
		btnAmountKillAll = new JButton("Kill All");
		btnAmountKillAll.addActionListener(this);
		GridBagConstraints gbc_btnAmountKillAll = new GridBagConstraints();
		gbc_btnAmountKillAll.anchor = GridBagConstraints.EAST;
		gbc_btnAmountKillAll.insets = new Insets(0, 0, 5, 5);
		gbc_btnAmountKillAll.gridx = 1;
		gbc_btnAmountKillAll.gridy = 0;
		panelEntityAmount.add(btnAmountKillAll, gbc_btnAmountKillAll);
		panelEntityAmount.add(btnRefreshEntityAmount, gbc_btnRefreshEntityAmount);
		
		scrollPaneEntityAmount = new JScrollPane();
		GridBagConstraints gbc_scrollPaneEntityAmount = new GridBagConstraints();
		gbc_scrollPaneEntityAmount.gridwidth = 3;
		gbc_scrollPaneEntityAmount.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneEntityAmount.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneEntityAmount.gridx = 0;
		gbc_scrollPaneEntityAmount.gridy = 1;
		panelEntityAmount.add(scrollPaneEntityAmount, gbc_scrollPaneEntityAmount);
		
		tableEntityList = new JTable();
		tableEntityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
		gbc_labelAmountValue.gridx = 2;
		gbc_labelAmountValue.gridy = 2;
		panelEntityAmount.add(labelAmountValue, gbc_labelAmountValue);
		
		panelSummary = new JPanel();
		tabbedPane.addTab("Summary", null, panelSummary, null);
		GridBagLayout gbl_panelSummary = new GridBagLayout();
		gbl_panelSummary.columnWidths = new int[]{0, 50, 20, 0, 70, 0, 20, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 53, 0, 0};
		gbl_panelSummary.rowHeights = new int[]{0, -4, 0, 0, 0, 0, 20, 10, 30, 0, 0, 0, 0, 0};
		gbl_panelSummary.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panelSummary.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panelSummary.setLayout(gbl_panelSummary);
		
		lblSummary_1 = new JLabel("Update time");
		GridBagConstraints gbc_lblSummary_1 = new GridBagConstraints();
		gbc_lblSummary_1.anchor = GridBagConstraints.WEST;
		gbc_lblSummary_1.gridwidth = 2;
		gbc_lblSummary_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary_1.gridx = 4;
		gbc_lblSummary_1.gridy = 1;
		panelSummary.add(lblSummary_1, gbc_lblSummary_1);
		
		btnSummaryReset = new JButton("Reset");
		GridBagConstraints gbc_btnSummaryReset = new GridBagConstraints();
		gbc_btnSummaryReset.insets = new Insets(0, 0, 5, 0);
		gbc_btnSummaryReset.gridx = 18;
		gbc_btnSummaryReset.gridy = 1;
		btnSummaryReset.addActionListener(this);
		
		label = new JLabel("Amount");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.LINE_END;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 7;
		gbc_label.gridy = 1;
		panelSummary.add(label, gbc_label);
		panelSummary.add(btnSummaryReset, gbc_btnSummaryReset);
		
		lblSummary_13 = new JLabel("World Tick");
		lblSummary_13.setToolTipText("This is the world tick profiling.\nThe server will update the time, do random block ticks\nand this kind of things while inside this code part.");
		GridBagConstraints gbc_lblSummary_13 = new GridBagConstraints();
		gbc_lblSummary_13.anchor = GridBagConstraints.LINE_START;
		gbc_lblSummary_13.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary_13.gridx = 1;
		gbc_lblSummary_13.gridy = 2;
		panelSummary.add(lblSummary_13, gbc_lblSummary_13);
		
		lblSummaryTimingWorldTick = new JLabel("0");
		GridBagConstraints gbc_lblSummaryTimingWorldTick = new GridBagConstraints();
		gbc_lblSummaryTimingWorldTick.anchor = GridBagConstraints.EAST;
		gbc_lblSummaryTimingWorldTick.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummaryTimingWorldTick.gridx = 4;
		gbc_lblSummaryTimingWorldTick.gridy = 2;
		panelSummary.add(lblSummaryTimingWorldTick, gbc_lblSummaryTimingWorldTick);
		
		lblSummary_14 = new JLabel("ms");
		GridBagConstraints gbc_lblSummary_14 = new GridBagConstraints();
		gbc_lblSummary_14.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary_14.gridx = 5;
		gbc_lblSummary_14.gridy = 2;
		panelSummary.add(lblSummary_14, gbc_lblSummary_14);
		
		lblSummary_3 = new JLabel("Tile Entities");
		GridBagConstraints gbc_lblSummary_3 = new GridBagConstraints();
		gbc_lblSummary_3.anchor = GridBagConstraints.LINE_START;
		gbc_lblSummary_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary_3.gridx = 1;
		gbc_lblSummary_3.gridy = 3;
		panelSummary.add(lblSummary_3, gbc_lblSummary_3);
		
		lblSummaryTimingTileEnts = new JLabel("0");
		lblSummaryTimingTileEnts.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblSummaryTimingTileEnts = new GridBagConstraints();
		gbc_lblSummaryTimingTileEnts.anchor = GridBagConstraints.EAST;
		gbc_lblSummaryTimingTileEnts.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummaryTimingTileEnts.gridx = 4;
		gbc_lblSummaryTimingTileEnts.gridy = 3;
		panelSummary.add(lblSummaryTimingTileEnts, gbc_lblSummaryTimingTileEnts);
		
		lblSummary_9 = new JLabel("ms");
		GridBagConstraints gbc_lblSummary_9 = new GridBagConstraints();
		gbc_lblSummary_9.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary_9.gridx = 5;
		gbc_lblSummary_9.gridy = 3;
		panelSummary.add(lblSummary_9, gbc_lblSummary_9);
		
		lblSummaryAmountTileEnts = new JLabel("0");
		GridBagConstraints gbc_lblSummaryAmountTileEnts = new GridBagConstraints();
		gbc_lblSummaryAmountTileEnts.anchor = GridBagConstraints.LINE_END;
		gbc_lblSummaryAmountTileEnts.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummaryAmountTileEnts.gridx = 7;
		gbc_lblSummaryAmountTileEnts.gridy = 3;
		panelSummary.add(lblSummaryAmountTileEnts, gbc_lblSummaryAmountTileEnts);
		
		lblSummary_4 = new JLabel("Entities");
		GridBagConstraints gbc_lblSummary_4 = new GridBagConstraints();
		gbc_lblSummary_4.anchor = GridBagConstraints.WEST;
		gbc_lblSummary_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary_4.gridx = 1;
		gbc_lblSummary_4.gridy = 4;
		panelSummary.add(lblSummary_4, gbc_lblSummary_4);
		
		lblSummaryTimingEntities = new JLabel("0");
		GridBagConstraints gbc_lblSummaryTimingEntities = new GridBagConstraints();
		gbc_lblSummaryTimingEntities.anchor = GridBagConstraints.EAST;
		gbc_lblSummaryTimingEntities.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummaryTimingEntities.gridx = 4;
		gbc_lblSummaryTimingEntities.gridy = 4;
		panelSummary.add(lblSummaryTimingEntities, gbc_lblSummaryTimingEntities);
		
		lblSummary_7 = new JLabel("ms");
		GridBagConstraints gbc_lblSummary_7 = new GridBagConstraints();
		gbc_lblSummary_7.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary_7.gridx = 5;
		gbc_lblSummary_7.gridy = 4;
		panelSummary.add(lblSummary_7, gbc_lblSummary_7);
		
		lblSummaryAmountEntities = new JLabel("0");
		GridBagConstraints gbc_lblSummaryAmountEntities = new GridBagConstraints();
		gbc_lblSummaryAmountEntities.anchor = GridBagConstraints.LINE_END;
		gbc_lblSummaryAmountEntities.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummaryAmountEntities.gridx = 7;
		gbc_lblSummaryAmountEntities.gridy = 4;
		panelSummary.add(lblSummaryAmountEntities, gbc_lblSummaryAmountEntities);
		
		lblSummary_5 = new JLabel("Handlers");
		lblSummary_5.setToolTipText("This is a generic profiling of server side tickhandlers.\nTickHandlers are a part of Forge and used by mods to do specific\nthings at the start and end of every tick.");
		GridBagConstraints gbc_lblSummary_5 = new GridBagConstraints();
		gbc_lblSummary_5.anchor = GridBagConstraints.WEST;
		gbc_lblSummary_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary_5.gridx = 1;
		gbc_lblSummary_5.gridy = 5;
		panelSummary.add(lblSummary_5, gbc_lblSummary_5);
		
		lblSummaryTimingHandlers = new JLabel("0");
		GridBagConstraints gbc_lblSummaryTimingHandlers = new GridBagConstraints();
		gbc_lblSummaryTimingHandlers.anchor = GridBagConstraints.EAST;
		gbc_lblSummaryTimingHandlers.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummaryTimingHandlers.gridx = 4;
		gbc_lblSummaryTimingHandlers.gridy = 5;
		panelSummary.add(lblSummaryTimingHandlers, gbc_lblSummaryTimingHandlers);
		
		lblSummary_8 = new JLabel("ms");
		GridBagConstraints gbc_lblSummary_8 = new GridBagConstraints();
		gbc_lblSummary_8.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary_8.gridx = 5;
		gbc_lblSummary_8.gridy = 5;
		panelSummary.add(lblSummary_8, gbc_lblSummary_8);
		
		lblSummaryAmountHandlers = new JLabel("0");
		GridBagConstraints gbc_lblSummaryAmountHandlers = new GridBagConstraints();
		gbc_lblSummaryAmountHandlers.anchor = GridBagConstraints.LINE_END;
		gbc_lblSummaryAmountHandlers.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummaryAmountHandlers.gridx = 7;
		gbc_lblSummaryAmountHandlers.gridy = 5;
		panelSummary.add(lblSummaryAmountHandlers, gbc_lblSummaryAmountHandlers);
		
		lblSummary_6 = new JLabel("Total");
		GridBagConstraints gbc_lblSummary_6 = new GridBagConstraints();
		gbc_lblSummary_6.anchor = GridBagConstraints.WEST;
		gbc_lblSummary_6.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary_6.gridx = 1;
		gbc_lblSummary_6.gridy = 7;
		panelSummary.add(lblSummary_6, gbc_lblSummary_6);
		
		lblSummaryTimingTotal = new JLabel("0");
		GridBagConstraints gbc_lblSummaryTimingTotal = new GridBagConstraints();
		gbc_lblSummaryTimingTotal.anchor = GridBagConstraints.EAST;
		gbc_lblSummaryTimingTotal.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummaryTimingTotal.gridx = 4;
		gbc_lblSummaryTimingTotal.gridy = 7;
		panelSummary.add(lblSummaryTimingTotal, gbc_lblSummaryTimingTotal);
		
		lblSummary_10 = new JLabel("ms");
		GridBagConstraints gbc_lblSummary_10 = new GridBagConstraints();
		gbc_lblSummary_10.anchor = GridBagConstraints.WEST;
		gbc_lblSummary_10.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary_10.gridx = 5;
		gbc_lblSummary_10.gridy = 7;
		panelSummary.add(lblSummary_10, gbc_lblSummary_10);
		
		lblSummary_11 = new JLabel("Tick Time");
		GridBagConstraints gbc_lblSummary_11 = new GridBagConstraints();
		gbc_lblSummary_11.anchor = GridBagConstraints.WEST;
		gbc_lblSummary_11.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary_11.gridx = 1;
		gbc_lblSummary_11.gridy = 9;
		panelSummary.add(lblSummary_11, gbc_lblSummary_11);
		
		lblSummaryTimingTick = new JLabel("0");
		GridBagConstraints gbc_lblSummaryTimingTick = new GridBagConstraints();
		gbc_lblSummaryTimingTick.anchor = GridBagConstraints.EAST;
		gbc_lblSummaryTimingTick.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummaryTimingTick.gridx = 4;
		gbc_lblSummaryTimingTick.gridy = 9;
		panelSummary.add(lblSummaryTimingTick, gbc_lblSummaryTimingTick);
		
		lblSummary_12 = new JLabel("ms");
		GridBagConstraints gbc_lblSummary_12 = new GridBagConstraints();
		gbc_lblSummary_12.anchor = GridBagConstraints.WEST;
		gbc_lblSummary_12.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary_12.gridx = 5;
		gbc_lblSummary_12.gridy = 9;
		panelSummary.add(lblSummary_12, gbc_lblSummary_12);
		
		lblSummaryTickChart = new JLabel("");
		GridBagConstraints gbc_lblSummaryTickChart = new GridBagConstraints();
		gbc_lblSummaryTickChart.fill = GridBagConstraints.BOTH;
		gbc_lblSummaryTickChart.gridwidth = 18;
		gbc_lblSummaryTickChart.gridx = 1;
		gbc_lblSummaryTickChart.gridy = 12;
		panelSummary.add(lblSummaryTickChart, gbc_lblSummaryTickChart);
		
		panelTimingTE = new JPanel();
		tabbedPane.addTab("TileEntity Timing", null, panelTimingTE, null);
		GridBagLayout gbl_panelTimingTE = new GridBagLayout();
		gbl_panelTimingTE.columnWidths = new int[]{63, 58, 0, 0};
		gbl_panelTimingTE.rowHeights = new int[]{-12, 288, 20, 0};
		gbl_panelTimingTE.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panelTimingTE.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelTimingTE.setLayout(gbl_panelTimingTE);
		
		btnTimingTECenterMap = new JButton("Center Map");
		GridBagConstraints gbc_btnTimingTECenterMap = new GridBagConstraints();
		gbc_btnTimingTECenterMap.insets = new Insets(0, 0, 5, 5);
		gbc_btnTimingTECenterMap.gridx = 0;
		gbc_btnTimingTECenterMap.gridy = 0;
		btnTimingTECenterMap.addActionListener(this);
		panelTimingTE.add(btnTimingTECenterMap, gbc_btnTimingTECenterMap);
		
		btnTimingTETeleport = new JButton("Teleport");
		GridBagConstraints gbc_btnTimingTETeleport = new GridBagConstraints();
		gbc_btnTimingTETeleport.insets = new Insets(0, 0, 5, 5);
		gbc_btnTimingTETeleport.gridx = 1;
		gbc_btnTimingTETeleport.gridy = 0;
		btnTimingTETeleport.addActionListener(this);
		panelTimingTE.add(btnTimingTETeleport, gbc_btnTimingTETeleport);
		
		btnTimingTERefresh = new JButton("Refresh");
		GridBagConstraints gbc_btnTimingTERefresh = new GridBagConstraints();
		gbc_btnTimingTERefresh.anchor = GridBagConstraints.EAST;
		gbc_btnTimingTERefresh.insets = new Insets(0, 0, 5, 0);
		gbc_btnTimingTERefresh.gridx = 2;
		gbc_btnTimingTERefresh.gridy = 0;
		btnTimingTERefresh.addActionListener(this);
		panelTimingTE.add(btnTimingTERefresh, gbc_btnTimingTERefresh);
		
		JScrollPane scrollPaneTimingTE = new JScrollPane();
		GridBagConstraints gbc_scrollPaneTimingTE = new GridBagConstraints();
		gbc_scrollPaneTimingTE.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneTimingTE.gridwidth = 3;
		gbc_scrollPaneTimingTE.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneTimingTE.gridx = 0;
		gbc_scrollPaneTimingTE.gridy = 1;
		panelTimingTE.add(scrollPaneTimingTE, gbc_scrollPaneTimingTE);
		
		tableTimingTE = new JTable();
		tableTimingTE.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
		//tableTimingTE.getSelectionModel().addListSelectionListener(this);		
		scrollPaneTimingTE.setViewportView(tableTimingTE);
		
		lblTimingTEValue = new JLabel("Total update time : 0 µs");
		GridBagConstraints gbc_lblTimingTEValue = new GridBagConstraints();
		gbc_lblTimingTEValue.gridwidth = 3;
		gbc_lblTimingTEValue.insets = new Insets(0, 0, 0, 5);
		gbc_lblTimingTEValue.gridx = 0;
		gbc_lblTimingTEValue.gridy = 2;
		panelTimingTE.add(lblTimingTEValue, gbc_lblTimingTEValue);
		
		panelTimingEnt = new JPanel();
		tabbedPane.addTab("EntityTiming", null, panelTimingEnt, null);
		GridBagLayout gbl_panelTimingEnt = new GridBagLayout();
		gbl_panelTimingEnt.columnWidths = new int[]{89, 70, 0, 0, 0};
		gbl_panelTimingEnt.rowHeights = new int[]{11, 0, 20, 0};
		gbl_panelTimingEnt.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panelTimingEnt.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelTimingEnt.setLayout(gbl_panelTimingEnt);
		
		btnTimingEntCenterMap = new JButton("Center Map");
		GridBagConstraints gbc_btnTimingEntCenterMap = new GridBagConstraints();
		gbc_btnTimingEntCenterMap.insets = new Insets(0, 0, 5, 5);
		gbc_btnTimingEntCenterMap.gridx = 0;
		gbc_btnTimingEntCenterMap.gridy = 0;
		btnTimingEntCenterMap.addActionListener(this);
		panelTimingEnt.add(btnTimingEntCenterMap, gbc_btnTimingEntCenterMap);
		
		btnTimingEntTeleport = new JButton("Teleport");
		GridBagConstraints gbc_btnTimingEntTeleport = new GridBagConstraints();
		gbc_btnTimingEntTeleport.insets = new Insets(0, 0, 5, 5);
		gbc_btnTimingEntTeleport.gridx = 1;
		gbc_btnTimingEntTeleport.gridy = 0;
		btnTimingEntTeleport.addActionListener(this);
		panelTimingEnt.add(btnTimingEntTeleport, gbc_btnTimingEntTeleport);
		
		btnTimingEntRefresh = new JButton("Refresh");
		GridBagConstraints gbc_btnTimingEntRefresh = new GridBagConstraints();
		gbc_btnTimingEntRefresh.anchor = GridBagConstraints.EAST;
		gbc_btnTimingEntRefresh.insets = new Insets(0, 0, 5, 0);
		gbc_btnTimingEntRefresh.gridx = 3;
		gbc_btnTimingEntRefresh.gridy = 0;
		btnTimingEntRefresh.addActionListener(this);
		panelTimingEnt.add(btnTimingEntRefresh, gbc_btnTimingEntRefresh);
		
		btnTimingEntKillTarget = new JButton("Kill Target");
		btnTimingEntKillTarget.setEnabled(false);
		GridBagConstraints gbc_btnTimingEntKillTarget = new GridBagConstraints();
		gbc_btnTimingEntKillTarget.insets = new Insets(0, 0, 5, 5);
		gbc_btnTimingEntKillTarget.gridx = 2;
		gbc_btnTimingEntKillTarget.gridy = 0;
		btnTimingEntKillTarget.addActionListener(this);		
		panelTimingEnt.add(btnTimingEntKillTarget, gbc_btnTimingEntKillTarget);
		
		scrollPaneTimingEnt = new JScrollPane();
		GridBagConstraints gbc_scrollPaneTimingEnt = new GridBagConstraints();
		gbc_scrollPaneTimingEnt.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneTimingEnt.gridwidth = 4;
		gbc_scrollPaneTimingEnt.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneTimingEnt.gridx = 0;
		gbc_scrollPaneTimingEnt.gridy = 1;
		panelTimingEnt.add(scrollPaneTimingEnt, gbc_scrollPaneTimingEnt);
		
		tableTimingEnt = new JTable();
		tableTimingEnt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
		
		lblTimingEntValue = new JLabel("Total update time : 0 µs");
		GridBagConstraints gbc_lblTimingEntValue = new GridBagConstraints();
		gbc_lblTimingEntValue.gridwidth = 4;
		gbc_lblTimingEntValue.gridx = 0;
		gbc_lblTimingEntValue.gridy = 2;
		panelTimingEnt.add(lblTimingEntValue, gbc_lblTimingEntValue);
		
		panelTimingHandler = new JPanel();
		tabbedPane.addTab("Handlers Timing", null, panelTimingHandler, null);
		GridBagLayout gbl_panelTimingHandler = new GridBagLayout();
		gbl_panelTimingHandler.columnWidths = new int[]{649, 0};
		gbl_panelTimingHandler.rowHeights = new int[]{-12, 341, 0, 0};
		gbl_panelTimingHandler.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelTimingHandler.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelTimingHandler.setLayout(gbl_panelTimingHandler);
		
		btnTimingHandlerRefresh = new JButton("Refresh");
		GridBagConstraints gbc_btnTimingHandlerRefresh = new GridBagConstraints();
		gbc_btnTimingHandlerRefresh.anchor = GridBagConstraints.EAST;
		gbc_btnTimingHandlerRefresh.insets = new Insets(0, 0, 5, 0);
		gbc_btnTimingHandlerRefresh.gridx = 0;
		gbc_btnTimingHandlerRefresh.gridy = 0;
		btnTimingHandlerRefresh.addActionListener(this);
		panelTimingHandler.add(btnTimingHandlerRefresh, gbc_btnTimingHandlerRefresh);
		
		scrollPaneTimingHandler = new JScrollPane();
		GridBagConstraints gbc_scrollPaneTimingHandler = new GridBagConstraints();
		gbc_scrollPaneTimingHandler.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneTimingHandler.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneTimingHandler.gridx = 0;
		gbc_scrollPaneTimingHandler.gridy = 1;
		panelTimingHandler.add(scrollPaneTimingHandler, gbc_scrollPaneTimingHandler);
		
		tableTimingHandler = new JTable();
		tableTimingHandler.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
		
		lblTimingHandlerValue = new JLabel("Total update time : 0 µs");
		GridBagConstraints gbc_lblTimingHandlerValue = new GridBagConstraints();
		gbc_lblTimingHandlerValue.gridx = 0;
		gbc_lblTimingHandlerValue.gridy = 2;
		panelTimingHandler.add(lblTimingHandlerValue, gbc_lblTimingHandlerValue);
		
		panelTimingChunk = new JPanel();
		tabbedPane.addTab("Chunk Timing", null, panelTimingChunk, null);
		GridBagLayout gbl_panelTimingChunk = new GridBagLayout();
		gbl_panelTimingChunk.columnWidths = new int[]{649, 0};
		gbl_panelTimingChunk.rowHeights = new int[]{-15, 308, 0};
		gbl_panelTimingChunk.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelTimingChunk.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panelTimingChunk.setLayout(gbl_panelTimingChunk);
		
		scrollPaneTimingChunk = new JScrollPane();
		GridBagConstraints gbc_scrollPaneTimingChunk = new GridBagConstraints();
		gbc_scrollPaneTimingChunk.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneTimingChunk.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneTimingChunk.gridx = 0;
		gbc_scrollPaneTimingChunk.gridy = 1;
		panelTimingChunk.add(scrollPaneTimingChunk, gbc_scrollPaneTimingChunk);
		
		tableTimingChunk = new JTable();
		tableTimingChunk.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableTimingChunk.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
			},
			new String[] {
				"Dimension", "Position", "TileEntities", "Entities", "Update Time"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, Integer.class, Integer.class, String.class
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
		tableTimingChunk.setAutoCreateRowSorter(true);		
		scrollPaneTimingChunk.setViewportView(tableTimingChunk);
		
		btnTimingChunkRefresh = new JButton("Refresh");
		GridBagConstraints gbc_btnTimingChunkRefresh = new GridBagConstraints();
		gbc_btnTimingChunkRefresh.anchor = GridBagConstraints.EAST;
		gbc_btnTimingChunkRefresh.gridx = 0;
		gbc_btnTimingChunkRefresh.gridy = 0;
		btnTimingChunkRefresh.addActionListener(this);
		panelTimingChunk.add(btnTimingChunkRefresh, gbc_btnTimingChunkRefresh);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		
		for (int i = 0; i < tableEntityList.getColumnCount(); i++)
			tableEntityList.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

		for (int i = 0; i < tableTimingTE.getColumnCount(); i++)
			tableTimingTE.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		
		for (int i = 0; i < tableTimingEnt.getColumnCount(); i++)
			tableTimingEnt.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		
		for (int i = 0; i < tableTimingHandler.getColumnCount(); i++)
			tableTimingHandler.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		
		for (int i = 0; i < tableTimingChunk.getColumnCount(); i++)
			tableTimingChunk.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
	}

	public JTable getTableEntityList() {
		return tableEntityList;
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
	public JTable getTableTimingChunk() {
		return tableTimingChunk;
	}
	
	public JLabel getLabelAmountValue() {
		return labelAmountValue;
	}
	public JLabel getLblTimingHandlerValue() {
		return lblTimingHandlerValue;
	}
	public JLabel getLblTimingEntValue() {
		return lblTimingEntValue;
	}
	public JLabel getLblTimingTEValue() {
		return lblTimingTEValue;
	}
	public JLabel getLblSummaryTimingTileEnts() {
		return lblSummaryTimingTileEnts;
	}
	public JLabel getLblSummaryAmountTileEnts() {
		return lblSummaryAmountTileEnts;
	}
	public JLabel getLblSummaryTimingEntities() {
		return lblSummaryTimingEntities;
	}
	public JLabel getLblSummaryAmountEntities() {
		return lblSummaryAmountEntities;
	}
	public JLabel getLblSummaryTimingHandlers() {
		return lblSummaryTimingHandlers;
	}
	public JLabel getLblSummaryAmountHandlers() {
		return lblSummaryAmountHandlers;
	}
	public JLabel getLblSummaryTimingTotal() {
		return lblSummaryTimingTotal;
	}	
	public JLabel getLblSummaryTimingTick() {
		return lblSummaryTimingTick;
	}	
	public JLabel getLblSummaryTickChart() {
		return lblSummaryTickChart;
	}	
	public JLabel getLblSummaryTimingWorldTick() {
		return lblSummaryTimingWorldTick;
	}	
	//public JLabel getLblSummaryTimingGlobalUpdate() {
	//	return lblSummaryTimingGlobalUpdate;
	//}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == btnRefreshEntityAmount){
			this.requestAmoutEntityUpdate();
		}
		
		else if ((e.getSource() == btnTimingTECenterMap) && (tableTimingTE.getSelectedRow() != -1)){
			int indexData = tableTimingTE.convertRowIndexToModel(tableTimingTE.getSelectedRow());
			StatsTileEntity data = DataCache.instance().getTimingTileEnts().get(indexData);
			
			CoordinatesBlock coord = data.getCoordinates();
			OverlayMeanTime.instance().setSelectedChunk(coord.dim, coord.x >> 4, coord.z >> 4);
			MwAPI.setCurrentDataProvider(OverlayMeanTime.instance());
			Minecraft.getMinecraft().displayGuiScreen(new MwGui(Mw.instance, coord.dim, coord.x, coord.z));			
		}
		
		else if ((e.getSource() == btnTimingTETeleport) && (tableTimingTE.getSelectedRow() != -1)){		
			int indexData = tableTimingTE.convertRowIndexToModel(tableTimingTE.getSelectedRow());	
			StatsTileEntity data = DataCache.instance().getTimingTileEnts().get(indexData);
			
			CoordinatesBlock coord = data.getCoordinates();
			modOpis.selectedBlock = coord;
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND, DataReq.TELEPORT, DataReq.BLOCK, coord));
			Minecraft.getMinecraft().setIngameFocus();			
		}
		
		else if ((e.getSource() == btnTimingEntCenterMap) && (tableTimingEnt.getSelectedRow() != -1)){
			int indexData = tableTimingEnt.convertRowIndexToModel(tableTimingEnt.getSelectedRow());
			StatsEntity data = DataCache.instance().getTimingEntities().get(indexData);
			
			CoordinatesBlock coord = data.getCoordinates();
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.OVERLAY, DataReq.CHUNK, DataReq.ENTITIES));
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.LIST,    DataReq.CHUNK, DataReq.ENTITIES, data.getChunk()));			
			OverlayMeanTime.instance().setSelectedChunk(coord.dim, coord.x >> 4, coord.z >> 4);
			MwAPI.setCurrentDataProvider(OverlayEntityPerChunk.instance());
			Minecraft.getMinecraft().displayGuiScreen(new MwGui(Mw.instance, coord.dim, coord.x, coord.z));			
		}
		
		else if ((e.getSource() == btnTimingEntTeleport) && (tableTimingEnt.getSelectedRow() != -1)){		
			int indexData = tableTimingEnt.convertRowIndexToModel(tableTimingEnt.getSelectedRow());	
			StatsEntity data = DataCache.instance().getTimingEntities().get(indexData);
			
			int eid = data.getID();
			int dim = data.getCoordinates().dim;
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND, DataReq.TELEPORT, DataReq.ENTITIES, new TargetEntity(eid, dim)));
			Minecraft.getMinecraft().setIngameFocus();			
		}
		
		else if ((e.getSource() == btnTimingTERefresh) || (e.getSource() == btnTimingEntRefresh) || (e.getSource() == btnTimingHandlerRefresh) || (e.getSource() == btnTimingChunkRefresh) || (e.getSource() == btnSummaryReset)){
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND, DataReq.START, DataReq.NONE));
		}
		
		else if ((e.getSource() == btnTimingEntKillTarget)){
			
		}
		
		else if ((e.getSource() == btnAmountKillAll) && (tableEntityList.getSelectedRow() != -1)){
			int indexData = tableEntityList.convertRowIndexToModel(tableEntityList.getSelectedRow());
			AmountHolder data = DataCache.instance().getAmountEntities().get(indexData);
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND, DataReq.KILLALL, DataReq.NONE, new SerialString(data.key)));
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.LIST, DataReq.AMOUNT, DataReq.ENTITIES));
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
