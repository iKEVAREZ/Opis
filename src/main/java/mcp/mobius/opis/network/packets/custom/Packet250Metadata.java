package mcp.mobius.opis.network.packets.custom;

import java.util.ArrayList;

import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.network.enums.Message;
import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet250Metadata extends Packet250CustomPayload {

	public Message       dataReq;
	public ISerializable dataValue;
	public ArrayList<? extends ISerializable> dataList;
	
	
}
