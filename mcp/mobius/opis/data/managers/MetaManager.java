package mcp.mobius.opis.data.managers;

import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;
import mcp.mobius.mobiuscore.profiler_v2.ProfilerSection;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.events.OpisServerTickHandler;

public class MetaManager {

	public static void reset(){
		modOpis.profilerRun   = false;
		modOpis.selectedBlock = null;
		OpisServerTickHandler.instance.profilerRunningTicks = 0;
		TileEntityManager.references.clear();
		TileEntityManager.stats.clear();
		TickHandlerManager.startStats.clear();
		TickHandlerManager.endStats.clear();
		ProfilerRegistrar.turnOff();
		
		ProfilerSection.resetAll();
		ProfilerSection.desactivateAll();
	}
	
}
