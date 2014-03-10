package mcp.mobius.opis.commands.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.TickHandlerStats;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.network.server.Packet_DataScreenTimingHandlers;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatMessageComponent;

public class CommandHandler extends CommandBase  implements IOpisCommand {

	@Override
	public String getCommandName() {
		return "opis_handler";
	}

	@Override
	public String getCommandNameOpis() {
		return this.getCommandName();
	}	
	
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		ArrayList<TickHandlerStats> stats = TickHandlerManager.getCumulatedStats();
		//((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(Packet_DataScreenTimingHandlers.create(stats));
		
		if (icommandsender instanceof EntityPlayerMP)
			((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(Packet_DataScreenTimingHandlers.create(stats));
		else{
			for (TickHandlerStats s : stats)
				icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("%s : %.2f", s.getName(), s.getGeometricMean())));
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
		if ((sender instanceof EntityPlayerMP) && ((EntityPlayerMP)sender).playerNetServerHandler.netManager instanceof MemoryConnection) return true;
		if (!(sender instanceof DedicatedServer) && !(sender instanceof EntityPlayerMP)) return true;
        return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(((EntityPlayerMP)sender).username);
    }

	@Override
	public String getDescription() {
		return "Opens timing data for server side tick handlers.";
	}

}
