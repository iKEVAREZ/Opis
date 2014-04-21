package mcp.mobius.opis.data.profilers;

import java.util.EnumSet;
import java.util.WeakHashMap;

import mcp.mobius.opis.data.profilers.Clock.IClock;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class ProfilerHandler extends ProfilerAbstract {

	private IClock clockServer = Clock.getNewClock();
	private IClock clockRender = Clock.getNewClock();
	public  WeakHashMap<IScheduledTickHandler, DescriptiveStatistics> dataServer = new WeakHashMap<IScheduledTickHandler, DescriptiveStatistics>();	
	public  WeakHashMap<IScheduledTickHandler, DescriptiveStatistics> dataRender = new WeakHashMap<IScheduledTickHandler, DescriptiveStatistics>();
	
	@Override
	public void reset() {
		
	}

	@Override
	public void start(Object key1, Object key2) {
		IScheduledTickHandler ticker = (IScheduledTickHandler) key1;
		EnumSet<TickType> ticksToRun = (EnumSet<TickType>) key2;		
		
		if (ticksToRun.contains(TickType.SERVER) && ticksToRun.size() == 1){
			if (!dataServer.containsKey(ticker))
				dataServer.put(ticker, new DescriptiveStatistics());
		
			clockServer.start();
		}
		
		else if (ticksToRun.contains(TickType.RENDER) && ticksToRun.size() == 1){
			if (!dataRender.containsKey(ticker))
				dataRender.put(ticker, new DescriptiveStatistics());
		
			clockRender.start();
		}		
	}

	@Override
	public void stop(Object key1, Object key2) {
		IScheduledTickHandler ticker = (IScheduledTickHandler) key1;
		EnumSet<TickType> ticksToRun = (EnumSet<TickType>) key2;
		if (ticksToRun.contains(TickType.SERVER) && ticksToRun.size() == 1){
			clockServer.stop();
			dataServer.get(ticker).addValue((double)clockServer.getDelta());
		}
		
		else if (ticksToRun.contains(TickType.RENDER) && ticksToRun.size() == 1){
			clockRender.stop();
			dataRender.get(ticker).addValue((double)clockRender.getDelta());
		}		
	}	
}
