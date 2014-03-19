package mcp.mobius.opis.network.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import mcp.mobius.opis.data.holders.CoordinatesBlock;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.TargetEntity;
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
			
			if (istream.readBoolean()){
				String datatype = Packet.readString(istream, 255);
				this.param1    = readParam(this.maintype, this.subtype, this.target, datatype, istream);
			}
			
			if (istream.readBoolean()){
				String datatype = Packet.readString(istream, 255);
				this.param2    = readParam(this.maintype, this.subtype, this.target, datatype, istream);
			}
			
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(DataReq maintype, DataReq subtype, DataReq target){
		return Packet_ReqData.create(maintype, subtype, target, null, null) ;
	}		

	public static Packet250CustomPayload create(DataReq maintype, DataReq subtype, DataReq target, ISerializable param1){
		return Packet_ReqData.create(maintype, subtype, target, param1, null) ;
	}			
	
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
				Packet.writeString(param1.getClass().getCanonicalName(), outputStream);
				param1.writeToStream(outputStream);
			} else {
				outputStream.writeBoolean(false);
			}
			
			if (param2 != null){
				outputStream.writeBoolean(true);
				Packet.writeString(param2.getClass().getCanonicalName(), outputStream);				
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
	
	private ISerializable readParam(DataReq maintype, DataReq subtype, DataReq target, String datatypeStr, DataInputStream istream){
		try{
			Class  datatype = Class.forName(datatypeStr);
			Method readFromStream = datatype.getMethod("readFromStream", DataInputStream.class);
			
			return (ISerializable)readFromStream.invoke(null, istream);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
}
