package mcp.mobius.opis.data.profilers;

import java.util.ArrayList;
import java.util.HashMap;

import mcp.mobius.opis.data.holders.newtypes.DataPacket;
import mcp.mobius.opis.data.holders.newtypes.DataPacket250;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

public class ProfilerPacket extends ProfilerAbstract {

	public long dataAmount = 0;
	public ArrayList<DataPacket>          data    = new ArrayList<DataPacket>();
	public HashMap<String, DataPacket250> data250 = new HashMap<String, DataPacket250>();
	
	public ArrayList<DataPacket>	jabbaSpec     = new ArrayList<DataPacket>();
	
	public ProfilerPacket(){
		for (int i = 0; i < 254; i++)
			data.add(new DataPacket(i));
		
		/*
		for (int i = 0; i < 15; i++)
			jabbaSpec.add(new DataPacket(i));
		*/		
	}
	
	@Override
	public void reset() {
		dataAmount = 0;
	}
	
	@Override
	public void start(Object key) {
		if (key != null){
			Packet packet = (Packet)key;
			dataAmount += packet.getPacketSize() + 1;
			data.get(packet.getPacketId()).fill(packet);
			
			if (packet.getPacketId() == 250){
				String channel = ((Packet250CustomPayload)packet).channel;
				if (!data250.containsKey(channel))
					data250.put(channel, new DataPacket250(channel));
				
				data250.get(channel).fill(packet);
				
				/*
				if (channel.equals("JABBA")){
					byte ID = ((Packet250CustomPayload)packet).data[0];
					jabbaSpec.get(ID).fill(packet);
				}
				*/
			}
		}
	}

}
