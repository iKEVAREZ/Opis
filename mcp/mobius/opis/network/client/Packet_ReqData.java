package mcp.mobius.opis.network.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.network.Packets;

public class Packet_ReqData {
	
	public byte             header;
	public CoordinatesChunk coord;
	public String           datatype;
	
	public Packet_ReqData(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header    = inputStream.readByte();
			this.coord     = CoordinatesChunk.readFromStream(inputStream);
			this.datatype  = Packet.readString(inputStream, 255);
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(String datatype){
		return Packet_ReqData.create(new CoordinatesChunk(0, 0, 0), datatype);
	}		
	
	public static Packet250CustomPayload create(int dim, int x, int z, String datatype){
		return Packet_ReqData.create(new CoordinatesChunk(dim, x, z), datatype);
	}	
	
	public static Packet250CustomPayload create(int dim, String datatype){
		return Packet_ReqData.create(new CoordinatesChunk(dim, 0,0), datatype);
	}
	
	public static Packet250CustomPayload create(CoordinatesChunk coord, String datatype){
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.REQ_DATA);
			coord.writeToStream(outputStream);
			Packet.writeString(datatype, outputStream);
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}	
}
