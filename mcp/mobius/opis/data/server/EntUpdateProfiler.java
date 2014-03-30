package mcp.mobius.opis.data.server;

import mcp.mobius.mobiuscore.profiler.IProfilerEntUpdate;
import mcp.mobius.opis.data.holders.stats.StatsWorld;
import mcp.mobius.opis.data.managers.GlobalTimingManager;

public class EntUpdateProfiler extends AbstractProfiler implements	IProfilerEntUpdate {

	//private static EntUpdateProfiler _instance = new EntUpdateProfiler();
	//public  static EntUpdateProfiler instance() {return _instance;}	
	//public  StatsBase  stats = new StatsBase();	
	
	@Override
	public void EntUpdateStart(int id) {
		this.clock.start();
	}

	@Override
	public void EntUpdateEnd(int id) {
		this.clock.stop();
		GlobalTimingManager.INSTANCE.addStat(id, this.clock.timeDelta, GlobalTimingManager.INSTANCE.entUpdateStats);
	}

}
