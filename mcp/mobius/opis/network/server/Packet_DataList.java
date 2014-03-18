package mcp.mobius.opis.network.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
			for (int i = 0; i < ndata; i++)
				data.add(dataRead(this.maintype, this.subtype, this.target, istream));
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(DataReq maintype, DataReq subtype, DataReq target, ArrayList<? extends ISerializable> stats){
		Packet250CustomPayload packet      = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.DATA_LIST_GENERAL);
			outputStream.writeInt(maintype.ordinal());
			outputStream.writeInt(subtype.ordinal());
			outputStream.writeInt(target.ordinal());			
			outputStream.writeInt(stats.size());
			for (ISerializable data : stats)
				data.writeToStream(outputStream);
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}	
	
	private ISerializable dataRead(DataReq maintype, DataReq subtype, DataReq target, DataInputStream istream){
		try{
			if ((maintype == DataReq.LIST) && (subtype == DataReq.TIMING) && (target == DataReq.TILETENTS))
				return TileEntityStats.readFromStream(istream);
			
			if ((maintype == DataReq.LIST) && (subtype == DataReq.TIMING) && (target == DataReq.ENTITIES))
				return EntityStats.readFromStream(istream);
			
			if ((maintype == DataReq.LIST) && (subtype == DataReq.TIMING) && (target == DataReq.HANDLERS))
				return TickHandlerStats.readFromStream(istream);			

			if ((maintype == DataReq.LIST) && (subtype == DataReq.TIMING) && (target == DataReq.CHUNK))
				return ChunkStats.readFromStream(istream);			

			if ((maintype == DataReq.LIST) && (subtype == DataReq.AMOUNT) && (target == DataReq.ENTITIES))
				return AmountHolder.readFromStream(istream);				
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		throw new RuntimeException(String.format("Unknown datatype for %s / %s / %s", maintype, subtype, target));
	}
	
}
