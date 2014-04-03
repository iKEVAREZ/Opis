package mcp.mobius.opis.network.packets.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.enums.Packets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

public class NetDataList extends NetDataRaw{

	//public ArrayList<ISerializable> data = new ArrayList<ISerializable>(); 
	
	public NetDataList() {}
	
	public NetDataList(Packet250CustomPayload packet) {
		DataInputStream istream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header   = istream.readByte();
			this.msg  = Message.values()[istream.readInt()];
			int ndata     = istream.readInt();
			this.clazzStr = "";
			if (ndata > 0){
				this.clazzStr = Packet.readString(istream, 255);
				this.clazz    = this.getClass(this.clazzStr);
			}
			
			this.array = new ArrayList<ISerializable>();
			
			for (int i = 0; i < ndata; i++)
				this.array.add(dataRead(this.clazzStr, istream));
		} catch (IOException e){}				
	}

	/*
	public static Packet_DataList create(Message msg){
		ArrayList<ISerializable> list = new ArrayList<ISerializable>();
		return create(msg, list);
	}	
	
	public static Packet_DataList create(Message msg, ISerializable data){
		ArrayList<ISerializable> list = new ArrayList<ISerializable>();
		list.add(data);
		return create(msg, list);
	}
	*/
	
	//public static Packet250Metadata create(DataReq dataReq, ArrayList<? extends ISerializable> stats){
	public static NetDataList create(Message msg, List<? extends ISerializable> data){
		//Packet250Metadata packet      = new Packet250Metadata();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream ostream = new DataOutputStream(bos);

		try{
			ostream.writeByte(Packets.NETDATALIST);
			ostream.writeInt(msg.ordinal());
			ostream.writeInt(data.size());
			
			if (data.size() > 0)
				Packet.writeString(data.get(0).getClass().getCanonicalName(), ostream);
				
			for (ISerializable odata : data)
				odata.writeToStream(ostream);
		}catch(IOException e){}
		
		//packet.dataReq  = dataReq;
		//packet.dataList = stats;
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		NetDataList capsule = new NetDataList();
		capsule.msg = msg;
		capsule.header  = Packets.NETDATAVALUE;
		capsule.packet  = packet;
		
		return capsule;
	}	
	

	
}
