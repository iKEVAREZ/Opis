package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WorldTickStats extends StatAbstract implements ISerializable {

	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeDouble(this.getGeometricMean());		
	}
	
	public static  TickStats readFromStream(DataInputStream stream) throws IOException {
		TickStats stat = new TickStats();
		stat.setGeometricMean(stream.readDouble());
		return stat;
	}	

}
