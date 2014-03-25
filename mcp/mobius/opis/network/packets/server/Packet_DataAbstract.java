package mcp.mobius.opis.network.packets.server;

import net.minecraft.network.packet.Packet250CustomPayload;
import mcp.mobius.opis.network.enums.Message;

public class Packet_DataAbstract {

	public byte     header;
	public Message  msg;	
	public Packet250CustomPayload packet;
}
