package mcp.mobius.opis.data;

import java.util.HashMap;

import cpw.mods.fml.common.network.Player;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import mcp.mobius.opis.data.holders.CoordinatesBlock;
import mcp.mobius.opis.data.holders.EntityStats;

public class EntityManager {

	public static HashMap<Integer, EntityStats> stats = new HashMap<Integer, EntityStats>();
	
	public static void addEntity(Entity ent, long timing){
		int entID = ent.entityId;
		String entName;
		entName = ent.getClass().getName();

		if (!(stats.containsKey(entID)))
			stats.put(entID, new EntityStats(entID, entName));
		stats.get(entID).addMeasure(timing);
	}	
	
	
	public static CoordinatesBlock getTeleportTarget(CoordinatesBlock coord){
		World world = DimensionManager.getWorld(coord.dim);
		if (world == null) {return null;}
		
		int maxOffset       = 16;
		boolean targetFound = false;
		
		if (coord.y > 0){
			for (int xoffset = 0; xoffset <= maxOffset; xoffset++){
				for (int zoffset = 0; zoffset <= maxOffset; zoffset++){
					if ( world.isAirBlock(coord.x + xoffset, coord.y,     coord.z + zoffset) && 
					     world.isAirBlock(coord.x + xoffset, coord.y + 1, coord.z + zoffset) &&
					    !world.isAirBlock(coord.x + xoffset, coord.y - 1, coord.z + zoffset))
						return new CoordinatesBlock(coord.dim, coord.x + xoffset, coord.y, coord.z + zoffset);
					
					if (world.isAirBlock(coord.x - xoffset, coord.y,     coord.z + zoffset) && 
					    world.isAirBlock(coord.x - xoffset, coord.y + 1, coord.z + zoffset) &&
					   !world.isAirBlock(coord.x - xoffset, coord.y - 1, coord.z + zoffset))				
						return new CoordinatesBlock(coord.dim, coord.x - xoffset, coord.y, coord.z + zoffset);
					
					if (world.isAirBlock(coord.x + xoffset, coord.y,     coord.z - zoffset) && 
						world.isAirBlock(coord.x + xoffset, coord.y + 1, coord.z - zoffset) &&
					   !world.isAirBlock(coord.x + xoffset, coord.y - 1, coord.z - zoffset))
						return new CoordinatesBlock(coord.dim, coord.x + xoffset, coord.y, coord.z - zoffset);
					
					if (world.isAirBlock(coord.x - xoffset, coord.y,     coord.z - zoffset) && 
						world.isAirBlock(coord.x - xoffset, coord.y + 1, coord.z - zoffset) &&
					   !world.isAirBlock(coord.x - xoffset, coord.y - 1, coord.z - zoffset))
						return new CoordinatesBlock(coord.dim, coord.x - xoffset, coord.y, coord.z - zoffset);				
				}
			}
		} else {
			int y = 256;
			while (world.isAirBlock(coord.x, y, coord.z) ||
				   world.getBlockId(coord.x, y, coord.z) == Block.vine.blockID
				  )
				y--;
	
			for (int xoffset = 0; xoffset <= maxOffset; xoffset++){
				for (int zoffset = 0; zoffset <= maxOffset; zoffset++){
					if (world.isAirBlock(coord.x + xoffset, y, coord.z + zoffset) && world.isAirBlock(coord.x + xoffset, y + 1, coord.z + zoffset))
						return new CoordinatesBlock(coord.dim, coord.x + xoffset, y, coord.z + zoffset);
					
					if (world.isAirBlock(coord.x - xoffset, y, coord.z + zoffset) && world.isAirBlock(coord.x - xoffset, y + 1, coord.z + zoffset))				
						return new CoordinatesBlock(coord.dim, coord.x - xoffset, y, coord.z + zoffset);
					
					if (world.isAirBlock(coord.x + xoffset, y, coord.z - zoffset) && world.isAirBlock(coord.x + xoffset, y + 1, coord.z - zoffset))
						return new CoordinatesBlock(coord.dim, coord.x + xoffset, y, coord.z - zoffset);
					
					if (world.isAirBlock(coord.x - xoffset, y, coord.z - zoffset) && world.isAirBlock(coord.x - xoffset, y + 1, coord.z - zoffset))
						return new CoordinatesBlock(coord.dim, coord.x - xoffset, y, coord.z - zoffset);				
				}
			}	
		}
		
		return null;
	}
	
	public static boolean teleportPlayer(CoordinatesBlock coord, EntityPlayerMP player){
		//System.out.printf("%s %s\n", coord, getTeleportTarget(coord));
		CoordinatesBlock target = EntityManager.getTeleportTarget(coord);
		if (target == null) return false;
		if (player.worldObj.provider.dimensionId != coord.dim) 
			player.travelToDimension(coord.dim);
		
		player.setPositionAndUpdate(target.x + 0.5, target.y, target.z + 0.5);
		
		return true;
	}
	
}
