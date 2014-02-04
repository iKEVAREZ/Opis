package mcp.mobius.opis.commands.server;

import java.util.ArrayList;

import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.ChunkStats;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.network.server.Packet_ChunkTopList;
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
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		ArrayList<ChunkStats> chunks = new ArrayList<ChunkStats>();
		
		if (astring.length == 0)
			chunks = ChunkManager.getTopChunks(20);
		else
			try{
				chunks = ChunkManager.getTopChunks(Integer.valueOf(astring.length));	
			}catch (Exception e){return;}
		
		/*
		System.out.printf("== ==\n");
		for (ChunkStats stat : chunks){
			System.out.printf("%s\n", stat);
		}
		*/
		if (icommandsender instanceof EntityPlayer)		
			((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(Packet_ChunkTopList.create(chunks));
		else if (icommandsender instanceof DedicatedServer){
			icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("[DIM X Z] Time NTEs"));
			for (ChunkStats stat : chunks){
				icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(stat.toString()));
			}
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
		return "Shows the 20 slowest chunks, in respect to tile entities.";
	}	

}
