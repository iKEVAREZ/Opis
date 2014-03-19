package mcp.mobius.opis.data.holders.basetypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.ISerializable;

public class SerialInt implements ISerializable {

	public int value = 0;
	
	public SerialInt(int value){
		this.value = value;
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeInt(this.value);
	}

	public static  SerialInt readFromStream(DataInputStream stream) throws IOException {
		return new SerialInt(stream.readInt());
	}	
	
}
