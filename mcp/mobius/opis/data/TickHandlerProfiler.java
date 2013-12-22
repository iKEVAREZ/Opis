package mcp.mobius.opis.data;

import java.util.EnumSet;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;
import mcp.mobius.mobiuscore.profiler.IProfilerTick;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.server.OpisServerTickHandler;

public class TickHandlerProfiler implements IProfilerTick {

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
		//if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) return;		
		if ((!modOpis.profilerRun) || (OpisServerTickHandler.instance.profilerUpdateTickCounter % modOpis.profilerDelay != 0)) return;
		if (!ticksToRun.contains(TickType.SERVER)) return;
		this.currentHandler = ticker.getLabel();
		this.clock.start();		
	}

	@Override
	public void StopTickStart(IScheduledTickHandler ticker, EnumSet<TickType> ticksToRun) {
		if ((!modOpis.profilerRun) || (OpisServerTickHandler.instance.profilerUpdateTickCounter % modOpis.profilerDelay != 0)) return;		
		if (!ticksToRun.contains(TickType.SERVER)) return;		
		this.clock.stop();
		
		if (!this.currentHandler.equals(ticker.getLabel()))
			throw new RuntimeException(String.format("Mismatched tick handler during the profiling ! %s %s", this.currentHandler, ticker.getLabel()));

		//EntityManager.addEntity(ent, this.clock.timeDelta);
		TickHandlerManager.addHandlerStart(ticker, this.clock.timeDelta);
		this.currentHandler = null;		
	}

	@Override
	public void StartTickEnd(IScheduledTickHandler ticker, EnumSet<TickType> ticksToRun) {
		if ((!modOpis.profilerRun) || (OpisServerTickHandler.instance.profilerUpdateTickCounter % modOpis.profilerDelay != 0)) return;
		if (!ticksToRun.contains(TickType.SERVER)) return;
		this.currentHandler = ticker.getLabel();
		this.clock.start();				
	}

	@Override
	public void StopTickEnd(IScheduledTickHandler ticker, EnumSet<TickType> ticksToRun) {
		if ((!modOpis.profilerRun) || (OpisServerTickHandler.instance.profilerUpdateTickCounter % modOpis.profilerDelay != 0)) return;		
		if (!ticksToRun.contains(TickType.SERVER)) return;		
		this.clock.stop();
		
		if (!this.currentHandler.equals(ticker.getLabel()))
			throw new RuntimeException(String.format("Mismatched tick handler during the profiling ! %s %s", this.currentHandler, ticker.getLabel()));

		TickHandlerManager.addHandlerEnd(ticker, this.clock.timeDelta);
		this.currentHandler = null;					
	}

}
