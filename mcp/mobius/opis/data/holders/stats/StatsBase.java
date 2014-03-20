package mcp.mobius.opis.data.holders.stats;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.ISerializable;

public class StatsBase extends StatAbstract implements ISerializable {

	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeDouble(this.getGeometricMean());		
	}
	
	public static  StatsTick readFromStream(DataInputStream stream) throws IOException {
		StatsTick stat = new StatsTick();
		stat.setGeometricMean(stream.readDouble());
		return stat;
	}	

}
