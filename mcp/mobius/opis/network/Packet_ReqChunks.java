package mcp.mobius.opis.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.network.packet.Packet250CustomPayload;
import mcp.mobius.opis.data.holders.CoordinatesChunk;

public class Packet_ReqChunks {

	public byte header;
	public int dim;
	public ArrayList<CoordinatesChunk> chunks = new ArrayList<CoordinatesChunk>();
	
	public Packet_ReqChunks(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header    = inputStream.readByte();
			this.dim       = inputStream.readInt();
			int nchunks    = inputStream.readInt();
			for (int i = 0; i < nchunks; i++)
				chunks.add(CoordinatesChunk.readFromStream(inputStream));
			
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(int dim, ArrayList<CoordinatesChunk> chunks){
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.REQ_CHUNKS);
			outputStream.writeInt(dim);
			outputStream.writeInt(chunks.size());
			for(CoordinatesChunk coord : chunks)
				coord.writeToStream(outputStream);
			
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}	
	
}
