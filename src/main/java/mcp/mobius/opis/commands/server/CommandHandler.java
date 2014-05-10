package mcp.mobius.opis.commands.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.newtypes.DataHandler;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.OpisPacketHandler_OLD;
import mcp.mobius.opis.network.enums.Message;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatComponentText;

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
		if (icommandsender instanceof EntityPlayerMP){
			icommandsender.addChatMessage(new ChatComponentText("DEPRECATED ! Please run /opis instead."));
			return;
		}				
		
		//ArrayList<DataHandler> stats = TickHandlerManager.getCumulatedStatsServer();
		
		//for (DataHandler s : stats)
		//	icommandsender.addChatMessage(new ChatComponentText(String.format("%s : %.2f", s.name, s.update.toString())));
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
