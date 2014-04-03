package mcp.mobius.opis.data.profilers;

import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import net.minecraft.world.World;
import mcp.mobius.mobiuscore.profiler_v2.IProfilerBase;

public class ProfilerDimTick implements IProfilerBase {

	private Clock clock = new Clock();
	public  HashMap<Integer, DescriptiveStatistics> data = new HashMap<Integer, DescriptiveStatistics>();
	
	@Override
	public void start(Object key) {
		World world = (World)key;
		if (world.isRemote) return;
		
		if (!data.containsKey(world.provider.dimensionId))
			data.put(world.provider.dimensionId, new DescriptiveStatistics(20));
		clock.start();
	}
	
	@Override
	public void stop(Object key) {
		World world = (World)key;		
		if (world.isRemote) return;
		
		clock.stop();
		data.get(world.provider.dimensionId).addValue((double)clock.timeDelta);
	}

	/* UNUSED */
	@Override
	public void start() {/*UNUSED*/}
	@Override
	public void stop() {/*UNUSED*/}
	@Override
	public void start(Object key1, Object key2) {/*UNUSED*/}
	@Override
	public void stop(Object key1, Object key2) {/*UNUSED*/}
	
}
