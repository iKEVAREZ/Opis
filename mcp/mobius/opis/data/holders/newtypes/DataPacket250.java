package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;
import mcp.mobius.opis.data.holders.ISerializable;
import net.minecraft.network.packet.Packet250CustomPayload;

public class DataPacket250 implements ISerializable{
	public String channel;
	public long   size;
	public long   amount;

	public DataPacket250(){	}	
	
	public DataPacket250(String channel){
		this.channel = channel;
	}
	
	public DataPacket250 fill(Packet packet){
		this.size   += packet.getPacketSize() + 1;
		this.amount += 1;
		return this;
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		Packet.writeString(this.channel, stream);
		stream.writeLong(this.size);
		stream.writeLong(this.amount);
	}

	public static DataPacket250 readFromStream(DataInputStream stream) throws IOException {
		DataPacket250 retVal = new DataPacket250();
		retVal.channel    = Packet.readString(stream, 255);
		retVal.size       = stream.readLong();
		retVal.amount     = stream.readLong();

		return retVal;
	}	
}