package mcp.mobius.opis.data.client;

import java.util.EnumSet;

import mcp.mobius.mobiuscore.profiler.IProfilerTick;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.client.OpisClientTickHandler;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.server.OpisServerTickHandler;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandlerClientProfiler implements IProfilerTick {

	public class Clock{
		public long startTime = 0;
		public long timeDelta = 0;
		public void start(){this.startTime = System.nanoTime();}
		public void stop(){this.timeDelta = System.nanoTime() - this.startTime;} 
	}	
	
	private String currentHandler = null;
	private Clock          clock  = new Clock();		
	
	@Override
	public void StartTickStart(IScheduledTickHandler ticker, EnumSet<TickType> ticksToRun) {
		if (OpisClientTickHandler.instance.profilerUpdateTickCounter % modOpis.profilerDelay != 0) return;
		if (!ticksToRun.contains(TickType.RENDER) || ticksToRun.size() != 1) return;
		
		String name = TickHandlerManager.getHandlerName(ticker);
		this.currentHandler = name;
		this.clock.start();		
	}

	@Override
	public void StopTickStart(IScheduledTickHandler ticker, EnumSet<TickType> ticksToRun) {
		this.clock.stop();	
		if (OpisClientTickHandler.instance.profilerUpdateTickCounter % modOpis.profilerDelay != 0) return;		
		if (!ticksToRun.contains(TickType.RENDER) || ticksToRun.size() != 1) return;	
		
		String name = TickHandlerManager.getHandlerName(ticker);
		if (!this.currentHandler.equals(name))
			throw new RuntimeException(String.format("Mismatched tick handler during the profiling ! %s %s", this.currentHandler, ticker.getLabel()));

		//EntityManager.addEntity(ent, this.clock.timeDelta);
		TickHandlerManager.addHandlerStart(ticker, this.clock.timeDelta);
		this.currentHandler = null;		
	}

	@Override
	public void StartTickEnd(IScheduledTickHandler ticker, EnumSet<TickType> ticksToRun) {
		if (OpisClientTickHandler.instance.profilerUpdateTickCounter % modOpis.profilerDelay != 0) return;
		if (!ticksToRun.contains(TickType.RENDER) || ticksToRun.size() != 1) return;

		String name = TickHandlerManager.getHandlerName(ticker);	
		this.currentHandler = name;
		this.clock.start();				
	}

	@Override
	public void StopTickEnd(IScheduledTickHandler ticker, EnumSet<TickType> ticksToRun) {
		this.clock.stop();	
		if (OpisClientTickHandler.instance.profilerUpdateTickCounter % modOpis.profilerDelay != 0) return;
		if (!ticksToRun.contains(TickType.RENDER) || ticksToRun.size() != 1) return;		
		
		String name = TickHandlerManager.getHandlerName(ticker);
		if (!this.currentHandler.equals(name))
			throw new RuntimeException(String.format("Mismatched tick handler during the profiling ! %s %s", this.currentHandler, ticker.getLabel()));

		TickHandlerManager.addHandlerEnd(ticker, this.clock.timeDelta);
		this.currentHandler = null;					
	}	
	
}
