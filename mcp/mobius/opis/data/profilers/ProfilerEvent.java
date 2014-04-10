package mcp.mobius.opis.data.profilers;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;

public class ProfilerEvent extends ProfilerAbstract {

	private Clock clock = new Clock();
	public  HashBasedTable<Class, Class, DescriptiveStatistics> data = HashBasedTable.create(); 
	
	@Override
	public void reset() {
		data.clear();
	}
	
	@Override
	public void start() {
		clock.start();
	}

	@Override
	public void stop(Object key1, Object key2) {
		clock.stop();
		
		try{
			data.get(key1.getClass(), key2.getClass()).addValue((double)clock.timeDelta);
		} catch (Exception e) {
			data.put(key1.getClass(), key2.getClass(), new DescriptiveStatistics(250));
			data.get(key1.getClass(), key2.getClass()).addValue((double)clock.timeDelta);
		}
	}

}
