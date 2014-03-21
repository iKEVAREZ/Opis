package mcp.mobius.opis.network.server;

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
import mcp.mobius.opis.network.Packets;
import mcp.mobius.opis.network.enums.DataReq;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet_DataList {

	public byte header;
	public DataReq dataReq;
	public ArrayList<ISerializable> data = new ArrayList<ISerializable>(); 
	
	public Packet_DataList(Packet250CustomPayload packet) {
		DataInputStream istream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header   = istream.readByte();
			this.dataReq  = DataReq.values()[istream.readInt()];
			int ndata     = istream.readInt();
			String datatype = "";
			if (ndata > 0)
				datatype = Packet.readString(istream, 255);
			
			for (int i = 0; i < ndata; i++)
				data.add(dataRead(datatype, istream));
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(DataReq dataReq, ArrayList<? extends ISerializable> stats){
		Packet250CustomPayload packet      = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream ostream = new DataOutputStream(bos);

		try{
			ostream.writeByte(Packets.DATA_LIST_GENERAL);
			ostream.writeInt(dataReq.ordinal());
			ostream.writeInt(stats.size());
			
			if (stats.size() > 0)
				Packet.writeString(stats.get(0).getClass().getCanonicalName(), ostream);
				
			for (ISerializable data : stats)
				data.writeToStream(ostream);
		}catch(IOException e){}
		
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
