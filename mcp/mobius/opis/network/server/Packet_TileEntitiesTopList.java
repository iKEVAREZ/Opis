package mcp.mobius.opis.network.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import mcp.mobius.opis.data.holders.TileEntityStats;
import mcp.mobius.opis.network.Packet251Extended;
import mcp.mobius.opis.network.Packets;
import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet_TileEntitiesTopList {

	public byte header;
	public ArrayList<TileEntityStats> entities = new ArrayList<TileEntityStats>(); 
	
	public Packet_TileEntitiesTopList(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header  = inputStream.readByte();
			int ndata    = inputStream.readInt();
			for (int i = 0; i < ndata; i++)
				entities.add(TileEntityStats.readFromStream(inputStream));
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(ArrayList<TileEntityStats> stats){
		Packet251Extended packet      = new Packet251Extended();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.TILEENTITIES_TOPLIST);
			outputStream.writeInt(stats.size());
			for (TileEntityStats data : stats)
				data.writeToStream(outputStream);
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}	
	
}
