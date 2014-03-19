package mcp.mobius.opis.network.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.network.Packets;
import mcp.mobius.opis.network.enums.DataReq;

public class Packet_ReqData {
	
	public byte     header;
	public DataReq  maintype;
	public DataReq  subtype;
	public DataReq  target;
	public ISerializable param1 = null;
	public ISerializable param2 = null;
	
	public Packet_ReqData(Packet250CustomPayload packet) {
		DataInputStream istream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header    = istream.readByte();
			this.maintype  = DataReq.values()[istream.readInt()];
			this.subtype   = DataReq.values()[istream.readInt()];
			this.target    = DataReq.values()[istream.readInt()];
			
			if (istream.readBoolean())
				this.param1    = readParam1(this.maintype, this.subtype, this.target, istream);
			
			if (istream.readBoolean())
				this.param2    = readParam2(this.maintype, this.subtype, this.target, istream);
			
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(DataReq maintype, DataReq subtype, DataReq target){
		return Packet_ReqData.create(maintype, subtype, target, null, null) ;
	}		

	public static Packet250CustomPayload create(DataReq maintype, DataReq subtype, DataReq target, ISerializable param1){
		return Packet_ReqData.create(maintype, subtype, target, param1, null) ;
	}			
	
	/*
	public static Packet250CustomPayload create(int dim, int x, int z, DataReq maintype, DataReq subtype, DataReq target){
		return Packet_ReqData.create(new CoordinatesChunk(dim, x, z), maintype, subtype, target);
	}
	*/	
	
	/*
	public static Packet250CustomPayload create(int dim, DataReq maintype, DataReq subtype, DataReq target){
		return Packet_ReqData.create(new CoordinatesChunk(dim, 0,0), maintype, subtype, target);
	}
	*/
	
	public static Packet250CustomPayload create(DataReq maintype, DataReq subtype, DataReq target, ISerializable param1, ISerializable param2){
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.REQ_DATA);
			outputStream.writeInt(maintype.ordinal());
			outputStream.writeInt(subtype.ordinal());
			outputStream.writeInt(target.ordinal());
			
			if (param1 != null){
				outputStream.writeBoolean(true);
				param1.writeToStream(outputStream);
			} else {
				outputStream.writeBoolean(false);
			}
			
			if (param2 != null){
				outputStream.writeBoolean(true);
				param2.writeToStream(outputStream);
			} else {
				outputStream.writeBoolean(false);
			}			
			
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}
	
	private ISerializable readParam1(DataReq maintype, DataReq subtype, DataReq target, DataInputStream istream){
		try{
			if ((maintype == DataReq.OVERLAY) && (subtype == DataReq.CHUNK) && (target == DataReq.ENTITIES))
				return CoordinatesChunk.readFromStream(istream);

			if ((maintype == DataReq.LIST)    && (subtype == DataReq.CHUNK) && (target == DataReq.ENTITIES))
				return CoordinatesChunk.readFromStream(istream);			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		throw new RuntimeException(String.format("Unknown datatype for %s / %s / %s", maintype, subtype, target));
	}
	
	private ISerializable readParam2(DataReq maintype, DataReq subtype, DataReq target, DataInputStream istream){
		try{
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		throw new RuntimeException(String.format("Unknown datatype for %s / %s / %s", maintype, subtype, target));
	}	
}
