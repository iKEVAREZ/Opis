package mcp.mobius.opis.commands;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.mobiuscore.profiler.DummyProfiler;
import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.EntityManager;
import mcp.mobius.opis.data.TileEntityManager;
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

public class CommandReset extends CommandBase {

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
		ProfilerRegistrar.registerProfilerTileEntity(new DummyProfiler());	
		ProfilerRegistrar.registerProfilerEntity(    new DummyProfiler());		
		
		if (icommandsender instanceof EntityPlayer)
			PacketDispatcher.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromText(String.format("\u00A7oInternal data reseted."))), (Player)icommandsender);
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

}
