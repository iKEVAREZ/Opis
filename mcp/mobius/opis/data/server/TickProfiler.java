package mcp.mobius.opis.data.server;

import mcp.mobius.mobiuscore.profiler.IProfilerTick;
import mcp.mobius.opis.data.holders.TickStats;

public class TickProfiler implements IProfilerTick {

	public class Clock{
		public long startTime = 0;
		public long timeDelta = 0;
		public void start(){this.startTime = System.nanoTime();}
		public void stop(){this.timeDelta = System.nanoTime() - this.startTime;} 
	}	
	
	private static TickProfiler _instance = new TickProfiler();
	public  static TickProfiler instance() {return _instance;}
	
	private Clock     clock = new Clock();		
	public TickStats  stats = new TickStats();
	public int        profiledTicks = 0;
	
	@Override
	public void TickStart() {
		this.clock.start();		
	}

	@Override
	public void TickEnd() {
		this.clock.stop();
		this.stats.addMeasure(this.clock.timeDelta);
		this.profiledTicks += 1;
	}

}
