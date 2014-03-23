package mcp.mobius.opis.commands.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.stats.StatsTickHandler;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.OpisPacketHandler;
import mcp.mobius.opis.network.enums.DataReq;
import mcp.mobius.opis.network.server.Packet_DataList;
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
		if (icommandsender instanceof Player){
			icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("DEPRECATED ! Please run /opis instead."));
			return;
		}				
		
		ArrayList<StatsTickHandler> stats = TickHandlerManager.getCumulatedStats();
		
		for (StatsTickHandler s : stats)
			icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("%s : %.2f", s.getName(), s.getGeometricMean())));
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }	

	@Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
		return true;
    }

	@Override
	public String getDescription() {
		return "Opens timing data for server side tick handlers.";
	}

}
