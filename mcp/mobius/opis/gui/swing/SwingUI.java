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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.basetypes.SerialString;
import mcp.mobius.opis.data.holders.basetypes.TargetEntity;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
import mcp.mobius.opis.data.holders.stats.StatsPlayer;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.network.client.Packet_ReqData;
import mcp.mobius.opis.network.enums.DataReq;
import mcp.mobius.opis.overlay.OverlayMeanTime;
import mcp.mobius.opis.overlay.entperchunk.OverlayEntityPerChunk;
import net.minecraft.client.Minecraft;

import javax.swing.ListSelectionModel;
import javax.swing.JSeparator;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;

public class SwingUI extends JFrame implements  ActionListener, ItemListener, WindowListener{

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
	private JButton btnSummaryRefresh;
	private JButton btnTimingEntPull;
	private JButton btnAmountKillAll;
	private JLabel lblSummary_11;
	private JLabel lblSummary_12;
	private JLabel lblSummaryTimingTick;
	private JLabel lblSummaryTickChart;
	private JLabel lblSummary_13;
	private JLabel lblSummaryTimingWorldTick;
	private JLabel lblSummary_14;
	private JLabel lblNewLabel;
	private JLabel lblSummary_15;
	private JLabel lblSummaryDownload;
	private JLabel lblSummaryUpload;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private JLabel lblNewLabel_6;
	private JButton btnTimingChunkCenterMap;
	private JButton btnTimingChunkTeleport;
	private JProgressBar progBarSummaryOpis;
	private JPanel panelPlayers;
	private JButton btnPlayersCenterMap;
	private JButton btnPlayersTeleport;
	private JButton btnPlayersPull;
	private JScrollPane scrollPanePlayers;
	private JTable tablePlayers;
	private JButton btnPlayers_Dummy1;
	private JButton btnPlayers_Dummy2;
	private JLabel lblSummaryTimeStampLastRun;
	
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
		setBounds(100, 100, 838, 449);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		panelSummary = new JPanel();
		tabbedPane.addTab("Summary", null, panelSummary, null);
		GridBagLayout gbl_panelSummary = new GridBagLayout();
		gbl_panelSummary.columnWidths = new int[]{0, 50, 20, 0, 70, 0, 20, 50, 50, 0, 20, 70, 0, 0, 42, 0, 0};
		gbl_panelSummary.rowHeights = new int[]{0, -4, 0, 0, 0, 0, 20, 10, 30, 0, 0, 0, 0, 0};
		gbl_panelSummary.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panelSummary.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panelSummary.setLayout(gbl_panelSummary);
		
		lblSummary_1 = new JLabel("Update time");
		GridBagConstraints gbc_lblSummary_1 = new GridBagConstraints();
		gbc_lblSummary_1.anchor = GridBagConstraints.EAST;
		gbc_lblSummary_1.gridwidth = 2;
		gbc_lblSummary_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary_1.gridx = 4;
		gbc_lblSummary_1.gridy = 1;
		panelSummary.add(lblSummary_1, gbc_lblSummary_1);
		
		label = new JLabel("Amount");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.LINE_END;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 7;
		gbc_label.gridy = 1;
		panelSummary.add(label, gbc_label);
		
		lblNewLabel_6 = new JLabel("Amount");
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_6.gridwidth = 2;
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_6.gridx = 11;
		gbc_lblNewLabel_6.gridy = 1;
		panelSummary.add(lblNewLabel_6, gbc_lblNewLabel_6);
		
		btnSummaryRefresh = new JButton("Run Opis");
		GridBagConstraints gbc_btnSummaryReset = new GridBagConstraints();
		gbc_btnSummaryReset.anchor = GridBagConstraints.EAST;
		gbc_btnSummaryReset.insets = new Insets(0, 0, 5, 0);
		gbc_btnSummaryReset.gridx = 15;
		gbc_btnSummaryReset.gridy = 1;
		btnSummaryRefresh.addActionListener(this);
		panelSummary.add(btnSummaryRefresh, gbc_btnSummaryReset);
		
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
		
		lblNewLabel = new JLabel("Upload");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 9;
		gbc_lblNewLabel.gridy = 2;
		panelSummary.add(lblNewLabel, gbc_lblNewLabel);
		
