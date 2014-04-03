package mcp.mobius.opis.events;

import java.util.EnumSet;
import java.util.logging.Level;

import mcp.mobius.mobiuscore.profiler.DummyProfiler;
import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;
import mcp.mobius.mobiuscore.profiler_v2.ProfilerSection;
import mcp.mobius.opis.modOpis;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;

public class OpisClientTickHandler implements ITickHandler {

	public long profilerUpdateTickCounter = 0;	
	public long profilerRunningTicks;

	public static OpisClientTickHandler instance;
	
	public OpisClientTickHandler(){
		instance = this;
	}	
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(type.contains(TickType.CLIENT)){
			profilerUpdateTickCounter++;
			
			if (profilerRunningTicks < modOpis.profilerMaxTicks && modOpis.profilerRun){
				profilerRunningTicks++;
			}
			else if (profilerRunningTicks >= modOpis.profilerMaxTicks && modOpis.profilerRun){
				profilerRunningTicks = 0;
				modOpis.profilerRun = false;
				ProfilerRegistrar.turnOff();
				ProfilerSection.desactivateAll();
				
				//TODO : Print something to the client chat
				System.out.printf("Profiling done\n");				
				//modOpis.log.log(Level.INFO, "Profiling done");
			}			
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "opis.client.tickhandler";
	}

}
