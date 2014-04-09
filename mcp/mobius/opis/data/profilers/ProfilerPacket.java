package mcp.mobius.opis.data.profilers;

import java.util.ArrayList;
import java.util.HashMap;

import mcp.mobius.opis.data.holders.newtypes.DataPacket;
import net.minecraft.network.packet.Packet;

public class ProfilerPacket extends ProfilerAbstract {

	public long dataAmount = 0;
	public ArrayList<DataPacket> data = new ArrayList<DataPacket>();
	
	public ProfilerPacket(){
		for (int i = 0; i < 254; i++)
			data.add(new DataPacket(i));
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
		}
	}

}
