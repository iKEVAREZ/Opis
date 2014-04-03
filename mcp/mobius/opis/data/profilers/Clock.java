package mcp.mobius.opis.data.profilers;

public class Clock {
	public long startTime = 0;
	public long timeDelta = 0;
	public void start(){this.startTime = System.nanoTime();}
	public void stop() {this.timeDelta = System.nanoTime() - this.startTime;} 
}
