package mcp.mobius.opis.data;

import net.minecraft.tileentity.TileEntity;
import mcp.mobius.mobiuscore.profiler.IProfilerTileEntity;

public class ProfilerTileEntities implements IProfilerTileEntity {

	@Override
	public void GlobalStart() {	}

	@Override
	public void GlobalStop() { }

	@Override
	public void Start(TileEntity te) { }

	@Override
	public void Stop(TileEntity te) { }


}
