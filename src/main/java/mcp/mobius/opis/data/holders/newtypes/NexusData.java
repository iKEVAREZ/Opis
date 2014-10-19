package mcp.mobius.opis.data.holders.newtypes;

import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.SerialDouble;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.basetypes.SerialLong;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class NexusData  implements ISerializable {

	SerialLong packetOutbound;
	SerialLong packetInbound;
	SerialInt  chunkForced;
	SerialInt  chunkLoaded;
	DataTiming timingTick;
	SerialInt  amountTileEnts;
	SerialInt  amountEntities;
	SerialInt  amountPlayers;
	
		
	
	public NexusData(SerialLong Packet_Outbound, SerialLong Packet_Inbound, SerialInt Chunk_Forced, SerialInt Chunk_Loaded,
			         DataTiming Timing_Tick, SerialInt Amount_TileEnts, SerialInt Amount_Entities, SerialInt Amount_Players			
			){
		this.packetOutbound = Packet_Outbound;
		this.packetInbound  = Packet_Inbound;
		this.chunkForced    = Chunk_Forced;
		this.chunkLoaded    = Chunk_Loaded;
		this.timingTick     = Timing_Tick;
		this.amountTileEnts = Amount_TileEnts;
		this.amountEntities = Amount_Entities;
		this.amountPlayers  = Amount_Players;
	}
	
	
	@Override
	public void writeToStream(ByteArrayDataOutput stream){
		this.packetOutbound.writeToStream(stream);
		this.packetInbound.writeToStream(stream);
		this.chunkForced.writeToStream(stream);
		this.chunkLoaded.writeToStream(stream);
		this.timingTick.writeToStream(stream);
		this.amountTileEnts.writeToStream(stream);
		this.amountEntities.writeToStream(stream);
		this.amountPlayers.writeToStream(stream);
	}

	public static  NexusData readFromStream(ByteArrayDataInput stream){
		return new NexusData(
				SerialLong.readFromStream(stream),
				SerialLong.readFromStream(stream),
				SerialInt.readFromStream(stream),
				SerialInt.readFromStream(stream),
				DataTiming.readFromStream(stream),
				SerialInt.readFromStream(stream),
				SerialInt.readFromStream(stream),
				SerialInt.readFromStream(stream)
				);
	}	
}
