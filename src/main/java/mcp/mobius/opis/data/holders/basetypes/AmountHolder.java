package mcp.mobius.opis.data.holders.basetypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.ISerializable;
import net.minecraft.network.packet.Packet;

public class AmountHolder implements ISerializable {

	public String key    = null;
	public Integer value = 0;
	
	public AmountHolder(String key, Integer value){
		this.key   = key;
		this.value = value;
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		Packet.writeString(this.key, stream);
		stream.writeInt(this.value);
	}

	public static AmountHolder readFromStream(DataInputStream istream) throws IOException {
		String  key   = Packet.readString(istream, 255);
		Integer value = istream.readInt();
		
		return new AmountHolder(key, value);
	}
}
