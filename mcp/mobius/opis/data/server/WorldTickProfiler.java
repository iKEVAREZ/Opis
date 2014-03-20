package mcp.mobius.opis.data.server;

import mcp.mobius.mobiuscore.profiler.IProfilerWorldTick;
import mcp.mobius.opis.data.holders.stats.StatsWorld;
import mcp.mobius.opis.data.managers.GlobalTimingManager;

public class WorldTickProfiler extends AbstractProfiler implements IProfilerWorldTick {

	//private static WorldTickProfiler _instance = new WorldTickProfiler();
	//public  static WorldTickProfiler instance() {return _instance;}	
	//public  StatsBase  stats = new StatsBase();
	
	@Override
	public void WorldTickStart(int id) {
		this.clock.start();

	}

	@Override
	public void WorldTickEnd(int id) {
		this.clock.stop();
		GlobalTimingManager.addWorldTickStat(id, this.clock.timeDelta);
	}

}
