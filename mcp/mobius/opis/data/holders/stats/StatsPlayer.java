package mcp.mobius.opis.data.holders.stats;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;

public class StatsPlayer extends StatAbstract {

	int eid;
	
	public StatsPlayer(EntityPlayer player){
		this.eid   = player.entityId;
		this.name  = player.getDisplayName();
		this.coord = new CoordinatesBlock(player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ);
		this.chunk = this.coord.asCoordinatesChunk();
	}

	public StatsPlayer(String name, int eid, CoordinatesBlock coord){
		this.eid  = eid;
		this.name = name;
		this.coord = coord;
		this.chunk = coord.asCoordinatesChunk();
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeInt(this.eid);
		this.coord.writeToStream(stream);
		Packet.writeString(this.name, stream);
	}

	public static  StatsPlayer readFromStream(DataInputStream stream) throws IOException {
		int eid = stream.readInt(); 
		CoordinatesBlock coords   = CoordinatesBlock.readFromStream(stream);
		String name = Packet.readString(stream, 255);
		return new StatsPlayer(name, eid, coords);
	}	
	
}
