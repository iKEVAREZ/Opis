package mcp.mobius.opis.data;

import java.util.ArrayList;
import java.util.HashMap;

import mcp.mobius.opis.data.holders.ChunkStats;
import mcp.mobius.opis.data.holders.CoordinatesBlock;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.data.holders.TileEntityStats;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class TileEntityManager {
	public static HashMap<CoordinatesBlock, Class> references = new HashMap<CoordinatesBlock, Class>();
	public static HashMap<CoordinatesBlock, TileEntityStats> stats = new HashMap<CoordinatesBlock, TileEntityStats>();

	public static void addTileEntity(TileEntity te, long timing){
		CoordinatesBlock coord = new CoordinatesBlock(te);
		
		String teName;
		/*
		try{
			teName = te.blockType.getLocalizedName();
		} catch (Exception e){
			teName = te.getClass().getName();
		}
		*/
		teName = te.getClass().getName();
		
		if (references.containsKey(coord) && references.get(coord) != te.getClass()){
			references.remove(coord);
			stats.remove(coord);
		}
		
		if (!(references.containsKey(coord))){
			references.put(coord, te.getClass());
			stats.put(coord, new TileEntityStats(coord, teName));
		}
			
		stats.get(coord).addMeasure(timing);
	}
	
	public static HashMap<CoordinatesChunk, ChunkStats> getTimes(int dim){
		HashMap<CoordinatesChunk, ChunkStats> chunks = new HashMap<CoordinatesChunk, ChunkStats>();
		
		for (CoordinatesBlock coord : TileEntityManager.stats.keySet()){
			if (coord.dim == dim){

				CoordinatesChunk coordC = new CoordinatesChunk(coord);
				if (!(chunks.containsKey(coordC)))
					chunks.put(coordC, new ChunkStats());
				
				chunks.get(coordC).nentities  += 1;
				chunks.get(coordC).updateTime += stats.get(coord).getGeometricMean();
			}
		}
		return chunks;
	}
	
	public static ArrayList<TileEntityStats> getInChunk(CoordinatesChunk coord){
		ArrayList<TileEntityStats> returnList = new ArrayList<TileEntityStats>();
		
		for (CoordinatesBlock tecoord : TileEntityManager.stats.keySet()){
			if (coord.equals(new CoordinatesChunk(tecoord)))
				returnList.add(TileEntityManager.stats.get(tecoord));
		}
		
		return returnList;
	}
}
