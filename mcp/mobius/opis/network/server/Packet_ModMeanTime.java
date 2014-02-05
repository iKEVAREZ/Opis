package mcp.mobius.opis.network.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.network.packet.Packet250CustomPayload;
import mcp.mobius.opis.data.holders.ModStats;
import mcp.mobius.opis.network.Packets;

public class Packet_ModMeanTime {

	public byte header;
	public ArrayList<ModStats> modStats = new ArrayList<ModStats>(); 	
	
	public Packet_ModMeanTime(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header  = inputStream.readByte();
			int ndata    = inputStream.readInt();
			for (int i = 0; i < ndata; i++)
				modStats.add(ModStats.readFromStream(inputStream));
		} catch (IOException e){}				
	}	

	public static Packet250CustomPayload create(ArrayList<ModStats> stats){
		Packet250CustomPayload packet      = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.MODMEANTIME);
			outputStream.writeInt(stats.size());
			for (ModStats data : stats)
				data.writeToStream(outputStream);
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}	
	
}