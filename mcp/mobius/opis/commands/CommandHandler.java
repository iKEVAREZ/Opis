package mcp.mobius.opis.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import mcp.mobius.opis.data.TickHandlerManager;
import mcp.mobius.opis.data.holders.TickHandlerStats;
import mcp.mobius.opis.network.server.Packet_DataScreenTimingHandlers;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

public class CommandHandler extends CommandBase  implements IOpisCommand {

	@Override
	public String getCommandName() {
		return "opis_handler";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		ArrayList<TickHandlerStats> stats = TickHandlerManager.getCumulatedStats();
		((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(Packet_DataScreenTimingHandlers.create(stats));		
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }	

	@Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
		if (sender instanceof DedicatedServer) return false;
		if (((EntityPlayerMP)sender).playerNetServerHandler.netManager instanceof MemoryConnection) return true;		
        return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(((EntityPlayerMP)sender).username);
    }

	@Override
	public String getDescription() {
		return "Opens timing data for server side tick handlers.";
	}

}
