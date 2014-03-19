package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TargetEntity implements ISerializable {

	public int entityID = 0;
	public int dim      = 0;
	
	public TargetEntity(int id, int dim){
		this.entityID = id;
		this.dim      = dim;
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeInt(this.entityID);
		stream.writeInt(this.dim);
	}

	public static  TargetEntity readFromStream(DataInputStream stream) throws IOException {
		return new TargetEntity(stream.readInt(), stream.readInt());
	}
}
