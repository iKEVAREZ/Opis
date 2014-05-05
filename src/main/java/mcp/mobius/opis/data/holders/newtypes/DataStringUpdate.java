package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;
import mcp.mobius.opis.data.holders.ISerializable;

public class DataStringUpdate implements ISerializable {

	public String str;
	public int    index;

	public DataStringUpdate(){}	
	
	public DataStringUpdate(String str, int index){
		this.str   = str;
		this.index = index;
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		Packet.writeString(this.str, stream);
		stream.writeInt(this.index);

	}

	public static  DataStringUpdate readFromStream(DataInputStream stream) throws IOException {
		return new DataStringUpdate(Packet.readString(stream, 255), stream.readInt());
	}	
	
}
