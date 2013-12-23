package mcp.mobius.opis.network;

import java.util.ArrayList;
import java.util.logging.Level;

import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.client.OpisClientTickHandler;
import mcp.mobius.opis.data.client.TickHandlerClientManager;
import mcp.mobius.opis.data.client.TickHandlerClientProfiler;
import mcp.mobius.opis.data.holders.TickHandlerStats;

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
			
			modOpis.profilerRun = true;
			OpisClientTickHandler.instance.profilerRunningTicks = 0;
			TickHandlerClientManager.startStats.clear();
			TickHandlerClientManager.endStats.clear();		
	
			ProfilerRegistrar.registerProfilerTick(new TickHandlerClientProfiler());			
		}
		else if (cmd == ClientCommand.SHOW_RENDER_TICK){
			modOpis.log.log(Level.INFO, "=== RENDER TICK ===");
			ArrayList<TickHandlerStats> stats = TickHandlerClientManager.getCumulatedStats();
			for (TickHandlerStats stat : stats){
				System.out.printf("%s \n", stat);
			}
		}
	}
}
