package mcp.mobius.opis.data.holders.stats;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.ISerializable;

public class StatsWorld extends StatAbstract implements ISerializable {

	private int dim = 0;
	
	public StatsWorld(int dim){
		this.dim = dim;
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeInt(this.dim);
		stream.writeDouble(this.getGeometricMean());		
	}
	
	public static  StatsWorld readFromStream(DataInputStream stream) throws IOException {
		int dim = stream.readInt();
		StatsWorld stat = new StatsWorld(dim);
		stat.setGeometricMean(stream.readDouble());
		return stat;
	}	

}
