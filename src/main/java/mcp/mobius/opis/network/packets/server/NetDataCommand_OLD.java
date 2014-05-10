package mcp.mobius.opis.network.packets.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.enums.Packets;

public class NetDataCommand_OLD extends NetDataRaw_OLD{

	public NetDataCommand_OLD(){};
	
	public NetDataCommand_OLD(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		try{
			this.header  = inputStream.readByte();
			this.msg     = Message.values()[inputStream.readInt()];
		} catch (IOException e){}			
		
	}

	public static NetDataCommand_OLD create(Message msg){
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.NETDATACOMMAND);
			outputStream.writeInt(msg.ordinal());
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		NetDataCommand_OLD capsule = new NetDataCommand_OLD();
		capsule.msg     = msg;
		capsule.header  = Packets.NETDATACOMMAND;
		capsule.packet  = packet;
		
		return capsule;		
	}	
	
}
