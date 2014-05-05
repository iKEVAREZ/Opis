package mcp.mobius.opis.network.packets.server;

import java.io.DataInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;

import net.minecraft.network.packet.Packet250CustomPayload;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.newtypes.DataError;
import mcp.mobius.opis.network.enums.Message;

public class NetDataRaw_OLD {

	public byte     header;
	public Message  msg;
	public String   clazzStr;
	public Class    clazz;
	public Packet250CustomPayload  packet;
	public ArrayList<ISerializable> array = null;
	public ISerializable            value = null;
	
	protected Class getClass(String datatypeStr){
		try{
			return  Class.forName(datatypeStr);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}	
	
	protected ISerializable dataRead(Class datatype, DataInputStream istream){
		if (datatype == null) return new DataError();
		
		try{
			Method readFromStream = datatype.getMethod("readFromStream", DataInputStream.class);
			
			return (ISerializable)readFromStream.invoke(null, istream);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
