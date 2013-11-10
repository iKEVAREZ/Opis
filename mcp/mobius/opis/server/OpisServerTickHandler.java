package mcp.mobius.opis.server;

import java.util.EnumSet;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.ChunkManager;
import mcp.mobius.opis.data.TileEntityManager;
import mcp.mobius.opis.network.server.Packet_LoadedChunks;
import mcp.mobius.opis.network.server.Packet_MeanTime;
import mcp.mobius.opis.overlay.OverlayStatus;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class OpisServerTickHandler implements ITickHandler {

	public static long profilerUpdateTickCounter = 0;	
	public static long clientUpdateTickCounter = 0;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(type.contains(TickType.WORLD)){
			clientUpdateTickCounter++;
			if (clientUpdateTickCounter % 100 == 0){
				updatePlayers();
				clientUpdateTickCounter = 0;
			}
			
			profilerUpdateTickCounter++;
		}		
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel() {
		return "opis.server.tickhandler";
	}

	private void updatePlayers(){
		for (Player player : modOpis.proxy.playerOverlayStatus.keySet()){
			if (modOpis.proxy.playerOverlayStatus.get(player) == OverlayStatus.CHUNKSTATUS)
				PacketDispatcher.sendPacketToPlayer( Packet_LoadedChunks.create(ChunkManager.getLoadedChunks(modOpis.proxy.playerDimension.get(player))), player);
			if (modOpis.proxy.playerOverlayStatus.get(player) == OverlayStatus.MEANTIME)
				PacketDispatcher.sendPacketToPlayer( Packet_MeanTime.create(TileEntityManager.getTimes(modOpis.proxy.playerDimension.get(player)), modOpis.proxy.playerDimension.get(player)), player);
		}
	}
	
}
