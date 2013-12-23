package mcp.mobius.opis.commands.server;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.mobiuscore.profiler.DummyProfiler;
import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.server.EntityManager;
import mcp.mobius.opis.data.server.TickHandlerManager;
import mcp.mobius.opis.data.server.TileEntityManager;
import mcp.mobius.opis.network.server.Packet_ClearSelection;
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

public class CommandReset extends CommandBase implements IOpisCommand {

	@Override
	public String getCommandName() {
		return "opis_reset";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		modOpis.profilerRun = false;

		modOpis.selectedBlock = null;
		OpisServerTickHandler.instance.profilerRunningTicks = 0;
		TileEntityManager.references.clear();
		TileEntityManager.stats.clear();
		EntityManager.stats.clear();
		TickHandlerManager.startStats.clear();
		TickHandlerManager.endStats.clear();			
		ProfilerRegistrar.registerProfilerTileEntity(new DummyProfiler());	
		ProfilerRegistrar.registerProfilerEntity(    new DummyProfiler());	
		ProfilerRegistrar.registerProfilerTick(new DummyProfiler());		
		
		if (icommandsender instanceof EntityPlayer)
			PacketDispatcher.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromText(String.format("\u00A7oInternal data reseted."))), (Player)icommandsender);
		
		PacketDispatcher.sendPacketToAllPlayers(Packet_ClearSelection.create());
		//notifyAdmins(icommandsender, "Opis started with a tick delay %s.", new Object[] {modOpis.profilerDelay});		
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
		return "Cleans up all profiling data and remove client block overlay.";
	}	

}
