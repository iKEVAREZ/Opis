package mcp.mobius.opis.data.profilers;

import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import mcp.mobius.mobiuscore.profiler_v2.IProfilerBase;

public class ProfilerDimBlockTick implements IProfilerBase {

	private Clock clock = new Clock();
	public  HashMap<Integer, DescriptiveStatistics> data = new HashMap<Integer, DescriptiveStatistics>();
	
	
	@Override
	public void start(Object key) {
		Integer dim = (Integer)key;
		
		if (!data.containsKey(dim))
			data.put(dim, new DescriptiveStatistics(20));
		clock.start();
	}
	
	@Override
	public void stop(Object key) {
		Integer dim = (Integer)key;		
		clock.stop();
		data.get(dim).addValue((double)clock.timeDelta);
	}

	
	
	/* UNUSED */
	@Override
	public void start() {}
	@Override
	public void stop() {}
	@Override
	public void start(Object key1, Object key2) {}
	@Override
	public void stop(Object key1, Object key2) {}

}
