package mcp.mobius.opis.data.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import mcp.mobius.opis.data.holders.ChunkStats;
import mcp.mobius.opis.data.holders.CoordinatesBlock;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.data.holders.ModStats;
import mcp.mobius.opis.data.holders.TileEntityStats;
import mcp.mobius.opis.tools.ModIdentification;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

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
			System.out.printf("%s\n", e);
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

			World world     = DimensionManager.getWorld(coord.dim);
			int   blockID   = world.getBlockId(coord.x, coord.y, coord.z);
			short blockMeta = (short)world.getBlockMetadata(coord.x, coord.y, coord.z);
			stats.put(coord, new TileEntityStats(coord, blockID, blockMeta));
			
			//stats.put(coord, new TileEntityStats(coord, teName));
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
				
				chunks.get(coordC).addEntity();
				chunks.get(coordC).addMeasure(stats.get(coord).getGeometricMean());
			}
		}
		return chunks;
	}
	
	private static void cleanUpStats(){
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
	}
	
	public static ArrayList<TileEntityStats> getInChunk(CoordinatesChunk coord){
		cleanUpStats();
		
		ArrayList<TileEntityStats> returnList = new ArrayList<TileEntityStats>();
		
		for (CoordinatesBlock tecoord : TileEntityManager.stats.keySet()){
			if (coord.equals(new CoordinatesChunk(tecoord))){
		        TileEntityStats testats = TileEntityManager.stats.get(tecoord);
				
		        /*
				int x = testats.getCoordinates().x;
				int y = testats.getCoordinates().y;
				int z = testats.getCoordinates().z;		        
		        
				World world = DimensionManager.getWorld(tecoord.dim);
		        Block mouseoverBlock = Block.blocksList[world.getBlockId(x, y, z)];
		        
		        try{
	        		ItemStack stack = new ItemStack(mouseoverBlock, 1, world.getBlockMetadata(x, y, z));
		            testats.setType(stack.getDisplayName());
		            
		        }catch (Exception e){
		        	try{
		        		ItemStack stack = mouseoverBlock.getBlockDropped(world, x, y, z, world.getBlockMetadata(x, y, z), 0).get(0);
		        		testats.setType(stack.getDisplayName());		        		
		        	}catch (Exception f){}
		        }
		        */
		        
				returnList.add(testats);
			}
		}
		
		return returnList;
	}
	
	public static ArrayList<TileEntityStats> getTopEntities(int quantity){
		cleanUpStats();	
		
		ArrayList<TileEntityStats> sortedEntities = new ArrayList(TileEntityManager.stats.values());
		ArrayList<TileEntityStats> topEntities    = new ArrayList<TileEntityStats>();
		Collections.sort(sortedEntities);
		
		
		for (int i = 0; i < Math.min(quantity, sortedEntities.size()); i++){
			TileEntityStats testats = sortedEntities.get(i);

			/*
			int x = testats.getCoordinates().x;
			int y = testats.getCoordinates().y;
			int z = testats.getCoordinates().z;
			
			World world = DimensionManager.getWorld(testats.getCoordinates().dim);
	        Block mouseoverBlock = Block.blocksList[world.getBlockId(x, y, z)];
	        //MovingObjectPosition mop = new MovingObjectPosition(x, y, z, 0, Vec3.createVectorHelper(0, 0, 0));
	        
	        try{
        		ItemStack stack = new ItemStack(mouseoverBlock, 1, world.getBlockMetadata(x, y, z));
	            testats.setType(stack.getDisplayName());
	        }catch (Exception e){
	        	try{
		            ItemStack stack = mouseoverBlock.getBlockDropped(world, x, y, z, world.getBlockMetadata(x, y, z), 0).get(0);
	        		testats.setType(stack.getDisplayName());
	        	}catch (Exception f){}
	        }			
			*/
			
			topEntities.add(testats);
		}
		
		return topEntities;
		
	}
	
	public static ArrayList<ModStats> getModStats(){
		cleanUpStats();
		HashMap<String, ModStats> modStats = new HashMap<String, ModStats>();
		
		for (TileEntityStats testat : TileEntityManager.stats.values()){
			String modID = ModIdentification.idFromStack(new ItemStack(testat.getID(), 1, testat.getMeta()));
			if (!modStats.containsKey(modID))
				modStats.put(modID, new ModStats(modID));
			
			modStats.get(modID).addStat(testat);
		}
		
		ArrayList<ModStats> outModStats = new ArrayList<ModStats>(modStats.values());
		
		//Collections.sort(outModStats);
		
		return outModStats;
	}
}
