package mcp.mobius.opis.data.profilers;

import mcp.mobius.opis.data.profilers.Clock.IClock;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;

public class ProfilerEvent extends ProfilerAbstract {

	private IClock clock = Clock.getNewClock();
	public  HashBasedTable<Class, String, DescriptiveStatistics> data    = HashBasedTable.create(); 
	public  HashBasedTable<Class, String, String>                dataMod = HashBasedTable.create();
	
	@Override
	public void reset() {
		data.clear();
	}
	
	@Override
	public void start() {
		clock.start();
	}

	@Override
	public void stop(Object event, Object pkg, Object handler, Object mod) {
		clock.stop();
		
		try{
			String name = (String)pkg + "|" + handler.getClass().getSimpleName();
			data.get(event.getClass(), name).addValue((double)clock.getDelta());
		} catch (Exception e) {
			String name = (String)pkg + "|" + handler.getClass().getSimpleName();
			data.put(event.getClass(), name, new DescriptiveStatistics(250));
			dataMod.put(event.getClass(), name, (String)mod);
			data.get(event.getClass(), name).addValue((double)clock.getDelta());
		}
	}

}
