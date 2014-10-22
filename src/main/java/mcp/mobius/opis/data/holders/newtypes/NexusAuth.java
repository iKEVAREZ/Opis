package mcp.mobius.opis.data.holders.newtypes;

import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.basetypes.SerialLong;
import mcp.mobius.opis.data.holders.basetypes.SerialString;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class NexusAuth implements ISerializable {

	SerialString uuid;
	SerialString passphrase;
	boolean      reconnect;
	
	public NexusAuth(String uuid, String passphrase, boolean reconnect){
		this.uuid        = new SerialString(uuid);
		this.passphrase  = new SerialString(passphrase);
		this.reconnect   = reconnect;
	}
	
	public NexusAuth(SerialString uuid, SerialString passphrase, boolean reconnect){
		this.uuid        = uuid;
		this.passphrase  = passphrase;
		this.reconnect   = reconnect;		
	}
	
	
	@Override
	public void writeToStream(ByteArrayDataOutput stream){
		this.uuid.writeToStream(stream);
		this.passphrase.writeToStream(stream);
		stream.writeBoolean(reconnect);
	}
 
	public static  NexusAuth readFromStream(ByteArrayDataInput stream){
		return new NexusAuth(
				SerialString.readFromStream(stream),
				SerialString.readFromStream(stream),
				stream.readBoolean()
				);
	}	
}