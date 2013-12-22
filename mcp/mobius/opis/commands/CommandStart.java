package mcp.mobius.opis.commands;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.EntityManager;
import mcp.mobius.opis.data.EntityProfiler;
import mcp.mobius.opis.data.TickHandlerManager;
import mcp.mobius.opis.data.TickHandlerProfiler;
import mcp.mobius.opis.data.TileEntityManager;
import mcp.mobius.opis.data.TileEntityProfiler;
import mcp.mobius.opis.server.OpisServerTickHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatMessageComponent;
import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;

public class CommandStart extends CommandBase implements IOpisCommand {

	@Override
	public String getCommandName() {
		return "opis_start";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		modOpis.profilerRun = true;

		OpisServerTickHandler.instance.profilerRunningTicks = 0;
		TileEntityManager.references.clear();
		TileEntityManager.stats.clear();
		EntityManager.stats.clear();
		TickHandlerManager.startStats.clear();
		TickHandlerManager.endStats.clear();		
		
		ProfilerRegistrar.registerProfilerTileEntity(new TileEntityProfiler());	
		ProfilerRegistrar.registerProfilerEntity(new EntityProfiler());		
		ProfilerRegistrar.registerProfilerTick(new TickHandlerProfiler());
		
		if (icommandsender instanceof EntityPlayer){
			OpisServerTickHandler.instance.players.add((EntityPlayer)icommandsender);
			PacketDispatcher.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromText(String.format("\u00A7oOpis started with a tick delay %s.", modOpis.profilerDelay))), (Player)icommandsender);
		}
		
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }	

	@Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
		if (sender instanceof DedicatedServer) return true;
		if (((EntityPlayerMP)sender).playerNetServerHandler.netManager instanceof MemoryConnection) return true;
        return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(((EntityPlayerMP)sender).username);
    }

	@Override
	public String getDescription() {
		return "Starts a run.";
	}	
	
}
