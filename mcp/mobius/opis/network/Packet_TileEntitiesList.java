package mcp.mobius.opis.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import mcp.mobius.opis.data.TileEntityStatsData;
import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet_TileEntitiesList {

	public byte header;
	ArrayList<TileEntityStatsData> entities = new ArrayList<TileEntityStatsData>(); 
	
	public Packet_TileEntitiesList(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header  = inputStream.readByte();
			int ndata    = inputStream.readInt();
			for (int i = 0; i < ndata; i++)
				entities.add(TileEntityStatsData.readFromStream(inputStream));
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(ArrayList<TileEntityStatsData> stats){
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.TILEENTITIES_LIST);
			outputStream.writeInt(stats.size());
			for (TileEntityStatsData data : stats)
				data.writeToStream(outputStream);
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}	
	
}
