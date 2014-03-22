package mcp.mobius.opis.network.server;

import net.minecraft.network.packet.Packet250CustomPayload;
import mcp.mobius.opis.network.enums.DataReq;

public class Packet_DataAbstract {

	public byte     header;
	public DataReq dataReq;	
	public Packet250CustomPayload packet;
}
