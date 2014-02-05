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

public class Packet_DataListChunkEntities {
	public byte header;
	public ArrayList<EntityStats> stats = new ArrayList<EntityStats>();
	
	public Packet_DataListChunkEntities(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		this.stats.clear(); 
		
		try{
			this.header  = inputStream.readByte();
			int nents    = inputStream.readInt();
			
			for (int i = 0; i <= nents; i++){
				EntityStats stat = EntityStats.readFromStream(inputStream);
				this.stats.add(stat);
			}
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(ArrayList<EntityStats> entities){
		Packet250CustomPayload packet      = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.DATA_LIST_CHUNK_ENTITIES);
			outputStream.writeInt(entities.size());
			for (EntityStats stat : entities){
				stat.writeToStream(outputStream);
			}
			
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}	
}
