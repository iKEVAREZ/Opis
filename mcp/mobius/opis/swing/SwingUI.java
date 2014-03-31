package mcp.mobius.opis.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;

import javax.swing.SwingConstants;

import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.opis.api.IMessageHandler;
import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.api.TabPanelRegistrar;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.Packet_ReqData;
import mcp.mobius.opis.network.packets.server.NetDataRaw;
import mcp.mobius.opis.swing.panels.PanelAmountEntities;
import mcp.mobius.opis.swing.panels.PanelPlayers;
import mcp.mobius.opis.swing.panels.PanelSummary;
import mcp.mobius.opis.swing.panels.PanelTimingChunks;
import mcp.mobius.opis.swing.panels.PanelTimingEntities;
import mcp.mobius.opis.swing.panels.PanelTimingHandlers;
import mcp.mobius.opis.swing.panels.PanelTimingTileEnts;
import mcp.mobius.opis.swing.widgets.JButtonAccess;

public class SwingUI extends JFrame implements WindowListener, IMessageHandler{

	public static HashSet<JButtonAccess> registeredButtons = new HashSet<JButtonAccess>();
	
	private static SwingUI _instance = new SwingUI();
	public  static SwingUI instance(){
		return _instance;
	}
	
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	
	
	/*
	private PanelSummary        panelSummary;
	private PanelPlayers        panelPlayers;	
	private PanelAmountEntities panelAmountEntities;	
	private PanelTimingTileEnts panelTimingTileEnts;
	private PanelTimingEntities panelTimingEntities;
	private PanelTimingHandlers panelTimingHandlers;	
	private PanelTimingChunks   panelTimingChunks;
	*/
	
	public void showUI(){
		EventQueue.invokeLater(new Runnable() {
			@Override
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
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 893, 455);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		/*
		for (ITabPanel panel : TabPanelRegistrar.INSTANCE.getTabs()){
			tabbedPane.addTab(panel.getTabTitle(), (JPanel)panel);
		}
		*/
		
		/*
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
		*/
		
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
	
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
	
	/*
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
	*/
	
	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {
		switch(msg){
		case STATUS_ACCESS_LEVEL:{
			AccessLevel level =  AccessLevel.values()[((SerialInt)rawdata.value).value];
			for (JButtonAccess button : SwingUI.registeredButtons){
				if (level.ordinal() < button.getAccessLevel().ordinal()){
					button.setEnabled(false);
				} else {
					button.setEnabled(true);
				}
			}
			break;
		}
		case CLIENT_SHOW_SWING:{
			this.showUI();
			break;
		}
		default:
			return false;
			
		}
		return true;		
	}
}
