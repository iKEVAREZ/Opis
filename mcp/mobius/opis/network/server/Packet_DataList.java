package mcp.mobius.opis.network.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import mcp.mobius.opis.data.holders.AmountHolder;
import mcp.mobius.opis.data.holders.ChunkStats;
import mcp.mobius.opis.data.holders.EntityStats;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.StatAbstract;
import mcp.mobius.opis.data.holders.TickHandlerStats;
import mcp.mobius.opis.data.holders.TileEntityStats;
import mcp.mobius.opis.network.Packets;
import mcp.mobius.opis.network.enums.DataReq;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet_DataList {

	public byte header;
	public DataReq maintype;
	public DataReq subtype;
	public DataReq target;	
	public ArrayList<ISerializable> data = new ArrayList<ISerializable>(); 
	
	public Packet_DataList(Packet250CustomPayload packet) {
		DataInputStream istream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header    = istream.readByte();
			this.maintype  = DataReq.values()[istream.readInt()];
			this.subtype   = DataReq.values()[istream.readInt()];
			this.target    = DataReq.values()[istream.readInt()];			
			int ndata      = istream.readInt();
			String datatype = "";
			if (ndata > 0)
				datatype = Packet.readString(istream, 255);
			
			for (int i = 0; i < ndata; i++)
				data.add(dataRead(this.maintype, this.subtype, this.target, datatype, istream));
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(DataReq maintype, DataReq subtype, DataReq target, ArrayList<? extends ISerializable> stats){
		Packet250CustomPayload packet      = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream ostream = new DataOutputStream(bos);

		try{
			ostream.writeByte(Packets.DATA_LIST_GENERAL);
			ostream.writeInt(maintype.ordinal());
			ostream.writeInt(subtype.ordinal());
			ostream.writeInt(target.ordinal());
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
	
	private ISerializable dataRead(DataReq maintype, DataReq subtype, DataReq target, String datatypeStr, DataInputStream istream){
		try{
			Class  datatype = Class.forName(datatypeStr);
			Method readFromStream = datatype.getMethod("readFromStream", DataInputStream.class);
			
			return (ISerializable)readFromStream.invoke(null, istream);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
