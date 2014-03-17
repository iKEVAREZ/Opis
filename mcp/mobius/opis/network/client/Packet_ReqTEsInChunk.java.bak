package mcp.mobius.opis.network.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.network.Packets;
import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet_ReqTEsInChunk {

	public byte header;
	public CoordinatesChunk chunk;
	
	public Packet_ReqTEsInChunk(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header    = inputStream.readByte();
			this.chunk     = new CoordinatesChunk(inputStream.readInt(), inputStream.readInt(), inputStream.readInt());
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(CoordinatesChunk chunk){
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.REQ_TES_IN_CHUNK);
			outputStream.writeInt(chunk.dim);
			outputStream.writeInt(chunk.chunkX);
			outputStream.writeInt(chunk.chunkZ);
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}		
	
}
