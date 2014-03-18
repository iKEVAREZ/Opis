package mcp.mobius.opis.network.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import mcp.mobius.opis.network.Packets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet_DataListAmountEntities {

	public byte header;
	public HashMap<String, Integer> entities = new HashMap<String, Integer>(); 
	
	public Packet_DataListAmountEntities(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header  = inputStream.readByte();
			int ndata    = inputStream.readInt();
			for (int i = 0; i < ndata; i++){
				String name = Packet.readString(inputStream, 255);
				int    amount = inputStream.readInt();
				entities.put(name, amount);
			}
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(HashMap<String, Integer> entities){
		Packet250CustomPayload packet      = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.DATA_LIST_AMOUNT_ENTITIES);
			outputStream.writeInt(entities.keySet().size());
			for (String name : entities.keySet()){
				packet.writeString(name, outputStream);
				outputStream.writeInt(entities.get(name));
			}
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}	
	
}
