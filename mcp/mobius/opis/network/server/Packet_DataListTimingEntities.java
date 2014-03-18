package mcp.mobius.opis.network.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.network.packet.Packet250CustomPayload;
import mcp.mobius.opis.data.holders.EntityStats;
import mcp.mobius.opis.network.Packets;

public class Packet_DataListTimingEntities {

	public byte header;
	public ArrayList<EntityStats> entities = new ArrayList<EntityStats>(); 
	
	public Packet_DataListTimingEntities(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header  = inputStream.readByte();
			int ndata    = inputStream.readInt();
			for (int i = 0; i < ndata; i++)
				entities.add(EntityStats.readFromStream(inputStream));
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(ArrayList<EntityStats> stats){
		Packet250CustomPayload packet      = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.DATA_LIST_TIMING_ENTITIES);
			outputStream.writeInt(stats.size());
			for (EntityStats data : stats)
				data.writeToStream(outputStream);
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}	
	
}
