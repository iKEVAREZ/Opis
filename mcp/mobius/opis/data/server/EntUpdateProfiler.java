package mcp.mobius.opis.data.server;

import mcp.mobius.mobiuscore.profiler.IProfilerEntUpdate;
import mcp.mobius.opis.data.holders.stats.StatsBase;

public class EntUpdateProfiler extends AbstractProfiler implements	IProfilerEntUpdate {

	private static EntUpdateProfiler _instance = new EntUpdateProfiler();
	public  static EntUpdateProfiler instance() {return _instance;}	
	public  StatsBase  stats = new StatsBase();	
	
	@Override
	public void EntUpdateStart() {
		this.clock.start();
	}

	@Override
	public void EntUpdateEnd() {
		this.clock.stop();
		this.stats.addMeasure(this.clock.timeDelta);
	}

}
