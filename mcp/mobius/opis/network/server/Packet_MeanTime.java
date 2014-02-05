package mcp.mobius.opis.network.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import mcp.mobius.opis.data.holders.ChunkStats;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.network.Packets;
import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet_MeanTime {

	public byte header;
	public HashMap<CoordinatesChunk, ChunkStats> chunkStatus = new HashMap<CoordinatesChunk, ChunkStats>();
	
	public Packet_MeanTime(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		this.chunkStatus.clear();
		
		try{
			this.header  = inputStream.readByte();
			int dim      = inputStream.readInt();
			int nchunks  = inputStream.readInt();
			
			for (int i = 0; i <= nchunks; i++){
				CoordinatesChunk coord = new CoordinatesChunk(dim, inputStream.readInt(), inputStream.readInt()); 
				this.chunkStatus.put(coord, new ChunkStats(coord, inputStream.readInt(), inputStream.readDouble()));
			}
				
			
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(HashMap<CoordinatesChunk, ChunkStats> chunks, int dim){
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
				outputStream.writeInt(chunks.get(chunk).nentities);
				outputStream.writeDouble(chunks.get(chunk).updateTime);
			}
			
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}		
	
}
