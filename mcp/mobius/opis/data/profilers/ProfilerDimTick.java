package mcp.mobius.opis.data.profilers;

import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import net.minecraft.world.World;
import mcp.mobius.mobiuscore.profiler_v2.IProfilerBase;

public class ProfilerDimTick implements IProfilerBase {

	private Clock clock = new Clock();
	public  HashMap<Integer, DescriptiveStatistics> data = new HashMap<Integer, DescriptiveStatistics>();
	
	@Override
	public void start(World key) {
		if (key.isRemote) return;
		
		if (!data.containsKey(key.provider.dimensionId))
			data.put(key.provider.dimensionId, new DescriptiveStatistics(20));
		clock.start();
	}
	
	@Override
	public void stop(World key) {
		if (key.isRemote) return;
		
		clock.stop();
		data.get(key.provider.dimensionId).addValue((double)clock.timeDelta);
	}

	/* UNUSED */
	@Override
	public void start() {/*UNUSED*/}
	@Override
	public void stop() {/*UNUSED*/}
	@Override
	public void start(Integer key) {/*UNUSED*/}
	@Override
	public void stop(Integer key) {/*UNUSED*/}	
}