		lblSummaryUpload = new JLabel("0");
		GridBagConstraints gbc_lblSummaryUpload = new GridBagConstraints();
		gbc_lblSummaryUpload.anchor = GridBagConstraints.EAST;
		gbc_lblSummaryUpload.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummaryUpload.gridx = 11;
		gbc_lblSummaryUpload.gridy = 2;
		panelSummary.add(lblSummaryUpload, gbc_lblSummaryUpload);
		
		lblNewLabel_4 = new JLabel("kB/s");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 12;
		gbc_lblNewLabel_4.gridy = 2;
		panelSummary.add(lblNewLabel_4, gbc_lblNewLabel_4);
		
		progBarSummaryOpis = new JProgressBar();
		GridBagConstraints gbc_progBarSummaryOpis = new GridBagConstraints();
		gbc_progBarSummaryOpis.fill = GridBagConstraints.HORIZONTAL;
		gbc_progBarSummaryOpis.insets = new Insets(0, 0, 5, 0);
		gbc_progBarSummaryOpis.gridx = 15;
		gbc_progBarSummaryOpis.gridy = 2;
		panelSummary.add(progBarSummaryOpis, gbc_progBarSummaryOpis);
		
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
		
		lblSummary_15 = new JLabel("Download");
		GridBagConstraints gbc_lblSummary_15 = new GridBagConstraints();
		gbc_lblSummary_15.anchor = GridBagConstraints.WEST;
		gbc_lblSummary_15.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummary_15.gridx = 9;
		gbc_lblSummary_15.gridy = 3;
		panelSummary.add(lblSummary_15, gbc_lblSummary_15);
		
		lblSummaryDownload = new JLabel("0");
		GridBagConstraints gbc_lblSummaryDownload = new GridBagConstraints();
		gbc_lblSummaryDownload.anchor = GridBagConstraints.EAST;
		gbc_lblSummaryDownload.insets = new Insets(0, 0, 5, 5);
		gbc_lblSummaryDownload.gridx = 11;
		gbc_lblSummaryDownload.gridy = 3;
		panelSummary.add(lblSummaryDownload, gbc_lblSummaryDownload);
		
		lblNewLabel_5 = new JLabel("kB/s");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 12;
		gbc_lblNewLabel_5.gridy = 3;
		panelSummary.add(lblNewLabel_5, gbc_lblNewLabel_5);
		
		lblSummaryTimeStampLastRun = new JLabel("Last run : Never");
		GridBagConstraints gbc_lblSummaryTimeStampLastRun = new GridBagConstraints();
		gbc_lblSummaryTimeStampLastRun.anchor = GridBagConstraints.EAST;
		gbc_lblSummaryTimeStampLastRun.insets = new Insets(0, 0, 5, 0);
		gbc_lblSummaryTimeStampLastRun.gridx = 15;
		gbc_lblSummaryTimeStampLastRun.gridy = 3;
		panelSummary.add(lblSummaryTimeStampLastRun, gbc_lblSummaryTimeStampLastRun);
		
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
		gbc_lblSummaryTickChart.gridwidth = 16;
		gbc_lblSummaryTickChart.gridx = 0;
		gbc_lblSummaryTickChart.gridy = 12;
		panelSummary.add(lblSummaryTickChart, gbc_lblSummaryTickChart);
		
		panelPlayers = new JPanel();
		tabbedPane.addTab("Players", null, panelPlayers, null);
		panelPlayers.setLayout(new MigLayout("", "[][][][][][grow][]", "[][grow]"));
		
		btnPlayersCenterMap = new JButton("Center Map");
		btnPlayersCenterMap.addActionListener(this);
		panelPlayers.add(btnPlayersCenterMap, "cell 0 0,alignx left");
		
		btnPlayersTeleport = new JButton("Teleport");
		btnPlayersTeleport.addActionListener(this);
		panelPlayers.add(btnPlayersTeleport, "cell 1 0,alignx left");
		
		btnPlayersPull = new JButton("Pull");
		btnPlayersPull.addActionListener(this);
		panelPlayers.add(btnPlayersPull, "cell 2 0,alignx left");
		
		btnPlayers_Dummy1 = new JButton("Kill");
		btnPlayers_Dummy1.setEnabled(false);
		panelPlayers.add(btnPlayers_Dummy1, "cell 3 0");
		
