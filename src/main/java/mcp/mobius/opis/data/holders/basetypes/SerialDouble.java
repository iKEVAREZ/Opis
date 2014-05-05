package mcp.mobius.opis.data.holders.basetypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.ISerializable;

public class SerialDouble implements ISerializable {

	public double value = 0;
	
	public SerialDouble(double value){
		this.value = value;
	}
	
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeDouble(this.value);
	}

	public static  SerialDouble readFromStream(DataInputStream stream) throws IOException {
		return new SerialDouble(stream.readDouble());
	}	
	
}
