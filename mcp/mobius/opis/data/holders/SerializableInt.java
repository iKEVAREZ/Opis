package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SerializableInt implements ISerializable {

	public int value = 0;
	
	public SerializableInt(int value){
		this.value = value;
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeInt(this.value);
	}

	public static  SerializableInt readFromStream(DataInputStream stream) throws IOException {
		return new SerializableInt(stream.readInt());
	}	
	
}