		btnPlayers_Dummy2 = new JButton("Morph");
		btnPlayers_Dummy2.setEnabled(false);
		panelPlayers.add(btnPlayers_Dummy2, "cell 4 0");
		
		scrollPanePlayers = new JScrollPane();
		panelPlayers.add(scrollPanePlayers, "cell 0 1 7 1,grow");
		
		tablePlayers = new JTable();
		tablePlayers.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null},
			},
			new String[] {
				"Name", "Dimension", "Coordinates"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Integer.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tablePlayers.getColumnModel().getColumn(2).setPreferredWidth(96);
		tablePlayers.setAutoCreateRowSorter(true);
		scrollPanePlayers.setViewportView(tablePlayers);
		
		panelEntityAmount = new JPanel();
		tabbedPane.addTab("Entity Amount", null, panelEntityAmount, null);
		
		chkBoxDisplayAll = new JCheckBox("Filter Entities");
		chkBoxDisplayAll.addItemListener(this);
		panelEntityAmount.setLayout(new MigLayout("", "[left][left][481.00px,grow][right]", "[][324px,grow][]"));
		panelEntityAmount.add(chkBoxDisplayAll, "cell 0 0,alignx left,aligny center");
		
		btnRefreshEntityAmount = new JButton("Refresh");
		btnRefreshEntityAmount.addActionListener(this);
		
		btnAmountKillAll = new JButton("Kill All");
		btnAmountKillAll.addActionListener(this);
		panelEntityAmount.add(btnAmountKillAll, "cell 1 0,alignx left,aligny center");
		panelEntityAmount.add(btnRefreshEntityAmount, "cell 3 0,alignx right,aligny center");
		
		scrollPaneEntityAmount = new JScrollPane();
		panelEntityAmount.add(scrollPaneEntityAmount, "cell 0 1 4 1,grow");
		
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
		panelEntityAmount.add(labelAmountTotal, "cell 0 2,grow");
		
		labelAmountValue = new JLabel("0");
		labelAmountValue.setHorizontalAlignment(SwingConstants.CENTER);
		panelEntityAmount.add(labelAmountValue, "cell 3 2,grow");
		
		panelTimingTE = new JPanel();
		tabbedPane.addTab("TileEntity Timing", null, panelTimingTE, null);
		
		btnTimingTECenterMap = new JButton("Center Map");
		btnTimingTECenterMap.addActionListener(this);
		panelTimingTE.setLayout(new MigLayout("", "[left][left][233px,grow][right]", "[][334px,grow][]"));
		panelTimingTE.add(btnTimingTECenterMap, "cell 0 0,alignx center,aligny center");
		
		btnTimingTETeleport = new JButton("Teleport");
		btnTimingTETeleport.addActionListener(this);
		panelTimingTE.add(btnTimingTETeleport, "cell 1 0,alignx center,aligny center");
		
		btnTimingTERefresh = new JButton("Run Opis");
		btnTimingTERefresh.addActionListener(this);
		panelTimingTE.add(btnTimingTERefresh, "cell 3 0,alignx right,aligny center");
		
		JScrollPane scrollPaneTimingTE = new JScrollPane();
		panelTimingTE.add(scrollPaneTimingTE, "cell 0 1 4 1,grow");
		
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
				String.class, String.class, Integer.class, Object.class, StatAbstract.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tableTimingTE.setAutoCreateRowSorter(true);
		//tableTimingTE.getSelectionModel().addListSelectionListener(this);		
		scrollPaneTimingTE.setViewportView(tableTimingTE);
		
		lblTimingTEValue = new JLabel("Total update time : 0 µs");
		panelTimingTE.add(lblTimingTEValue, "cell 0 2 4 1,alignx center,aligny center");
		
		panelTimingEnt = new JPanel();
		tabbedPane.addTab("EntityTiming", null, panelTimingEnt, null);
		
		btnTimingEntCenterMap = new JButton("Center Map");
		btnTimingEntCenterMap.addActionListener(this);
		panelTimingEnt.setLayout(new MigLayout("", "[left][left][left][grow][right]", "[][334px,grow][]"));
		panelTimingEnt.add(btnTimingEntCenterMap, "cell 0 0,alignx center,aligny center");
		
		btnTimingEntTeleport = new JButton("Teleport");
		btnTimingEntTeleport.addActionListener(this);
		panelTimingEnt.add(btnTimingEntTeleport, "cell 1 0,alignx center,aligny center");
		
		btnTimingEntRefresh = new JButton("Run Opis");
		btnTimingEntRefresh.addActionListener(this);
		panelTimingEnt.add(btnTimingEntRefresh, "cell 4 0,alignx right,aligny center");
		
		btnTimingEntPull = new JButton("Pull");
		btnTimingEntPull.addActionListener(this);		
		panelTimingEnt.add(btnTimingEntPull, "cell 2 0,alignx center,aligny center");
		
		scrollPaneTimingEnt = new JScrollPane();
		panelTimingEnt.add(scrollPaneTimingEnt, "cell 0 1 5 1,grow");
		
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
				String.class, Integer.class, Integer.class, Object.class, StatAbstract.class, Integer.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tableTimingEnt.setAutoCreateRowSorter(true);
		scrollPaneTimingEnt.setViewportView(tableTimingEnt);
		
		lblTimingEntValue = new JLabel("Total update time : 0 µs");
		panelTimingEnt.add(lblTimingEntValue, "cell 0 2 5 1,alignx center,aligny center");
		
		panelTimingHandler = new JPanel();
		tabbedPane.addTab("Handlers Timing", null, panelTimingHandler, null);
		
		btnTimingHandlerRefresh = new JButton("Run Opis");
		btnTimingHandlerRefresh.addActionListener(this);
		panelTimingHandler.setLayout(new MigLayout("", "[495px,grow][]", "[][339px,grow][]"));
		panelTimingHandler.add(btnTimingHandlerRefresh, "cell 1 0,alignx right,aligny center");
		
		scrollPaneTimingHandler = new JScrollPane();
		panelTimingHandler.add(scrollPaneTimingHandler, "cell 0 1 2 1,grow");
		
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
				String.class, StatAbstract.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tableTimingHandler.setAutoCreateRowSorter(true);
		scrollPaneTimingHandler.setViewportView(tableTimingHandler);
		
		lblTimingHandlerValue = new JLabel("Total update time : 0 µs");
		panelTimingHandler.add(lblTimingHandlerValue, "cell 0 2 2 1,alignx center,aligny center");
		
		panelTimingChunk = new JPanel();
		tabbedPane.addTab("Chunk Timing", null, panelTimingChunk, null);
		
		btnTimingChunkCenterMap = new JButton("Center Map");
		btnTimingChunkCenterMap.addActionListener(this);
		panelTimingChunk.setLayout(new MigLayout("", "[left][left][97px,grow][right]", "[][359px,grow]"));
		panelTimingChunk.add(btnTimingChunkCenterMap, "cell 0 0,alignx left,aligny center");
		
		btnTimingChunkTeleport = new JButton("Teleport");
		btnTimingChunkTeleport.addActionListener(this);		
		panelTimingChunk.add(btnTimingChunkTeleport, "cell 1 0,alignx left,aligny center");
		
		scrollPaneTimingChunk = new JScrollPane();
		panelTimingChunk.add(scrollPaneTimingChunk, "cell 0 1 4 1,grow");
		
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
				Integer.class, String.class, Integer.class, Integer.class, StatAbstract.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tableTimingChunk.setAutoCreateRowSorter(true);		
		scrollPaneTimingChunk.setViewportView(tableTimingChunk);
		
		btnTimingChunkRefresh = new JButton("Run Opis");
		btnTimingChunkRefresh.addActionListener(this);
		panelTimingChunk.add(btnTimingChunkRefresh, "cell 3 0,alignx right,aligny center");
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		
		this.addWindowListener(this);
		
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
		
		for (int i = 0; i < tablePlayers.getColumnCount(); i++)
			tablePlayers.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);		
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
	public JLabel getLblSummaryUpload() {
		return lblSummaryUpload;
	}
	public JLabel getLblSummaryDownload() {
		return lblSummaryDownload;
	}	
	
	public JButton getBtnSummaryRefresh() {
		return btnSummaryRefresh;
	}
	public JButton getBtnTimingTERefresh() {
		return btnTimingTERefresh;
	}
	public JButton getBtnTimingEntRefresh() {
		return btnTimingEntRefresh;
	}
	public JButton getBtnTimingHandlerRefresh() {
		return btnTimingHandlerRefresh;
	}
	public JButton getBtnTimingChunkRefresh() {
		return btnTimingChunkRefresh;
	}	
	
	//public JLabel getLblSummaryTimingGlobalUpdate() {
	//	return lblSummaryTimingGlobalUpdate;
	//}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == btnRefreshEntityAmount){
			this.requestAmoutEntityUpdate();
		}
		
		else if ((e.getSource() == this.btnTimingTECenterMap) && (this.tableTimingTE.getSelectedRow() != -1)){
			int indexData = tableTimingTE.convertRowIndexToModel(tableTimingTE.getSelectedRow());
			StatsTileEntity data = DataCache.instance().getTimingTileEnts().get(indexData);
			
			CoordinatesBlock coord = data.getCoordinates();
			OverlayMeanTime.instance().setSelectedChunk(coord.dim, coord.x >> 4, coord.z >> 4);
			MwAPI.setCurrentDataProvider(OverlayMeanTime.instance());
			Minecraft.getMinecraft().displayGuiScreen(new MwGui(Mw.instance, coord.dim, coord.x, coord.z));			
		}
		
		else if ((e.getSource() == this.btnTimingTETeleport) && (this.tableTimingTE.getSelectedRow() != -1)){		
			int indexData = tableTimingTE.convertRowIndexToModel(tableTimingTE.getSelectedRow());	
			StatsTileEntity data = DataCache.instance().getTimingTileEnts().get(indexData);
			
			CoordinatesBlock coord = data.getCoordinates();
			modOpis.selectedBlock = coord;
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND_TELEPORT_BLOCK, coord));
			Minecraft.getMinecraft().setIngameFocus();			
		}
		
		else if ((e.getSource() == this.btnTimingEntCenterMap) && (this.tableTimingEnt.getSelectedRow() != -1)){
			int indexData = tableTimingEnt.convertRowIndexToModel(tableTimingEnt.getSelectedRow());
			StatsEntity data = DataCache.instance().getTimingEntities().get(indexData);
			
			CoordinatesBlock coord = data.getCoordinates();
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.OVERLAY_CHUNK_ENTITIES));
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.LIST_CHUNK_ENTITIES, data.getChunk()));			
			OverlayMeanTime.instance().setSelectedChunk(coord.dim, coord.x >> 4, coord.z >> 4);
			MwAPI.setCurrentDataProvider(OverlayEntityPerChunk.instance());
			Minecraft.getMinecraft().displayGuiScreen(new MwGui(Mw.instance, coord.dim, coord.x, coord.z));			
		}
		
		else if ((e.getSource() == this.btnTimingEntTeleport) && (this.tableTimingEnt.getSelectedRow() != -1)){		
			int indexData = tableTimingEnt.convertRowIndexToModel(tableTimingEnt.getSelectedRow());	
			StatsEntity data = DataCache.instance().getTimingEntities().get(indexData);
			
			int eid = data.getID();
			int dim = data.getCoordinates().dim;
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND_TELEPORT_TO_ENTITY, new TargetEntity(eid, dim)));
			Minecraft.getMinecraft().setIngameFocus();			
		}
		
		else if ((e.getSource() == this.btnTimingEntPull) && (this.tableTimingEnt.getSelectedRow() != -1)){		
			int indexData = tableTimingEnt.convertRowIndexToModel(tableTimingEnt.getSelectedRow());	
			StatsEntity data = DataCache.instance().getTimingEntities().get(indexData);
			
			int eid = data.getID();
			int dim = data.getCoordinates().dim;
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND_TELEPORT_PULL_ENTITY, new TargetEntity(eid, dim)));
		}		
		
		else if ((e.getSource() == this.btnTimingTERefresh) || (e.getSource() == btnTimingEntRefresh) || (e.getSource() == btnTimingHandlerRefresh) || (e.getSource() == btnTimingChunkRefresh) || (e.getSource() == btnSummaryRefresh)){
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND_START));
		}
		
		else if ((e.getSource() == this.btnAmountKillAll) && (this.tableEntityList.getSelectedRow() != -1)){
			int indexData = tableEntityList.convertRowIndexToModel(tableEntityList.getSelectedRow());
			AmountHolder data = DataCache.instance().getAmountEntities().get(indexData);
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND_KILLALL, new SerialString(data.key)));
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.LIST_AMOUNT_ENTITIES));
		}
		
		else if ((e.getSource() == this.btnTimingChunkTeleport) && (this.tableTimingChunk.getSelectedRow() != -1)){
			int indexData = tableTimingChunk.convertRowIndexToModel(tableTimingChunk.getSelectedRow());
			StatsChunk data = DataCache.instance().getTimingChunks().get(indexData);
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND_TELEPORT_CHUNK, data.getChunk()));
		}
		
		else if ((e.getSource() == this.btnTimingChunkCenterMap) && (this.tableTimingChunk.getSelectedRow() != -1)){
			int indexData = tableTimingChunk.convertRowIndexToModel(tableTimingChunk.getSelectedRow());
			StatsChunk data = DataCache.instance().getTimingChunks().get(indexData);
			
			OverlayMeanTime.instance().setSelectedChunk(data.getChunk().dim, data.getChunk().chunkX, data.getChunk().chunkZ);
			MwAPI.setCurrentDataProvider(OverlayMeanTime.instance());
			Minecraft.getMinecraft().displayGuiScreen(new MwGui(Mw.instance, data.getChunk().dim, data.getChunk().x + 8, data.getChunk().z + 8));			
		}		
		
		else if ((e.getSource() == this.btnPlayersCenterMap) && (this.tablePlayers.getSelectedRow() != -1)){
			int indexData = tablePlayers.convertRowIndexToModel(tablePlayers.getSelectedRow());
			StatsPlayer data = DataCache.instance().getListPlayers().get(indexData);
			
			CoordinatesBlock coord = data.getCoordinates();
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.OVERLAY_CHUNK_ENTITIES));
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.LIST_CHUNK_ENTITIES, data.getChunk()));			
			OverlayMeanTime.instance().setSelectedChunk(coord.dim, coord.x >> 4, coord.z >> 4);
			MwAPI.setCurrentDataProvider(OverlayEntityPerChunk.instance());
			Minecraft.getMinecraft().displayGuiScreen(new MwGui(Mw.instance, coord.dim, coord.x, coord.z));			
		}		
		
		else if ((e.getSource() == this.btnPlayersTeleport) && (this.tablePlayers.getSelectedRow() != -1)){		
			int indexData = tablePlayers.convertRowIndexToModel(tablePlayers.getSelectedRow());
			StatsPlayer data = DataCache.instance().getListPlayers().get(indexData);
			
			int eid = data.getEID();
			int dim = data.getCoordinates().dim;
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND_TELEPORT_TO_ENTITY, new TargetEntity(eid, dim)));
			Minecraft.getMinecraft().setIngameFocus();			
		}		
	
		else if ((e.getSource() == this.btnPlayersPull) && (this.tablePlayers.getSelectedRow() != -1)){		
			int indexData = tablePlayers.convertRowIndexToModel(tablePlayers.getSelectedRow());
			StatsPlayer data = DataCache.instance().getListPlayers().get(indexData);
			
			int eid = data.getEID();
			int dim = data.getCoordinates().dim;
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND_TELEPORT_PULL_ENTITY, new TargetEntity(eid, dim)));			
		}		
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getItem() == chkBoxDisplayAll){
			if      (e.getStateChange() == ItemEvent.SELECTED){
				PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND_FILTERING_TRUE));
				this.requestAmoutEntityUpdate();
			}
			else if (e.getStateChange() == ItemEvent.DESELECTED){
				PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND_FILTERING_FALSE));
				this.requestAmoutEntityUpdate();				
			}
		}
	}
	
	private void requestAmoutEntityUpdate(){
		PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.LIST_AMOUNT_ENTITIES));
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {
		PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND_UNREGISTER_SWING));
	}

	@Override
	public void windowClosing(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}

	public JProgressBar getProgBarSummaryOpis() {
		return progBarSummaryOpis;
	}
	public JTable getTablePlayers() {
		return tablePlayers;
	}
	public JLabel getLblSummaryTimeStampLastRun() {
		return lblSummaryTimeStampLastRun;
	}
}
