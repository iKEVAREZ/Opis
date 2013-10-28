package mcp.mobius.opis.data;

import java.util.HashMap;

import net.minecraft.tileentity.TileEntity;

public class TileEntitiesData {
	public static HashMap<CoordinatesBlock, Class> references = new HashMap<CoordinatesBlock, Class>();
	public static HashMap<CoordinatesBlock, TileEntityStatsData> stats = new HashMap<CoordinatesBlock, TileEntityStatsData>();

	public static void addTileEntity(TileEntity te, long timing){
		CoordinatesBlock coord = new CoordinatesBlock(te);
		if (references.containsKey(coord) && references.get(coord) != te.getClass()){
			references.remove(coord);
			stats.remove(coord);
		}
		
		if (!(references.containsKey(coord))){
			references.put(coord, te.getClass());
			stats.put(coord, new TileEntityStatsData());
		}
			
		stats.get(coord).addMeasure(timing);
	}
	
	public static HashMap<CoordinatesChunk, ChunkStatsData> getTimes(int dim){
		
		HashMap<CoordinatesChunk, ChunkStatsData> chunks = new HashMap<CoordinatesChunk, ChunkStatsData>();
		
		for (CoordinatesBlock coord : TileEntitiesData.stats.keySet()){
			if (coord.dim == dim){

				CoordinatesChunk coordC = new CoordinatesChunk(coord);
				if (!(chunks.containsKey(coordC)))
					chunks.put(coordC, new ChunkStatsData());
				
				chunks.get(coordC).nentities  += 1;
				chunks.get(coordC).updateTime += stats.get(coord).getGeometricMean();
			
			}
		}
		
		return chunks;
	}
	
	
}
