package mcp.mobius.opis.network.json;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.network.packet.Packet250CustomPayload;

public class CommandPacket {

	public static CommandPayload getCommand(Packet250CustomPayload packet) {
		DataInputStream istream = new DataInputStream(new ByteArrayInputStream(packet.data));
		CommandPayload  payload = CommandPayload.readFromStream(istream);
		
		return payload;
	}

	private static Packet250CustomPayload create(CommandPayload payload){
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream ostream      = new DataOutputStream(bos);

		payload.writeToStream(ostream);
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}
	
	public static void sendCommand(OpisCommand command){
		CommandPacket.sendCommand(command, null);
	}
	
	public static void sendCommand(OpisCommand command, Object[] params){
		Packet250CustomPayload packet = null;
		
		if (params == null)
			packet = CommandPacket.create(new CommandPayload(command));
		else
			packet = CommandPacket.create(new CommandPayload(command, params));
		
		PacketDispatcher.sendPacketToServer(packet);
	}
	
}
