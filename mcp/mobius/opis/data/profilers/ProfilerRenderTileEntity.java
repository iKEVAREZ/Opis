package mcp.mobius.opis.data.profilers;

import java.util.WeakHashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import net.minecraft.tileentity.TileEntity;

public class ProfilerRenderTileEntity extends ProfilerAbstract {

	public WeakHashMap<TileEntity, DescriptiveStatistics> data = new WeakHashMap<TileEntity, DescriptiveStatistics>();
	private Clock clock = new Clock();
	
	@Override
	public void reset() {
		data.clear();
	}

	@Override
	public void start(Object key){
		TileEntity tileEnt = (TileEntity)key;
		if (!data.containsKey(tileEnt))
			data.put(tileEnt, new DescriptiveStatistics());
		
		clock.start();
	}
	
	@Override
	public void stop(Object key){
		clock.stop();
		data.get((TileEntity)key).addValue((double)clock.timeDelta);
	}	
}
