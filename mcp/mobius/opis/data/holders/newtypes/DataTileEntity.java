package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;

public class DataTileEntity implements ISerializable {

	public CachedString     clazz;
	public CoordinatesBlock pos;
	
	public DataTileEntity fill(TileEntity tileEntity){
		this.pos    = new CoordinatesBlock(tileEntity.worldObj.provider.dimensionId, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
		this.clazz  = new CachedString(tileEntity.getClass().getCanonicalName());
		return this;
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		this.clazz.writeToStream(stream);
		this.pos.writeToStream(stream);
	}

	public static DataTileEntity readFromStream(DataInputStream stream) throws IOException {
		DataTileEntity retVal = new DataTileEntity();
		retVal.clazz = CachedString.readFromStream(stream);
		retVal.pos   = CoordinatesBlock.readFromStream(stream);
		return retVal;
	}
}
