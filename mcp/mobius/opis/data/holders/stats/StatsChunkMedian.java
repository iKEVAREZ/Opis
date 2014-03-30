package mcp.mobius.opis.data.holders.stats;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.world.ChunkCoordIntPair;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;

public class StatsChunkMedian extends StatAbstract{
	
	public StatsChunkMedian(){}
	
	public StatsChunkMedian(CoordinatesChunk chunk){
		this.chunk = chunk;
		this.coord = chunk.asCoordinatesBlock();
	}

	@Override
	public   void writeToStream(DataOutputStream stream) throws IOException{
		this.chunk.writeToStream(stream);
	}

	public static  StatsChunkMedian readFromStream(DataInputStream stream) throws IOException {
		CoordinatesChunk chunk   = CoordinatesChunk.readFromStream(stream);
		StatsChunkMedian chunkStats = new StatsChunkMedian(chunk);
		return chunkStats;
	}
	
	public String toString(){
		return String.format("%.3f µs", this.getGeometricMean());
	}
}