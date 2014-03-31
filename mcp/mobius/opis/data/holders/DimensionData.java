package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

/* Data holder for infos about dimensions */

public class DimensionData implements ISerializable {

	public int    dim;
	public String name;
	public int    players;
	public int    forced;
	public int    loaded;
	public int    entities;
	public int    mobs;
	public int    neutral;
	public double update;
	
	public DimensionData fill(int dim){
		WorldServer  world = DimensionManager.getWorld(dim);
		
		this.dim     = dim;
		this.name    = world.provider.getDimensionName();
		this.players = world.playerEntities.size();
		this.forced  = world.getPersistentChunks().size();
		this.loaded  = world.getChunkProvider().getLoadedChunkCount();
		this.update  = 0.0D;
		this.mobs     = 0;
		this.neutral  = 0;
		this.entities = world.loadedEntityList.size();		
		
		for (Entity entity : (ArrayList<Entity>)world.loadedEntityList){
			if (entity instanceof EntityMob)
				this.mobs += 1;
			if (entity instanceof EntityAnimal)
				this.neutral += 1;
		}
		
		return this;
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeInt(dim);
		stream.writeInt(players);
		stream.writeInt(forced);
		stream.writeInt(loaded);
		stream.writeInt(entities);
		stream.writeInt(mobs);
		stream.writeInt(neutral);
		stream.writeDouble(update);
		Packet.writeString(name, stream);
	}

	public static DimensionData readFromStream(DataInputStream stream) throws IOException {
		DimensionData retVal = new DimensionData();
		retVal.dim     = stream.readInt();
		retVal.players = stream.readInt();
		retVal.forced  = stream.readInt();
		retVal.loaded  = stream.readInt();
		retVal.entities= stream.readInt();
		retVal.mobs    = stream.readInt();
		retVal.neutral = stream.readInt();
		retVal.update  = stream.readDouble();
		retVal.name    = Packet.readString(stream, 255);
		return retVal;
	}
}
