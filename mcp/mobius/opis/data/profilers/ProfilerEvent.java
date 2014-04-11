package mcp.mobius.opis.data.profilers;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;

public class ProfilerEvent extends ProfilerAbstract {

	private Clock clock = new Clock();
	public  HashBasedTable<Class, String, DescriptiveStatistics> data = HashBasedTable.create(); 
	
	@Override
	public void reset() {
		data.clear();
	}
	
	@Override
	public void start() {
		clock.start();
	}

	@Override
	public void stop(Object key1, Object key2, Object key3) {
		clock.stop();
		
		try{
			String name = (String)key2 + "|" + key3.getClass().getSimpleName();
			data.get(key1.getClass(), name).addValue((double)clock.timeDelta);
		} catch (Exception e) {
			String name = (String)key2 + "|" + key3.getClass().getSimpleName();
			data.put(key1.getClass(), name, new DescriptiveStatistics(250));
			data.get(key1.getClass(), name).addValue((double)clock.timeDelta);
		}
	}

}
