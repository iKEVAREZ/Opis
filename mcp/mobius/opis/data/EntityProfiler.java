package mcp.mobius.opis.data;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.Entity;
import mcp.mobius.mobiuscore.profiler.IProfilerEntity;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.server.OpisServerTickHandler;

public class EntityProfiler implements IProfilerEntity {

	public class Clock{
		public long startTime = 0;
		public long timeDelta = 0;
		public void start(){this.startTime = System.nanoTime();}
		public void stop(){this.timeDelta = System.nanoTime() - this.startTime;} 
	}	
	
	private Entity currentEntity = null;
	private Clock          clock = new Clock();	
	
	@Override
	public void FullEntityStart() {}

	@Override
	public void FullEntityStop() {}

	@Override
	public void Start(Entity ent) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) return;		
		if ((!modOpis.profilerRun) || (OpisServerTickHandler.instance.profilerUpdateTickCounter % modOpis.profilerDelay != 0)) return;
		
		if (ent.worldObj.isRemote) return;
		
		this.currentEntity = ent;
		this.clock.start();

	}

	@Override
	public void Stop(Entity ent) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) return;
		if ((!modOpis.profilerRun) || (OpisServerTickHandler.instance.profilerUpdateTickCounter % modOpis.profilerDelay != 0)) return;		
		
		this.clock.stop();
		if (ent.worldObj.isRemote) return;
		
		if (ent != this.currentEntity)
			throw new RuntimeException(String.format("Mismatched entities during the profiling ! %s %s", ent, this.currentEntity));
		

		EntityManager.addEntity(ent, this.clock.timeDelta);
		this.currentEntity = null;

	}

}
