package mcp.mobius.opis.data.server;

import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import net.minecraft.network.packet.Packet;
import mcp.mobius.mobiuscore.profiler.IProfilerNetwork;

public enum NetworkProfiler implements IProfilerNetwork {
	INSTANCE;
	
	public class Clock{
		public long startTime = 0;
		public long timeDelta = 0;
		public void start(){this.startTime = System.nanoTime();}
		public void stop(){this.timeDelta = System.nanoTime() - this.startTime;} 
	}	
	
	public long dataSizeOut = 0;
	public long dataSizeIn  = 0;
	public HashMap<String, Clock> clocks                = new HashMap<String, Clock>();
	public HashMap<String, DescriptiveStatistics> stats = new HashMap<String, DescriptiveStatistics>();
	
	@Override
	public void addPacketOut(Packet packet) {
		if (packet != null)
			dataSizeOut += packet.getPacketSize() + 1;
	}

	@Override
	public void addPacketIn(Packet packet) {
		if (packet != null)
			dataSizeIn += packet.getPacketSize() + 1;
	}

	@Override
	public void startNetwork(String subprofile) {
		if (!clocks.containsKey(subprofile))
			clocks.put(subprofile, new Clock());
		
		clocks.get(subprofile).start();
	}

	@Override
	public void stopNetwork(String subprofile) {
		clocks.get(subprofile).stop();

		if (!stats.containsKey(subprofile))
			stats.put(subprofile, new DescriptiveStatistics(250));
		
		stats.get(subprofile).addValue((double)clocks.get(subprofile).timeDelta/1000.0);
	}

}
