package mcp.mobius.opis.data.server;

import mcp.mobius.mobiuscore.profiler.IProfilerWorldTick;
import mcp.mobius.opis.data.holders.stats.StatsBase;

public class WorldTickProfiler extends AbstractProfiler implements IProfilerWorldTick {

	private static WorldTickProfiler _instance = new WorldTickProfiler();
	public  static WorldTickProfiler instance() {return _instance;}	
	public  StatsBase  stats = new StatsBase();
	
	@Override
	public void WorldTickStart() {
		this.clock.start();

	}

	@Override
	public void WorldTickEnd() {
		this.clock.stop();
		this.stats.addMeasure(this.clock.timeDelta);
	}

}
