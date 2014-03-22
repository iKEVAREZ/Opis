package mcp.mobius.opis.network.custom;

import java.util.ArrayList;

import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.network.enums.DataReq;
import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet250Metadata extends Packet250CustomPayload {

	public DataReq       dataReq;
	public ISerializable dataValue;
	public ArrayList<? extends ISerializable> dataList;
	
	
}
