package mcp.mobius.opis.data.holders.newtypes;

import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.SerialString;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class PlayerStatus implements ISerializable {

	CachedString name;
	boolean    status;
	int        x, y, z, dim;
	
	public PlayerStatus(String name, boolean status, int dim, int x, int y, int z){
		this.name    = new CachedString(name);
		this.status  = status;
		this.dim     = dim;
		this.x       = x;
		this.y       = y;
		this.z       = z;
	}
	
	public PlayerStatus(CachedString name, boolean status, int dim, int x, int y, int z){
		this.name    = name;
		this.status  = status;
		this.dim     = dim;
		this.x       = x;
		this.y       = y;
		this.z       = z;		
	}
	
	
	@Override
	public void writeToStream(ByteArrayDataOutput stream){
		this.name.writeToStream(stream);
		stream.writeBoolean(this.status);
		stream.writeInt(dim);
		stream.writeInt(x);
		stream.writeInt(y);
		stream.writeInt(z);
			}
 
	public static  PlayerStatus readFromStream(ByteArrayDataInput stream){
		return new PlayerStatus(
				CachedString.readFromStream(stream),
				stream.readBoolean(),
				stream.readInt(),
				stream.readInt(),
				stream.readInt(),
				stream.readInt()
				);
	}	
}
