package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import mcp.mobius.mobiuscore.profiler_v2.ProfilerSection;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.profilers.ProfilerTileEntityUpdate;

public class DataTileEntity implements ISerializable, Comparable {

	public short            id;
	public short            meta;
	public CoordinatesBlock pos;
	public DataTiming       update;
	
	public DataTileEntity fill(CoordinatesBlock coord){
		this.pos    = coord;
		World world = DimensionManager.getWorld(this.pos.dim);
		
		this.id     = (short) world.getBlockId(this.pos.x, this.pos.y, this.pos.z);
		this.meta   = (short) world.getBlockMetadata(this.pos.x, this.pos.y, this.pos.z);

		HashMap<CoordinatesBlock, DescriptiveStatistics> data = ((ProfilerTileEntityUpdate)(ProfilerSection.TILEENT_UPDATETIME.getProfiler())).data;
		this.update  = new DataTiming(data.containsKey(this.pos) ? data.get(this.pos).getGeometricMean() : 0.0D);		
		
		return this;
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeShort(this.id);
		stream.writeShort(this.meta);
		this.pos.writeToStream(stream);
		this.update.writeToStream(stream);
	}

	public static DataTileEntity readFromStream(DataInputStream stream) throws IOException {
		DataTileEntity retVal = new DataTileEntity();
		retVal.id = stream.readShort();
		retVal.meta = stream.readShort();
		retVal.pos  = CoordinatesBlock.readFromStream(stream);
		retVal.update = DataTiming.readFromStream(stream);
		return retVal;
	}

	@Override
	public int compareTo(Object o) {
		return this.update.compareTo(((DataTileEntity)o).update);
	}	

}
