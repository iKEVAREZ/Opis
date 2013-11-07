package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChunkStats implements ISerializable, Comparable{
	public int    nentities  = 0;
	public double updateTime = 0;
	public CoordinatesChunk coord;
	
	public ChunkStats(){}
	
	public ChunkStats(CoordinatesChunk coord, int nentities, double updateTime){
		this.nentities  = nentities;
		this.updateTime = updateTime;
		this.coord = coord;
	}
	
	@Override
	public int compareTo(Object arg0) {
		double value = ((ChunkStats)arg0).updateTime - this.updateTime;
		if (value > 0)
			return 1;
		if (value < 0)
			return -1;
		return 0;
	}

	@Override
	public   void writeToStream(DataOutputStream stream) throws IOException{
		this.coord.writeToStream(stream);
		stream.writeInt(this.nentities);
		stream.writeDouble(this.updateTime);
	}

	public static  ChunkStats readFromStream(DataInputStream stream) throws IOException {
		CoordinatesChunk coord   = CoordinatesChunk.readFromStream(stream);
		return new ChunkStats(coord, stream.readInt(), stream.readDouble());
	}
	
	public String toString(){
		return String.format("%s %s %s", this.coord, this.updateTime / 1000, this.nentities);
	}	
}
