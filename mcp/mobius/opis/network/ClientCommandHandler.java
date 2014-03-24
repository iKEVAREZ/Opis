package mcp.mobius.opis.network;

import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.client.DataCache;
import mcp.mobius.opis.data.client.TickHandlerClientProfiler;
import mcp.mobius.opis.data.holders.stats.StatsTickHandler;
import mcp.mobius.opis.data.managers.MetaManager;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.events.OpisClientTickHandler;
import mcp.mobius.opis.network.enums.ClientCommand;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.Packet_ReqData;
import mcp.mobius.opis.swing.SwingUI;

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
			ArrayList<StatsTickHandler> stats = TickHandlerManager.getCumulatedStats();
			for (StatsTickHandler stat : stats){
				System.out.printf("%s \n", stat);
			}
		}
		
		else if (cmd == ClientCommand.SHOW_SWING){
			SwingUI.instance().showUI();		
		}
		
		else if (cmd == ClientCommand.CLEAR_SELECTION){
			modOpis.selectedBlock = null;			
		}
	}
}
