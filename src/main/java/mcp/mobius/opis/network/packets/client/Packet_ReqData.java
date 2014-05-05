package mcp.mobius.opis.network.packets.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import mcp.mobius.opis.data.holders.DataType;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.basetypes.TargetEntity;
import mcp.mobius.opis.data.holders.newtypes.DataError;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.enums.Packets;

public class Packet_ReqData {
	
	public byte     header;
	public Message  dataReq;
	public ISerializable param1 = null;
	public ISerializable param2 = null;
	
	public Packet_ReqData(Packet250CustomPayload packet) {
		DataInputStream istream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try{
			this.header    = istream.readByte();
			this.dataReq   = Message.values()[istream.readInt()];
			
			if (istream.readBoolean()){
				//String datatype = Packet.readString(istream, 255);
				Class datatype = DataType.getForOrdinal(istream.readInt());
				this.param1    = dataRead(datatype, istream);
			}
			
			if (istream.readBoolean()){
				Class datatype = DataType.getForOrdinal(istream.readInt());
				this.param2    = dataRead(datatype, istream);
			}
			
		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(Message dataReq){
		return Packet_ReqData.create(dataReq, null, null) ;
	}		

	public static Packet250CustomPayload create(Message dataReq, ISerializable param1){
		return Packet_ReqData.create(dataReq, param1, null) ;
	}			
	
	public static Packet250CustomPayload create(Message dataReq, ISerializable param1, ISerializable param2){
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.REQ_DATA);
			outputStream.writeInt(dataReq.ordinal());
			
			if (param1 != null){
				outputStream.writeBoolean(true);
				//Packet.writeString(param1.getClass().getCanonicalName(), outputStream);
				outputStream.writeInt(DataType.getForClass(param1.getClass()).ordinal());
				param1.writeToStream(outputStream);
			} else {
				outputStream.writeBoolean(false);
			}
			
			if (param2 != null){
				outputStream.writeBoolean(true);
				outputStream.writeInt(DataType.getForClass(param2.getClass()).ordinal());				
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
	
	private ISerializable dataRead(Class datatype, DataInputStream istream){
		if (datatype == null) return new DataError();
		
		try{
			Method readFromStream = datatype.getMethod("readFromStream", DataInputStream.class);
			
			return (ISerializable)readFromStream.invoke(null, istream);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
}
