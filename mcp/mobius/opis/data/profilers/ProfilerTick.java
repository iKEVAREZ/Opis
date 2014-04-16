package mcp.mobius.opis.data.profilers;

import java.util.ArrayList;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class ProfilerTick extends ProfilerAbstract {

	private Clock clock = new Clock();
	public  DescriptiveStatistics data = new DescriptiveStatistics(20);
	
	@Override
	public void reset() {
		//this.data.clear();
	}	
	
	@Override
	public void start() {
		//DeadManSwitch.instance.setTimer(System.nanoTime());
		this.clock.start();			
	}
	
	@Override
	public void stop() {
		this.clock.stop();
		this.data.addValue((double)clock.timeDelta);
	}
	
}
