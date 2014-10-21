package mcp.mobius.opis.data.holders.basetypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.newtypes.CachedString;
import mcp.mobius.opis.network.PacketManager;

public class AmountHolder implements ISerializable {

	public CachedString key    = null;
	public Integer value = 0;
	
	public AmountHolder(String key, Integer value){
		this.key   = new CachedString(key);
		this.value = value;
	}

	public AmountHolder(CachedString key, Integer value){
		this.key   = key;
		this.value = value;
	}	
	
	@Override
	public void writeToStream(ByteArrayDataOutput stream){
		this.key.writeToStream(stream);
		stream.writeInt(this.value);
	}

	public static AmountHolder readFromStream(ByteArrayDataInput istream){
		return new AmountHolder(CachedString.readFromStream(istream), istream.readInt());
	}
}
