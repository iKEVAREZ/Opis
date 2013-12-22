package mcp.mobius.opis.commands;

import java.util.HashMap;

import mcp.mobius.opis.data.TickHandlerManager;
import mcp.mobius.opis.data.holders.TickHandlerStats;
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
		HashMap<String, TickHandlerStats> startStats = TickHandlerManager.startStats;
		HashMap<String, TickHandlerStats> endStats   = TickHandlerManager.endStats;
		
		System.out.printf("== TICK START ==\n");
		for (String stats : startStats.keySet())
			System.out.printf("%s\n", startStats.get(stats));
		
		System.out.printf("== TICK END ==\n");
		for (String stats : endStats.keySet())
			System.out.printf("%s\n", endStats.get(stats));		
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
