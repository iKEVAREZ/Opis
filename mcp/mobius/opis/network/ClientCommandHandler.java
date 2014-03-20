package mcp.mobius.opis.network;

import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.client.OpisClientTickHandler;
import mcp.mobius.opis.data.client.DataCache;
import mcp.mobius.opis.data.client.TickHandlerClientProfiler;
import mcp.mobius.opis.data.holders.TickHandlerStats;
import mcp.mobius.opis.data.managers.MetaManager;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.gui.swing.SwingUI;
import mcp.mobius.opis.network.client.Packet_ReqData;
import mcp.mobius.opis.network.enums.ClientCommand;
import mcp.mobius.opis.network.enums.DataReq;

public class ClientCommandHandler {
	
	private static ClientCommandHandler _instance;
	private ClientCommandHandler(){}
	
	public static ClientCommandHandler instance(){
		if(_instance == null)
			_instance = new ClientCommandHandler();			
		return _instance;
	}
	
	public void handle(ClientCommand cmd){
		if (cmd == ClientCommand.START_PROFILING){
			modOpis.log.log(Level.INFO, "Started profiling");
			
			MetaManager.reset();		
			modOpis.profilerRun = true;
			ProfilerRegistrar.turnOn();		
		}
		else if (cmd == ClientCommand.SHOW_RENDER_TICK){
			modOpis.log.log(Level.INFO, "=== RENDER TICK ===");
			ArrayList<TickHandlerStats> stats = TickHandlerManager.getCumulatedStats();
			for (TickHandlerStats stat : stats){
				System.out.printf("%s \n", stat);
			}
		}
		
		else if (cmd == ClientCommand.SHOW_SWING){
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.LIST,  DataReq.TIMING, DataReq.TILETENTS));
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.LIST,  DataReq.TIMING, DataReq.ENTITIES));	
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.LIST,  DataReq.TIMING, DataReq.HANDLERS));
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.LIST,  DataReq.TIMING, DataReq.CHUNK));
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.VALUE, DataReq.TIMING, DataReq.WORLDTICK));
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.LIST,  DataReq.AMOUNT, DataReq.ENTITIES));	
			SwingUI.instance().showUI();		
		}
		
		else if (cmd == ClientCommand.CLEAR_SELECTION){
			modOpis.selectedBlock = null;			
		}
	}
}
