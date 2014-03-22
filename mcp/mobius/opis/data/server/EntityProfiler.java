package mcp.mobius.opis.data.server;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.Entity;
import mcp.mobius.mobiuscore.profiler.IProfilerEntity;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.events.OpisServerTickHandler;

public class EntityProfiler extends AbstractProfiler implements IProfilerEntity {

	private Entity currentEntity = null;
	
	@Override
	public void FullEntityStart() {}

	@Override
	public void FullEntityStop() {}

	@Override
	public void Start(Entity ent) {
		//if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) return;		
		if ((!modOpis.profilerRun) || (OpisServerTickHandler.instance.profilerUpdateTickCounter % modOpis.profilerDelay != 0)) return;
		
		if (ent.worldObj.isRemote) return;
		
		this.currentEntity = ent;
		this.clock.start();

	}

	@Override
	public void Stop(Entity ent) {
		//if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) return;
		if ((!modOpis.profilerRun) || (OpisServerTickHandler.instance.profilerUpdateTickCounter % modOpis.profilerDelay != 0)) return;		
		
		this.clock.stop();
		if (ent.worldObj.isRemote) return;
		
		if (ent != this.currentEntity)
			throw new RuntimeException(String.format("Mismatched entities during the profiling ! %s %s", ent, this.currentEntity));
		

		EntityManager.addEntity(ent, this.clock.timeDelta);
		this.currentEntity = null;

	}

}
