package mcp.mobius.opis.data.server;

import mcp.mobius.mobiuscore.profiler.IProfilerTick;
import mcp.mobius.opis.data.holders.stats.StatsTick;

public class TickProfiler extends AbstractProfiler implements IProfilerTick {

	private static TickProfiler _instance = new TickProfiler();
	public  static TickProfiler instance() {return _instance;}
	
	private Clock     clock = new Clock();		
	public StatsTick  stats = new StatsTick();
	//public int        profiledTicks = 0;
	
	@Override
	public void TickStart() {
		DeadManSwitch.deadManSwitch.release();
		this.clock.start();	
	}

	@Override
	public void TickEnd() {
		this.clock.stop();
		this.stats.addMeasure(this.clock.timeDelta);
		//this.profiledTicks += 1;
	}

}
