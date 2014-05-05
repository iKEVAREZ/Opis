package mcp.mobius.opis.data.profilers;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import javax.management.ObjectName;

public class Clock {

	public interface IClock{
		void start();
		void stop();
		long getDelta();
	}
	
	public static ThreadMXBean threadMX = ManagementFactory.getThreadMXBean();
	//public static boolean canThreadCPU = threadMX.isCurrentThreadCpuTimeSupported();
	public static boolean  canThreadCPU = false;

	public class ClockMX implements IClock{
		public long startTime = 0;
		public long timeDelta = 0;
		public void start(){this.startTime = threadMX.getCurrentThreadCpuTime();}
		public void stop() {this.timeDelta = threadMX.getCurrentThreadCpuTime() - this.startTime;}
		public long getDelta() {return this.timeDelta;}
	}
	
	public class ClockNano implements IClock{
		public long startTime = 0;
		public long timeDelta = 0;
		public void start(){this.startTime = System.nanoTime();}
		public void stop() {this.timeDelta = System.nanoTime() - this.startTime;}
		public long getDelta() {return this.timeDelta;}		
	}
	
	public static IClock getNewClock(){
		if (Clock.canThreadCPU)
			return new Clock().new ClockMX();
		else
			return new Clock().new ClockNano();
	}
}
