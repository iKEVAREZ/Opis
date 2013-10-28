package mcp.mobius.opis.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import mcp.mobius.opis.data.ChunkStatsData;
import mcp.mobius.opis.data.CoordinatesChunk;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.ChunkCoordIntPair;

public class Packet_MeanTime {

	public byte header;
	public HashMap<CoordinatesChunk, ChunkStatsData> chunkStatus = new HashMap<CoordinatesChunk, ChunkStatsData>();
	
	public Packet_MeanTime(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		this.chunkStatus.clear();
		
		try{
			this.header  = inputStream.readByte();
			int dim      = inputStream.readInt();
			int nchunks  = inputStream.readInt();
			
			for (int i = 0; i <= nchunks; i++){
				this.chunkStatus.put(new CoordinatesChunk(dim, inputStream.readInt(), inputStream.readInt()), new ChunkStatsData(inputStream.readInt(), inputStream.readDouble()));
			}
				
			
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(HashMap<CoordinatesChunk, ChunkStatsData> chunks, int dim){
		Packet250CustomPayload packet = new Packet250CustomPayload();
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
