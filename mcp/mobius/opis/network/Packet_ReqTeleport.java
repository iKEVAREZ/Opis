package mcp.mobius.opis.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.CoordinatesBlock;
import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet_ReqTeleport {
	
	public byte header;
	public CoordinatesBlock coord;

	public Packet_ReqTeleport(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header    = inputStream.readByte();
			this.coord     = CoordinatesBlock.readFromStream(inputStream);
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(CoordinatesBlock coord){
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.REQ_TELEPORT);
			coord.writeToStream(outputStream);
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}
}
