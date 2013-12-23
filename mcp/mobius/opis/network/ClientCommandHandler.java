package mcp.mobius.opis.network;

import java.util.ArrayList;
import java.util.logging.Level;

import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.client.OpisClientTickHandler;
import mcp.mobius.opis.data.client.TickHandlerClientProfiler;
import mcp.mobius.opis.data.holders.TickHandlerStats;
import mcp.mobius.opis.data.managers.MetaManager;
import mcp.mobius.opis.data.managers.TickHandlerManager;

public class ClientCommandHandler {
	
	private static ClientCommandHandler _instance;
	private ClientCommandHandler(){}
	
	public static ClientCommandHandler instance(){
		if(_instance == null)
			_instance = new ClientCommandHandler();			
		return _instance;
	}
	
	public void handle(byte cmd){
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
	}
}
