package mcp.mobius.opis.network.packets.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
import mcp.mobius.opis.data.holders.stats.StatsTickHandler;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.enums.Packets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet_DataList extends Packet_DataAbstract{

	public ArrayList<ISerializable> data = new ArrayList<ISerializable>(); 
	
	public Packet_DataList() {}
	
	public Packet_DataList(Packet250CustomPayload packet) {
		DataInputStream istream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header   = istream.readByte();
			this.msg  = Message.values()[istream.readInt()];
			int ndata     = istream.readInt();
			String datatype = "";
			if (ndata > 0)
				datatype = Packet.readString(istream, 255);
			
			for (int i = 0; i < ndata; i++)
				data.add(dataRead(datatype, istream));
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
	public static Packet_DataList create(Message msg, ArrayList<? extends ISerializable> data){
		//Packet250Metadata packet      = new Packet250Metadata();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream ostream = new DataOutputStream(bos);

		try{
			ostream.writeByte(Packets.DATA_LIST_GENERAL);
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
		
		Packet_DataList capsule = new Packet_DataList();
		capsule.msg = msg;
		capsule.header  = Packets.DATA_VALUE_GENERAL;
		capsule.packet  = packet;
		
		return capsule;
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
