package mcp.mobius.opis.network.packets.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.network.enums.Packets;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.ChunkCoordIntPair;

public class Packet_DataOverlayChunkEntities {

	public byte header;
	public HashMap<CoordinatesChunk, Integer> chunkStatus = new HashMap<CoordinatesChunk, Integer>();
	
	public Packet_DataOverlayChunkEntities(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		this.chunkStatus.clear();
		
		try{
			this.header  = inputStream.readByte();
			int nchunks  = inputStream.readInt();
			
			for (int i = 0; i <= nchunks; i++){
				CoordinatesChunk chunk = CoordinatesChunk.readFromStream(inputStream);
				this.chunkStatus.put(chunk, inputStream.readInt());
			}
				
			
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(HashMap<CoordinatesChunk, Integer> chunks){
		Packet250CustomPayload packet      = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.DATA_OVERLAY_CHUNK_ENTITIES);
			outputStream.writeInt(chunks.size());
			for (CoordinatesChunk chunk : chunks.keySet()){
				chunk.writeToStream(outputStream);
				outputStream.writeInt(chunks.get(chunk));
			}
			
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}	
	
}
