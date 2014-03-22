package mcp.mobius.opis.data.server;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeHooks;
import mcp.mobius.mobiuscore.profiler.IProfilerTileEntity;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.events.OpisServerTickHandler;

public class TileEntityProfiler extends AbstractProfiler implements IProfilerTileEntity {

	private TileEntity currentEntity = null;
	
	@Override
	public void FullTileEntityStart() {	}

	@Override
	public void FullTileEntityStop() { }

	@Override
	public void Start(TileEntity te) {
		//if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) return;		
		if ((!modOpis.profilerRun) || (OpisServerTickHandler.instance.profilerUpdateTickCounter % modOpis.profilerDelay != 0)) return;
		
		if (te.worldObj.isRemote) return;
		
		this.currentEntity = te;
		this.clock.start();
	}

	@Override
	public void Stop(TileEntity te) {
		//if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) return;
		if ((!modOpis.profilerRun) || (OpisServerTickHandler.instance.profilerUpdateTickCounter % modOpis.profilerDelay != 0)) return;		
		
		this.clock.stop();
		if (te.worldObj.isRemote) return;
		
		if (te != this.currentEntity)
			throw new RuntimeException(String.format("Mismatched entities during the profiling ! %s %s", te, this.currentEntity));
		

		TileEntityManager.addTileEntity(te, this.clock.timeDelta);
		this.currentEntity = null;
	}

}
