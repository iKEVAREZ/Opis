package mcp.mobius.opis.server;

import java.util.EnumSet;
import java.util.logging.Level;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.constants.OverlayStatus;
import mcp.mobius.opis.data.ChunkData;
import mcp.mobius.opis.network.Packet_LoadedChunks;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class OpisServerTickHandler implements ITickHandler {

	int tickCounter = 0;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(type.contains(TickType.WORLD)){
			tickCounter += 1;
			if (tickCounter % 100 == 0){
				updatePlayers();
				tickCounter = 0;
			}
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
		for (Player player : modOpis.proxy.playerOverlayStatus.keySet())
			if (modOpis.proxy.playerOverlayStatus.get(player) == OverlayStatus.CHUNKSTATUS)
				PacketDispatcher.sendPacketToPlayer( Packet_LoadedChunks.create(ChunkData.getLoadedChunks(modOpis.proxy.playerDimension.get(player))), player);
	}
	
}
