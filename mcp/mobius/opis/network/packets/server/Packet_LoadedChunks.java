package mcp.mobius.opis.network.packets.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import mcp.mobius.opis.network.enums.Packets;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.ChunkCoordIntPair;

public class Packet_LoadedChunks {

	public byte header;
	public HashMap<ChunkCoordIntPair, Boolean> chunkStatus = new HashMap<ChunkCoordIntPair, Boolean>();
	
	public Packet_LoadedChunks(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		this.chunkStatus.clear();
		
		try{
			this.header  = inputStream.readByte();
			int nchunks  = inputStream.readInt();
			
			for (int i = 0; i <= nchunks; i++){
				this.chunkStatus.put(new ChunkCoordIntPair(inputStream.readInt(), inputStream.readInt()), inputStream.readBoolean());
			}
				
			
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(HashMap<ChunkCoordIntPair, Boolean> chunks){
		Packet250CustomPayload packet      = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.LOADED_CHUNKS);
			outputStream.writeInt(chunks.size());
			for (ChunkCoordIntPair chunk : chunks.keySet()){
				outputStream.writeInt(chunk.chunkXPos);
				outputStream.writeInt(chunk.chunkZPos);
				outputStream.writeBoolean(chunks.get(chunk));
			}
			
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}	
	
}
