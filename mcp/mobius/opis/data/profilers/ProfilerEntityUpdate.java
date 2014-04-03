package mcp.mobius.opis.data.profilers;

import java.util.HashMap;

import net.minecraft.entity.Entity;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import mcp.mobius.mobiuscore.profiler_v2.IProfilerBase;

public class ProfilerEntityUpdate extends ProfilerAbstract implements IProfilerBase {

	private Clock clock = new Clock();
	public  HashMap<Integer, DescriptiveStatistics> data = new HashMap<Integer, DescriptiveStatistics>();
	
	@Override
	public void reset() {
		this.data.clear();
	}	
	
	@Override
	public void start(Object key) {
		Entity entity = (Entity)key;
		if (entity.worldObj.isRemote) return;
		
		if (!data.containsKey(entity.entityId))
			data.put(entity.entityId, new DescriptiveStatistics());
		clock.start();
	}
	
	@Override
	public void stop(Object key) {
		Entity entity = (Entity)key;
		if (entity.worldObj.isRemote) return;
		
		clock.stop();
		data.get(entity.entityId).addValue((double)clock.timeDelta);
	}
}
