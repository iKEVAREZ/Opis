package mcp.mobius.opis.network.packets.server;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.gui.overlay.OverlayLoadedChunks;
import mcp.mobius.opis.gui.overlay.entperchunk.OverlayEntityPerChunk;
import mcp.mobius.opis.network.PacketBase;

public class PacketDataOverlayChunkEntities extends PacketBase {

	public HashMap<CoordinatesChunk, Integer> chunkStatus = new HashMap<CoordinatesChunk, Integer>();	
	
	public PacketDataOverlayChunkEntities(){}
	public PacketDataOverlayChunkEntities(HashMap<CoordinatesChunk, Integer> chunks){
		this.chunkStatus = chunks;
	}
	
	@Override
	public void encode(ByteArrayDataOutput output) {
		/*
		output.writeInt(chunkStatus.size());
		for (CoordinatesChunk chunk : chunkStatus.keySet()){
			chunk.writeToStream(output);
			output.writeInt(chunkStatus.get(chunk));
		}
		*/
	}

	@Override
	public void decode(ByteArrayDataInput input) {
		/*
		int nchunks  = input.readInt();
		
		for (int i = 0; i <= nchunks; i++){
			CoordinatesChunk chunk = CoordinatesChunk.readFromStream(input);
			this.chunkStatus.put(chunk, input.readInt());
		}
		*/
	}

    @SideOnly(Side.CLIENT)
    public void actionClient(World world, EntityPlayer player){
		OverlayEntityPerChunk.INSTANCE.overlayData = this.chunkStatus;
		OverlayEntityPerChunk.INSTANCE.reduceData();
		OverlayEntityPerChunk.INSTANCE.setupChunkTable();
    }	
	
}
