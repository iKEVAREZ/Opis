package mcp.mobius.opis.network.packets.client;

import java.util.ArrayList;

import mcp.mobius.opis.data.holders.DataType;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.ServerMessageHandler;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.enums.Packets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class PacketReqData extends PacketBase{

	public Message  dataReq;
	public ISerializable param1 = null;
	public ISerializable param2 = null;
	
    public PacketReqData(){}

    public PacketReqData(Message dataReq) {
    	this(dataReq, null, null);
    }	    
    
    public PacketReqData(Message dataReq, ISerializable param1) {
    	this(dataReq, param1, null);
    }	    
    
    public PacketReqData(Message dataReq, ISerializable param1, ISerializable param2) {
    	this.dataReq = dataReq;
    	this.param1  = param1;
    	this.param2  = param2;
    }	
	
	@Override
	public void encode(ByteArrayDataOutput output) {
		output.writeInt(dataReq.ordinal());
		
		if (param1 != null){
			output.writeBoolean(true);
			//Packet.writeString(param1.getClass().getCanonicalName(), outputStream);
			output.writeInt(DataType.getForClass(param1.getClass()).ordinal());
			param1.writeToStream(output);
		} else {
			output.writeBoolean(false);
		}
		
		if (param2 != null){
			output.writeBoolean(true);
			output.writeInt(DataType.getForClass(param2.getClass()).ordinal());				
			param2.writeToStream(output);
		} else {
			output.writeBoolean(false);
		}			
	}

	@Override
	public void decode(ByteArrayDataInput input) {
		this.dataReq   = Message.values()[input.readInt()];
		
		if (input.readBoolean()){
			//String datatype = Packet.readString(istream, 255);
			Class datatype = DataType.getForOrdinal(input.readInt());
			this.param1    = dataRead(datatype, input); 
		}
		
		if (input.readBoolean()){
			Class datatype = DataType.getForOrdinal(input.readInt());
			this.param2    = dataRead(datatype, input);
		}		
		
	}

	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		String logmsg = String.format("Received request %s from player %s ... ", this.dataReq, player.getDisplayName());
		
		if (this.dataReq.canPlayerUseCommand(player)){
			logmsg += "Accepted";
			ServerMessageHandler.instance().handle(this.dataReq, this.param1, this.param2,  player);
		} else {
			logmsg += "Rejected";
		}		
	}
}