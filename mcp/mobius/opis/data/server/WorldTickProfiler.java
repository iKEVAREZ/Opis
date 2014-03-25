package mcp.mobius.opis.data.server;

import mcp.mobius.mobiuscore.profiler.IProfilerWorldTick;
import mcp.mobius.opis.data.holders.stats.StatsWorld;
import mcp.mobius.opis.data.managers.GlobalTimingManager;

public class WorldTickProfiler extends AbstractProfiler implements IProfilerWorldTick {

	//private static WorldTickProfiler _instance = new WorldTickProfiler();
	//public  static WorldTickProfiler instance() {return _instance;}	
	//public  StatsBase  stats = new StatsBase();
	
	protected Clock clock_1 = new Clock();
	protected Clock clock_2 = new Clock();
	protected Clock clock_3 = new Clock();
	protected Clock clock_4 = new Clock();
	protected Clock clock_5 = new Clock();
	protected Clock clock_6 = new Clock();	
	
	@Override
	public void WorldTickStart(int id) {
		this.clock.start();
	}

	@Override
	public void WorldTickEnd(int id) {
		this.clock.stop();
		GlobalTimingManager.addStat(id, this.clock.timeDelta, GlobalTimingManager.worldTickStats);
	}

	@Override
	public void UpdatesS(int dim) {
		this.clock_1.start();
	}

	@Override
	public void UpdatesE(int dim) {
		this.clock_1.stop();
		GlobalTimingManager.addStat(dim, this.clock_1.timeDelta, GlobalTimingManager.worldUpdatesStats);		
	}

	@Override
	public void BlocksAndAmbianceS(int dim) {
		this.clock_2.start();		
	}

	@Override
	public void BlocksAndAmbianceE(int dim) {
		this.clock_2.stop();
		GlobalTimingManager.addStat(dim, this.clock_2.timeDelta, GlobalTimingManager.worldBlocksAndAmbianceStats);
	}

	@Override
	public void PlayerInstancesS(int dim) {
		this.clock_3.start();		
	}

	@Override
	public void PlayerInstancesE(int dim) {
		this.clock_3.stop();
		GlobalTimingManager.addStat(dim, this.clock_3.timeDelta, GlobalTimingManager.worldPlayerInstancesStats);
	}

	@Override
	public void VillageCollectionS(int dim) {
		this.clock_4.start();
	}

	@Override
	public void VillageCollectionE(int dim) {
		this.clock_4.stop();
		GlobalTimingManager.addStat(dim, this.clock_4.timeDelta, GlobalTimingManager.worldVillageCollectionStats);
	}

	@Override
	public void VillageSiegeS(int dim) {
		this.clock_5.start();
	}

	@Override
	public void VillageSiegeE(int dim) {
		this.clock_5.stop();
		GlobalTimingManager.addStat(dim, this.clock_5.timeDelta, GlobalTimingManager.worldVillageSiegeStats);
	}

	@Override
	public void ApplyBlockEventsS(int dim) {
		this.clock_6.start();
	}

	@Override
	public void ApplyBlockEventsE(int dim) {
		this.clock_6.stop();
		GlobalTimingManager.addStat(dim, this.clock_6.timeDelta, GlobalTimingManager.worldApplyBlockEventsStats);
	}

}
