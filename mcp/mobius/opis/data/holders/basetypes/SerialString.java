package mcp.mobius.opis.data.holders.basetypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;
import mcp.mobius.opis.data.holders.ISerializable;

public class SerialString implements ISerializable {

	public String value = "";
	
	public SerialString(String value){
		this.value = value;
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		Packet.writeString(this.value, stream);
	}

	public static  SerialString readFromStream(DataInputStream stream) throws IOException {
		return new SerialString(Packet.readString(stream, 255));
	}	
}
