package mcp.mobius.opis.network.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.network.Packets;
import mcp.mobius.opis.network.enums.DataReq;

public class Packet_ReqData {
	
	public byte             header;
	public CoordinatesChunk coord;
	public DataReq maintype;
	public DataReq subtype;
	public DataReq target;
	
	public Packet_ReqData(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header    = inputStream.readByte();
			this.coord     = CoordinatesChunk.readFromStream(inputStream);
			this.maintype  = DataReq.values()[inputStream.readInt()];
			this.subtype   = DataReq.values()[inputStream.readInt()];
			this.target    = DataReq.values()[inputStream.readInt()];
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(DataReq maintype, DataReq subtype, DataReq target){
		return Packet_ReqData.create(new CoordinatesChunk(0, 0, 0), maintype, subtype, target) ;
	}		
	
	public static Packet250CustomPayload create(int dim, int x, int z, DataReq maintype, DataReq subtype, DataReq target){
		return Packet_ReqData.create(new CoordinatesChunk(dim, x, z), maintype, subtype, target);
	}	
	
	public static Packet250CustomPayload create(int dim, DataReq maintype, DataReq subtype, DataReq target){
		return Packet_ReqData.create(new CoordinatesChunk(dim, 0,0), maintype, subtype, target);
	}
	
	public static Packet250CustomPayload create(CoordinatesChunk coord, DataReq maintype, DataReq subtype, DataReq target){
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.REQ_DATA);
			coord.writeToStream(outputStream);
			outputStream.writeInt(maintype.ordinal());
			outputStream.writeInt(subtype.ordinal());
			outputStream.writeInt(target.ordinal());
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}	
}
