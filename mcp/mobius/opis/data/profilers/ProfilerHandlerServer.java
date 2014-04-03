package mcp.mobius.opis.data.profilers;

import java.util.EnumSet;
import java.util.WeakHashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class ProfilerHandlerServer extends ProfilerAbstract {

	private Clock clock = new Clock();
	public  WeakHashMap<IScheduledTickHandler, DescriptiveStatistics> data = new WeakHashMap<IScheduledTickHandler, DescriptiveStatistics>();	
	
	@Override
	public void reset() {
		
	}

	@Override
	public void start(Object key1, Object key2) {
		IScheduledTickHandler ticker = (IScheduledTickHandler) key1;
		EnumSet<TickType> ticksToRun = (EnumSet<TickType>) key2;		
		if (!ticksToRun.contains(TickType.SERVER) || ticksToRun.size() != 1) return;
		
		if (!data.containsKey(ticker))
			data.put(ticker, new DescriptiveStatistics());
		
		clock.start();
	}

	@Override
	public void stop(Object key1, Object key2) {
		IScheduledTickHandler ticker = (IScheduledTickHandler) key1;
		EnumSet<TickType> ticksToRun = (EnumSet<TickType>) key2;
		if (!ticksToRun.contains(TickType.SERVER) || ticksToRun.size() != 1) return;
		
		clock.stop();
		data.get(ticker).addValue((double)clock.timeDelta);
	}	
}
