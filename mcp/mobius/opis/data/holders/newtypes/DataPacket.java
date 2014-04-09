package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.ISerializable;
import net.minecraft.network.packet.Packet;

public class DataPacket implements ISerializable{
	public int  id;
	public long size;
	public long amount;
	public String type;
	
	public DataPacket(){
	}	
	
	public DataPacket(int id){
		this.id   = id;
		try {
			this.type = ((Class)(Packet.packetIdToClassMap.lookup(this.id))).getName();
		} catch (Exception e) {
			this.type = "<UNUSED>";
		}
	}
	
	public DataPacket fill(Packet packet){
		this.size   += packet.getPacketSize() + 1;
		this.amount += 1;
		return this;
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeInt(this.id);
		stream.writeLong(this.size);
		stream.writeLong(this.amount);
	}

	public static DataPacket readFromStream(DataInputStream stream) throws IOException {
		DataPacket retVal = new DataPacket();
		retVal.id         = stream.readInt();
		retVal.size       = stream.readLong();
		retVal.amount     = stream.readLong();
		try {
			retVal.type = ((Class)(Packet.packetIdToClassMap.lookup(retVal.id))).getName();
		} catch (Exception e) {
			retVal.type = "<UNUSED>";
		}
		return retVal;
	}	
}
