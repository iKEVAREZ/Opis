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
	public int hashCode;
	public boolean isValid;
	
	public DataTileEntity fill(TileEntity tileEntity){
		this.pos        = new CoordinatesBlock(tileEntity.worldObj.provider.dimensionId, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
		this.clazz      = new CachedString(tileEntity.getClass().getCanonicalName());
		this.hashCode   = System.identityHashCode(tileEntity);
		this.isValid  = !tileEntity.isInvalid();
		return this;
	}
	
	public DataTileEntity fill(CoordinatesBlock coord, String clazz, int hashCode, boolean isInvalid){
		this.pos       = coord;
		this.clazz     = new CachedString(clazz);
		this.hashCode  = hashCode;
		this.isValid = !isInvalid;
		return this;
	}	
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		this.clazz.writeToStream(stream);
		this.pos.writeToStream(stream);
		stream.writeInt(this.hashCode);
		stream.writeBoolean(this.isValid);
	}

	public static DataTileEntity readFromStream(DataInputStream stream) throws IOException {
		DataTileEntity retVal = new DataTileEntity();
		retVal.clazz = CachedString.readFromStream(stream);
		retVal.pos   = CoordinatesBlock.readFromStream(stream);
		retVal.hashCode = stream.readInt();
		retVal.isValid = stream.readBoolean();
		return retVal;
	}
}
