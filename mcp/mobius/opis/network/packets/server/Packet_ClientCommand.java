package mcp.mobius.opis.network.packets.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.network.enums.ClientCommand;
import mcp.mobius.opis.network.enums.Packets;
import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet_ClientCommand {

	public byte          header;
	public ClientCommand cmd;
	
	public Packet_ClientCommand(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		try{
			this.header  = inputStream.readByte();
			this.cmd     = ClientCommand.values()[inputStream.readInt()];
		} catch (IOException e){}			
		
	}

	public static Packet250CustomPayload create(ClientCommand cmd){
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.CLIENT_CMD);
			outputStream.writeInt(cmd.ordinal());
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}	
	
}
