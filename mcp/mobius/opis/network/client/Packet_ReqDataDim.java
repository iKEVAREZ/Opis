package mcp.mobius.opis.network.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.network.Packets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet_ReqDataDim {

	public byte   header;
	public int    dimension;
	public String datatype;
	
	public Packet_ReqDataDim(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header    = inputStream.readByte();
			this.dimension = inputStream.readInt();
			this.datatype  = Packet.readString(inputStream, 255);
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(int dimension, String datatype){
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.REQ_DATA_DIM);
			outputStream.writeInt(dimension);
			Packet.writeString(datatype, outputStream);
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}	
	
}
