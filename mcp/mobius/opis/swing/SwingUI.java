package mcp.mobius.opis.swing;

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
import java.util.HashSet;

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
import mcp.mobius.opis.gui.overlay.OverlayMeanTime;
import mcp.mobius.opis.gui.overlay.entperchunk.OverlayEntityPerChunk;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.Packet_ReqData;
import net.minecraft.client.Minecraft;

import javax.swing.ListSelectionModel;
import javax.swing.JSeparator;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;

public class SwingUI extends JFrame implements WindowListener{

	public static HashSet<JButtonAccess> registeredButtons = new HashSet<JButtonAccess>();
	
	private static SwingUI _instance = new SwingUI();
	public  static SwingUI instance(){
		return _instance;
	}
	
	private JPanel contentPane;

	private PanelSummary        panelSummary;
	private PanelPlayers        panelPlayers;	
	private PanelAmountEntities panelAmountEntities;	
	private PanelTimingTileEnts panelTimingTileEnts;
	private PanelTimingEntities panelTimingEntities;
	private PanelTimingHandlers panelTimingHandlers;	
	private PanelTimingChunks   panelTimingChunks;
	
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
		setBounds(100, 100, 893, 455);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		panelSummary = new PanelSummary();
		tabbedPane.addTab("Summary", null, panelSummary, null);		
		
		panelPlayers = new PanelPlayers();
		tabbedPane.addTab("Players", null, panelPlayers, null);		

		panelAmountEntities = new PanelAmountEntities();
		tabbedPane.addTab("Entities amount", null, panelAmountEntities, null);		
		
		panelTimingTileEnts = new PanelTimingTileEnts();
		tabbedPane.addTab("TileEntities timing", null, panelTimingTileEnts, null);		
		
		panelTimingEntities = new PanelTimingEntities();
		tabbedPane.addTab("Entities timing", null, panelTimingEntities, null);		
		
		panelTimingHandlers = new PanelTimingHandlers();
		tabbedPane.addTab("Handlers timing", null, panelTimingHandlers, null);		
		
		panelTimingChunks = new PanelTimingChunks();
		tabbedPane.addTab("Chunks timing", null, panelTimingChunks, null);		
		
		this.addWindowListener(this);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {
		PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_UNREGISTER_SWING));
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
	
	
	public PanelTimingChunks getPanelTimingChunks() {
		return panelTimingChunks;
	}
	public PanelTimingHandlers getPanelTimingHandlers() {
		return panelTimingHandlers;
	}
	public PanelTimingEntities getPanelTimingEntities() {
		return panelTimingEntities;
	}
	public PanelTimingTileEnts getPanelTimingTileEnts() {
		return panelTimingTileEnts;
	}
	public PanelAmountEntities getPanelAmountEntities() {
		return panelAmountEntities;
	}
	public PanelPlayers getPanelPlayers() {
		return panelPlayers;
	}
	public PanelSummary getPanelSummary() {
		return panelSummary;
	}
	
	public void setTextRunButton(String s){
		this.getPanelSummary().getBtnRun().setText(s);
		this.getPanelTimingTileEnts().getBtnRun().setText(s);
		this.getPanelTimingEntities().getBtnRun().setText(s);
		this.getPanelTimingHandlers().getBtnRun().setText(s);
		this.getPanelTimingChunks().getBtnRun().setText(s);		
	}
}
