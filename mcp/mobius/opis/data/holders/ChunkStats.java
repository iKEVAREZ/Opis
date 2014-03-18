package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChunkStats extends StatAbstract{
	public int    tileEntities = 0;
	public int    entities     = 0;
	
	public ChunkStats(){}
	
	public ChunkStats(CoordinatesChunk chunk){
		this.chunk = chunk;
	}

	public ChunkStats(CoordinatesChunk chunk, int tileEntities, int entities){
		this.chunk = chunk;
		this.tileEntities = tileEntities;
		this.entities     = entities;
	}	
	
	public ChunkStats(CoordinatesChunk chunk, int tileEntities, int entities, double time){
		this.chunk = chunk;
		this.tileEntities = tileEntities;
		this.entities     = entities;
		this.setDataSum(time);
	}		
	
	public void addTileEntity(){
		this.tileEntities += 1;
	}
	
	public void addEntity(){
		this.entities += 1;
	}
	
	@Override
	public CoordinatesChunk getChunk(){
		return this.chunk;
	}	
	
	@Override
	public   void writeToStream(DataOutputStream stream) throws IOException{
		this.chunk.writeToStream(stream);
		stream.writeInt(this.tileEntities);
		stream.writeInt(this.entities);
		stream.writeDouble(this.getDataSum());
	}

	public static  ChunkStats readFromStream(DataInputStream stream) throws IOException {
		CoordinatesChunk chunk   = CoordinatesChunk.readFromStream(stream);
		ChunkStats chunkStats = new ChunkStats(chunk, stream.readInt(), stream.readInt());
		chunkStats.setDataSum(stream.readDouble());
		return chunkStats;
	}
	
	@Override
	public int compareTo(Object o) {
		double value = ((StatAbstract)o).getDataSum() - this.getDataSum();
		if (value > 0)
			return 1;
		if (value < 0)
			return -1;
		return 0;
	}		
	
	public String toString(){
		return String.format("%s %s %s", this.chunk, this.getDataSum() / 1000, this.tileEntities);
	}	
}
