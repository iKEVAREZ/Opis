package mcp.mobius.opis.commands;

import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.mobiuscore.profiler.DummyProfiler;
import mcp.mobius.opis.modOpis;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatMessageComponent;
import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;

public class CommandStop extends CommandBase {

	@Override
	public String getCommandName() {
		return "opis_stop";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		modOpis.profilerRun = false;
		
		ProfilerRegistrar.registerProfilerTileEntity(new DummyProfiler());	
		ProfilerRegistrar.registerProfilerEntity(new DummyProfiler());			
		
		PacketDispatcher.sendPacketToAllPlayers(new Packet3Chat(ChatMessageComponent.createFromText(String.format("\u00A7oOpis stopped."))));
		//notifyAdmins(icommandsender, "Opis stopped.", new Object[] {});		
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
