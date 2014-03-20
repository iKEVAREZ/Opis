package mcp.mobius.opis.network.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.network.Packets;
import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet_MeanTime {

	public byte header;
	public HashMap<CoordinatesChunk, StatsChunk> chunkStatus = new HashMap<CoordinatesChunk, StatsChunk>();
	
	public Packet_MeanTime(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		this.chunkStatus.clear();
		
		try{
			this.header  = inputStream.readByte();
			int dim      = inputStream.readInt();
			int nchunks  = inputStream.readInt();
			
			for (int i = 0; i <= nchunks; i++){
				CoordinatesChunk coord = new CoordinatesChunk(dim, inputStream.readInt(), inputStream.readInt()); 
				this.chunkStatus.put(coord, new StatsChunk(coord, inputStream.readInt(), inputStream.readInt(), inputStream.readDouble()));
			}
				
			
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(HashMap<CoordinatesChunk, StatsChunk> chunks, int dim){
		Packet250CustomPayload packet      = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.MEANTIME);
			outputStream.writeInt(dim);
			outputStream.writeInt(chunks.size());
			
			for (CoordinatesChunk chunk : chunks.keySet()){
				outputStream.writeInt(chunk.chunkX);
				outputStream.writeInt(chunk.chunkZ);
				outputStream.writeInt(chunks.get(chunk).tileEntities);
				outputStream.writeInt(chunks.get(chunk).entities);
				outputStream.writeDouble(chunks.get(chunk).getDataSum());
			}
			
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}		
	
}
