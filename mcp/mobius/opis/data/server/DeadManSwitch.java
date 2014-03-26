package mcp.mobius.opis.data.server;

import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.server.MinecraftServer;

public class DeadManSwitch implements Runnable{

	private Thread serverThread    = null;
	private MinecraftServer server = null;
	private int nTickWithoutAccident   = 0;
	public  static Semaphore deadManSwitch = new Semaphore(1);
	
	public DeadManSwitch(MinecraftServer server, Thread serverThread){
		this.server    = server;
		this.serverThread = serverThread; 
		
	}
	
	@Override
	public void run() {
		
		System.out.printf("Starting Dead Man Switch\n");
		
		while (server.isServerRunning()){
			try{
				int npermits = deadManSwitch.drainPermits();
				
				if (npermits == 0){
					System.out.printf("==== Main thread is staled ! %d ticks last seen ====\n", this.nTickWithoutAccident);
					this.nTickWithoutAccident = 0;
				} else {
					nTickWithoutAccident += 1;
				}
				
				long time = System.currentTimeMillis();
				
				Thread.sleep(500L);
				
			} catch (InterruptedException e){
				throw new RuntimeException(e);
			}
		}
		
		
	}
	
	public static DeadManSwitch startDeadManSwitch(MinecraftServer server){
		DeadManSwitch deadManSwitch = new DeadManSwitch(server, Thread.currentThread());
		(new Thread(deadManSwitch)).start();
		return deadManSwitch;
	}
	
}
