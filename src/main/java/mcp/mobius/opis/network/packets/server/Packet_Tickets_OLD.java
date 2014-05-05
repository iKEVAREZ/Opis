package mcp.mobius.opis.network.packets.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;

import mcp.mobius.opis.data.holders.basetypes.TicketData;
import mcp.mobius.opis.network.enums.Packets;
import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet_Tickets_OLD {

	public byte header;
	public HashSet<TicketData> tickets = new HashSet<TicketData>();
	
	public Packet_Tickets_OLD(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header = inputStream.readByte();
			int ntickets = inputStream.readInt();
			
			for (int i = 0; i < ntickets; i++)
				tickets.add(TicketData.readFromStream(inputStream));
			
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(HashSet<TicketData>  data){
		Packet250CustomPayload packet      = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.TICKETS);
			outputStream.writeInt(data.size());
			for (TicketData ticket : data)
				ticket.writeToStream(outputStream);
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}		
	
}
