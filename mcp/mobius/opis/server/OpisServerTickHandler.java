package mcp.mobius.opis.server;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.util.ChatMessageComponent;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.network.server.Packet_LoadedChunks;
import mcp.mobius.opis.network.server.Packet_MeanTime;
import mcp.mobius.opis.overlay.OverlayStatus;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.mobiuscore.profiler.DummyProfiler;
import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;

public class OpisServerTickHandler implements ITickHandler {

	public long profilerUpdateTickCounter = 0;	
	public long clientUpdateTickCounter = 0;
	public long profilerRunningTicks;
	public HashSet<EntityPlayer> players = new HashSet<EntityPlayer>();
	
	
	public static OpisServerTickHandler instance;
	
	public OpisServerTickHandler(){
		instance = this;
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(type.contains(TickType.SERVER)){
			clientUpdateTickCounter++;
			if (clientUpdateTickCounter % 100 == 0){
				updatePlayers();
				clientUpdateTickCounter = 0;
			}
			
			profilerUpdateTickCounter++;
			
			if (profilerRunningTicks < modOpis.profilerMaxTicks && modOpis.profilerRun)
				profilerRunningTicks++;
			else if (profilerRunningTicks >= modOpis.profilerMaxTicks && modOpis.profilerRun){
				profilerRunningTicks = 0;
				modOpis.profilerRun = false;
				ProfilerRegistrar.registerProfilerTileEntity(new DummyProfiler());	
				ProfilerRegistrar.registerProfilerEntity(new DummyProfiler());
				ProfilerRegistrar.registerProfilerTick(new DummyProfiler());				
				
				for (EntityPlayer player : players)
					PacketDispatcher.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromText(String.format("\u00A7oOpis automaticly stopped after %d ticks.", modOpis.profilerMaxTicks))), (Player)player);
				
				players.clear();
			}			
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER);
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
