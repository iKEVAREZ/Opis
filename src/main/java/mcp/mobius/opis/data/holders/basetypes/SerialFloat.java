package mcp.mobius.opis.data.holders.basetypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.ISerializable;

public class SerialFloat implements ISerializable {

	public float value = 0;
	
	public SerialFloat(float value){
		this.value = value;
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeFloat(this.value);
	}

	public static  SerialFloat readFromStream(DataInputStream stream) throws IOException {
		return new SerialFloat(stream.readFloat());
	}	
	
}
