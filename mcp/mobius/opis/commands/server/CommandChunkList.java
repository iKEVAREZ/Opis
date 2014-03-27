package mcp.mobius.opis.commands.server;

import java.util.ArrayList;

import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.OpisPacketHandler;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataList;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatMessageComponent;

public class CommandChunkList extends CommandBase implements IOpisCommand {

	@Override
	public String getCommandName() {
		return "opis_chunk";
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
		
		ArrayList<StatsChunk> chunks = new ArrayList<StatsChunk>();
		
		if (astring.length == 0)
			chunks = ChunkManager.instance().getTopChunks(20);
		else
			try{
				chunks = ChunkManager.instance().getTopChunks(Integer.valueOf(astring.length));	
			}catch (Exception e){return;}
		
		icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("[DIM X Z] Time NTEs"));
		for (StatsChunk stat : chunks){
			icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(stat.toString()));
		}

		
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
		return "Shows the 20 slowest chunks, in respect to tile entities.";
	}	

}
