package mcp.mobius.opis.data.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import mcp.mobius.mobiuscore.profiler_v2.ProfilerSection;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.newtypes.DataTileEntity;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.data.profilers.ProfilerTileEntityUpdate;
import mcp.mobius.opis.helpers.ModIdentification;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public enum TileEntityManager {
	INSTANCE;
	
	public HashMap<CoordinatesChunk, StatsChunk> getTimes(int dim){
		HashMap<CoordinatesChunk, StatsChunk> chunks = new HashMap<CoordinatesChunk, StatsChunk>();
		
		for (CoordinatesBlock coord : ((ProfilerTileEntityUpdate)ProfilerSection.TILEENT_UPDATETIME.getProfiler()).data.keySet()){
			if (coord.dim == dim){

				CoordinatesChunk coordC = new CoordinatesChunk(coord);
				if (!(chunks.containsKey(coordC)))
					chunks.put(coordC, new StatsChunk());
				
				chunks.get(coordC).addEntity();
				chunks.get(coordC).addMeasure(((ProfilerTileEntityUpdate)ProfilerSection.TILEENT_UPDATETIME.getProfiler()).data.get(coord).getGeometricMean());
			}
		}
		return chunks;
	}
	
	private void cleanUpStats(){
		
		/*
		HashSet<CoordinatesBlock> dirty = new HashSet<CoordinatesBlock>();
		
		for (CoordinatesBlock tecoord : TileEntityManager.stats.keySet()){
				World world     = DimensionManager.getWorld(tecoord.dim);
				int   blockID   = world.getBlockId(tecoord.x, tecoord.y, tecoord.z);
				short blockMeta = (short)world.getBlockMetadata(tecoord.x, tecoord.y, tecoord.z);
				
				if ((blockID != TileEntityManager.stats.get(tecoord).getID()) || (blockMeta != TileEntityManager.stats.get(tecoord).getMeta())){
					dirty.add(tecoord);
				}
		}
		
		for (CoordinatesBlock tecoord : dirty){
			stats.remove(tecoord);
			references.remove(tecoord);
		}
		*/
		
	}
	
	public ArrayList<DataTileEntity> getTileEntitiesInChunk(CoordinatesChunk coord){
		cleanUpStats();
		
		ArrayList<DataTileEntity> returnList = new ArrayList<DataTileEntity>();
		
		for (CoordinatesBlock tecoord : ((ProfilerTileEntityUpdate)ProfilerSection.TILEENT_UPDATETIME.getProfiler()).data.keySet()){
			if (coord.equals(tecoord.asCoordinatesChunk())){
				DataTileEntity testats = new DataTileEntity().fill(tecoord);
				
				returnList.add(testats);
			}
		}
		
		return returnList;
	}
	
	public ArrayList<DataTileEntity> getWorses(int amount){	
		ArrayList<DataTileEntity> sorted      = new ArrayList<DataTileEntity>();
		ArrayList<DataTileEntity> topEntities = new ArrayList<DataTileEntity>();
		
		for (CoordinatesBlock coord : ((ProfilerTileEntityUpdate)ProfilerSection.TILEENT_UPDATETIME.getProfiler()).data.keySet())
			sorted.add(new DataTileEntity().fill(coord));
		
		Collections.sort(sorted);
		
		for (int i = 0; i < Math.min(amount, sorted.size()); i++)
			topEntities.add(sorted.get(i));
		

		return topEntities;		
	}

	public DataTiming getTotalUpdateTime(){
		double updateTime = 0D;
		for (CoordinatesBlock coords : ((ProfilerTileEntityUpdate)ProfilerSection.TILEENT_UPDATETIME.getProfiler()).data.keySet()){
			updateTime += ((ProfilerTileEntityUpdate)ProfilerSection.TILEENT_UPDATETIME.getProfiler()).data.get(coords).getGeometricMean();
		}
		return new DataTiming(updateTime);
	}
	
	public int getAmountTileEntities(){
		int amountTileEntities = 0;
		for (WorldServer world : DimensionManager.getWorlds()){
			amountTileEntities += world.loadedTileEntityList.size();
		}
		return amountTileEntities;		
	}
}
