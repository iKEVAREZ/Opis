package mcp.mobius.opis.data.holders.basetypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.ISerializable;

public class SerialLong implements ISerializable {

	public long value = 0;
	
	public SerialLong(long value){
		this.value = value;
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeLong(this.value);
	}

	public static  SerialLong readFromStream(DataInputStream stream) throws IOException {
		return new SerialLong(stream.readLong());
	}	
	
}
