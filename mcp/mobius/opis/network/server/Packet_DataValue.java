package mcp.mobius.opis.network.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.network.Packets;
import mcp.mobius.opis.network.enums.DataReq;

public class Packet_DataValue {

	public byte header;
	public DataReq dataReq;
	public ISerializable data; 
	
	public Packet_DataValue(Packet250CustomPayload packet) {
		DataInputStream istream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header   = istream.readByte();
			this.dataReq  = DataReq.values()[istream.readInt()];
			String datatype = "";
			datatype = Packet.readString(istream, 255);
			data = dataRead(datatype, istream);
			
		} catch (IOException e){}				
	}

	//public static Packet250Metadata create(DataReq dataReq, ISerializable data){
	public static Packet250CustomPayload create(DataReq dataReq, ISerializable data){
		//Packet250Metadata packet      = new Packet250Metadata();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream      ostream = new DataOutputStream(bos);

		try{
			ostream.writeByte(Packets.DATA_VALUE_GENERAL);
			ostream.writeInt(dataReq.ordinal());
			Packet.writeString(data.getClass().getCanonicalName(), ostream);
			data.writeToStream(ostream);
			
		}catch(IOException e){}
		
		//packet.dataReq   = dataReq;
		//packet.dataValue = data;
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}	
	
	private ISerializable dataRead(String datatypeStr, DataInputStream istream){
		try{
			Class  datatype = Class.forName(datatypeStr);
			Method readFromStream = datatype.getMethod("readFromStream", DataInputStream.class);
			
			return (ISerializable)readFromStream.invoke(null, istream);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}	
	
}
