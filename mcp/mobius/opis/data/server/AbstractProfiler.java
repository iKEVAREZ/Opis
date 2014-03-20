package mcp.mobius.opis.data.server;

public class AbstractProfiler {

	public class Clock{
		public long startTime = 0;
		public long timeDelta = 0;
		public void start(){this.startTime = System.nanoTime();}
		public void stop(){this.timeDelta = System.nanoTime() - this.startTime;} 
	}	
	protected Clock          clock = new Clock();	
	
}
