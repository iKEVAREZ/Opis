package mcp.mobius.opis.data.server;

import net.minecraft.network.packet.Packet;
import mcp.mobius.mobiuscore.profiler.IProfilerPacket;

public class PacketProfiler implements IProfilerPacket {

	private static PacketProfiler _instance = new PacketProfiler();
	public  static PacketProfiler instance() {return _instance;}	
	
	public long dataSizeOut = 0;
	public long dataSizeIn  = 0;
	
	@Override
	public void addPacketOut(Packet packet) {
		if (packet != null)
			dataSizeOut += packet.getPacketSize() + 1;
	}

	@Override
	public void addPacketIn(Packet packet) {
		if (packet != null)
			dataSizeIn += packet.getPacketSize() + 1;
	}

}
