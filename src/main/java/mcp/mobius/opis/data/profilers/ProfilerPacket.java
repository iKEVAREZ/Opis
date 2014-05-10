package mcp.mobius.opis.data.profilers;

import java.util.ArrayList;
import java.util.HashMap;

import cpw.mods.fml.relauncher.Side;
import mcp.mobius.opis.data.holders.newtypes.DataPacket;
import mcp.mobius.opis.data.holders.newtypes.DataPacket250;
import mcp.mobius.opis.helpers.Helpers;

public class ProfilerPacket extends ProfilerAbstract {

	public long dataAmount = 0;
	public ArrayList<DataPacket>          data    = new ArrayList<DataPacket>();
	public HashMap<String, DataPacket250> data250 = new HashMap<String, DataPacket250>();
	
	public ArrayList<DataPacket>	jabbaSpec     = new ArrayList<DataPacket>();
	
	public ProfilerPacket(){
		for (int i = 0; i < 256; i++)
			data.add(new DataPacket(i));
		
	}

	public void startInterval(){
		for (DataPacket packet : data)
			packet.startInterval();
		
		for (DataPacket250 packet : data250.values())
			packet.startInterval();
	}
	
	@Override
	public void reset() {
		dataAmount = 0;
	}
	
	@Override
	public void start(Object key) {
		/*
		if (key != null && Helpers.getEffectiveSide() == Side.SERVER){
			//System.out.printf("%s | %s\n", Thread.currentThread().getClass(), Thread.currentThread().getName());
			
			Packet packet = (Packet)key;
			dataAmount += packet.getPacketSize() + 1;
			data.get(packet.getPacketId()).fill(packet);
			
			if (packet.getPacketId() == 250){
				String channel = ((Packet250CustomPayload)packet).channel;

				try{
					data250.get(channel).fill(packet);
				} catch (Exception e){
					data250.put(channel, new DataPacket250(channel));
					data250.get(channel).fill(packet);
				}
			}
		}
		*/
	}

}
