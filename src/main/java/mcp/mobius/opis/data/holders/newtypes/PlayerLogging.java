package mcp.mobius.opis.data.holders.newtypes;

import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.SerialString;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class PlayerLogging implements ISerializable {

	CachedString name;
	boolean status;
	
	public PlayerLogging(String name, boolean status){
		this.name    = new CachedString(name);
		this.status  = status;
	}
	
	public PlayerLogging(CachedString name, boolean status){
		this.name    = name;
		this.status  = status;
	}
	
	
	@Override
	public void writeToStream(ByteArrayDataOutput stream){
		this.name.writeToStream(stream);
		stream.writeBoolean(this.status);
			}
 
	public static  PlayerLogging readFromStream(ByteArrayDataInput stream){
		return new PlayerLogging(
				CachedString.readFromStream(stream),
				stream.readBoolean()
				);
	}	
}
