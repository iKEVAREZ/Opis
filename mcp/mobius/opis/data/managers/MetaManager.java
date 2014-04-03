package mcp.mobius.opis.data.managers;

import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;
import mcp.mobius.mobiuscore.profiler_v2.ProfilerSection;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.server.EntUpdateProfiler;
import mcp.mobius.opis.data.server.WorldTickProfiler;
import mcp.mobius.opis.events.OpisServerTickHandler;

public class MetaManager {

	public static void reset(){
		modOpis.profilerRun   = false;
		modOpis.selectedBlock = null;
		OpisServerTickHandler.instance.profilerRunningTicks = 0;
		TileEntityManager.references.clear();
		TileEntityManager.stats.clear();
		EntityManager.INSTANCE.stats.clear();
		TickHandlerManager.startStats.clear();
		TickHandlerManager.endStats.clear();
		GlobalTimingManager.INSTANCE.worldTickStats.clear();		
		GlobalTimingManager.INSTANCE.entUpdateStats.clear();
		ProfilerRegistrar.turnOff();
		
		ProfilerSection.desactivateAll();
	}
	
}
