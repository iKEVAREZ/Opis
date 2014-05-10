package mcp.mobius.opis.network.packets.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import mcp.mobius.opis.data.holders.DataType;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.enums.Packets;

public class NetDataValue_OLD extends NetDataRaw_OLD{

	public NetDataValue_OLD(){};
	
	public NetDataValue_OLD(Packet250CustomPayload packet) {
		DataInputStream istream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header   = istream.readByte();
			this.msg      = Message.values()[istream.readInt()];
			//this.clazzStr = Packet.readString(istream, 255);
			//this.clazz    = this.getClass(this.clazzStr);
			this.clazz = DataType.getForOrdinal(istream.readInt());
			this.value = dataRead(this.clazz, istream);
			
		} catch (IOException e){}				
	}

	//public static Packet250Metadata create(DataReq dataReq, ISerializable data){
	public static NetDataValue_OLD create(Message dataReq, ISerializable data){
		//Packet250Metadata packet      = new Packet250Metadata();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream  bos     = new ByteArrayOutputStream(1);
		DataOutputStream       ostream = new DataOutputStream(bos);

		try{
			ostream.writeByte(Packets.NETDATAVALUE);
			ostream.writeInt(dataReq.ordinal());
			ostream.writeInt(DataType.getForClass(data.getClass()).ordinal());
			//Packet.writeString(data.getClass().getCanonicalName(), ostream);
			data.writeToStream(ostream);
			
		}catch(IOException e){}
		
		//packet.dataReq   = dataReq;
		//packet.dataValue = data;
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		NetDataValue_OLD capsule = new NetDataValue_OLD();
		capsule.msg = dataReq;
		capsule.header  = Packets.NETDATAVALUE;
		capsule.packet  = packet;
		
		return capsule;
	}	
}
