package mcp.mobius.opis.data.profilers;

import net.minecraft.network.packet.Packet;

public class ProfilerPacket extends ProfilerAbstract {

	public long data = 0;
	
	@Override
	public void reset() {
		data = 0;
	}
	
	@Override
	public void start(Object key) {
		if (key != null){
			Packet packet = (Packet)key;
			data += packet.getPacketSize() + 1;
		}
	}

}
