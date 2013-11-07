package mcp.mobius.opis.data;

import net.minecraft.tileentity.TileEntity;
import mcp.mobius.mobiuscore.profiler.IProfilerTileEntity;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.server.OpisServerTickHandler;

public class TileEntityProfiler implements IProfilerTileEntity {

	public class Clock{
		public long startTime = 0;
		public long timeDelta = 0;
		public void start(){this.startTime = System.nanoTime();}
		public void stop(){this.timeDelta = System.nanoTime() - this.startTime;} 
	}	
	
	private TileEntity currentEntity = null;
	private Clock      clock = new Clock();
	
	@Override
	public void GlobalStart() {	}

	@Override
	public void GlobalStop() { }

	@Override
	public void Start(TileEntity te) {
		if ((!modOpis.profilerRun) || (OpisServerTickHandler.profilerUpdateTickCounter % modOpis.profilerDelay != 0)) return;
		
		if (te.worldObj.isRemote) return;
		
		this.currentEntity = te;
		this.clock.start();
	}

	@Override
	public void Stop(TileEntity te) {
		if ((!modOpis.profilerRun) || (OpisServerTickHandler.profilerUpdateTickCounter % modOpis.profilerDelay != 0)) return;		
		
		this.clock.stop();
		if (te.worldObj.isRemote) return;
		
		if (te != this.currentEntity)
			throw new RuntimeException(String.format("Mismatched entities during the profiling ! %s %s", te, this.currentEntity));
		

		TileEntityManager.addTileEntity(te, this.clock.timeDelta);
		this.currentEntity = null;
	}

}
