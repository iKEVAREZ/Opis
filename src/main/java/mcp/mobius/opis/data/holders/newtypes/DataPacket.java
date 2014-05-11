package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.Packet;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import mcp.mobius.opis.data.holders.ISerializable;

public class DataPacket implements ISerializable{
	public int  id;
	public DataByteSize size;
	public DataByteRate rate;
	public DataAmountRate amount;
	public String type;
	
	public DataPacket(){
	}	
	
	public DataPacket(int id){
		this.id   = id;
		this.size = new DataByteSize(0);
		this.rate = new DataByteRate(0, 5);
		this.amount = new DataAmountRate(0, 5);
		//try {
		//	this.type = ((Class)(Packet.packetIdToClassMap.lookup(this.id))).getSimpleName();
		//} catch (Exception e) {
			this.type = "<UNUSED>";
		//}
	}

	public DataPacket fill(Packet packet){
		//this.size.size += packet.getPacketSize() + 1;
		//this.rate.size += packet.getPacketSize() + 1;
		//this.amount.size += 1;
		return this;
	}
	
	public void startInterval(){
		this.rate.reset();
		this.amount.reset();
	}
	
	@Override
	public void writeToStream(ByteArrayDataOutput stream){
		stream.writeInt(this.id);
		this.size.writeToStream(stream);
		this.rate.writeToStream(stream);
		this.amount.writeToStream(stream);
	}

	public static DataPacket readFromStream(ByteArrayDataInput stream){
		DataPacket retVal = new DataPacket();
		retVal.id         = stream.readInt();
		retVal.size       = DataByteSize.readFromStream(stream);
		retVal.rate       = DataByteRate.readFromStream(stream);
		retVal.amount     = DataAmountRate.readFromStream(stream);
		//try {
		//	retVal.type = ((Class)(Packet.packetIdToClassMap.lookup(retVal.id))).getSimpleName();
		//} catch (Exception e) {
			retVal.type = "<UNUSED>";
		//}

		return retVal;
	}	
}
