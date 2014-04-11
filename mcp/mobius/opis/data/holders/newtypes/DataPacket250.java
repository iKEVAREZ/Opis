package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;
import mcp.mobius.opis.data.holders.ISerializable;
import net.minecraft.network.packet.Packet250CustomPayload;

public class DataPacket250 implements ISerializable{
	public CachedString   channel;
	public DataByteSize   size;
	public DataByteRate   rate;
	public DataAmountRate amount;

	public DataPacket250(){	}	
	
	public DataPacket250(String channel){
		this.channel = new CachedString(channel);
		this.size    = new DataByteSize(0);
		this.rate    = new DataByteRate(0, 5);
		this.amount  = new DataAmountRate(0, 5);
	}
	
	public DataPacket250 fill(Packet packet){
		this.size.size += packet.getPacketSize() + 1;
		this.rate.size += packet.getPacketSize() + 1;
		this.amount.size += 1;
		return this;
	}
	
	public void startInterval(){
		this.rate.reset();
		this.amount.reset();
	}	
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		this.channel.writeToStream(stream);
		this.size.writeToStream(stream);
		this.rate.writeToStream(stream);
		this.amount.writeToStream(stream);
	}

	public static DataPacket250 readFromStream(DataInputStream stream) throws IOException {
		DataPacket250 retVal = new DataPacket250();
		retVal.channel    = CachedString.readFromStream(stream);
		retVal.size       = DataByteSize.readFromStream(stream);
		retVal.rate       = DataByteRate.readFromStream(stream);
		retVal.amount     = DataAmountRate.readFromStream(stream);

		return retVal;
	}	
}