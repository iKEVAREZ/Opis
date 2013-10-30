package mcp.mobius.opis.data;

import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import mcp.mobius.opis.data.holders.CoordinatesBlock;

public class EntityManager {

	public static CoordinatesBlock getTeleportTarget(CoordinatesBlock coord){
		World world = DimensionManager.getWorld(coord.dim);
		if (world == null) {return null;}
		
		int maxOffset       = 16;
		boolean targetFound = false;
		
		for (int xoffset = 0; xoffset <= maxOffset; xoffset++){
			for (int zoffset = 0; zoffset <= maxOffset; zoffset++){
				if (world.isAirBlock(coord.x + xoffset, coord.y, coord.z + zoffset) && world.isAirBlock(coord.x + xoffset, coord.y + 1, coord.z + zoffset))
					return new CoordinatesBlock(coord.dim, coord.x + xoffset, coord.y, coord.z + zoffset);
				
				if (world.isAirBlock(coord.x - xoffset, coord.y, coord.z + zoffset) && world.isAirBlock(coord.x - xoffset, coord.y + 1, coord.z + zoffset))				
					return new CoordinatesBlock(coord.dim, coord.x - xoffset, coord.y, coord.z + zoffset);
				
				if (world.isAirBlock(coord.x + xoffset, coord.y, coord.z - zoffset) && world.isAirBlock(coord.x + xoffset, coord.y + 1, coord.z - zoffset))
					return new CoordinatesBlock(coord.dim, coord.x + xoffset, coord.y, coord.z - zoffset);
				
				if (world.isAirBlock(coord.x - xoffset, coord.y, coord.z - zoffset) && world.isAirBlock(coord.x - xoffset, coord.y + 1, coord.z - zoffset))
					return new CoordinatesBlock(coord.dim, coord.x - xoffset, coord.y, coord.z - zoffset);				
			}
		}
		
		return null;
	}
	
	public static boolean teleportPlayer(CoordinatesBlock coord, EntityPlayerMP player){
		//System.out.printf("%s %s\n", coord, getTeleportTarget(coord));
		CoordinatesBlock target = EntityManager.getTeleportTarget(coord);
		if (target == null) return false;
		if (player.worldObj.provider.dimensionId != coord.dim) return false;
		
		player.setPositionAndUpdate(target.x + 0.5, target.y, target.z + 0.5);
		
		return true;
	}
	
}
