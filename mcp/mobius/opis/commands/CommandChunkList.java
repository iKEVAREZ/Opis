package mcp.mobius.opis.commands;

import java.util.ArrayList;

import mcp.mobius.opis.data.ChunkManager;
import mcp.mobius.opis.data.holders.ChunkStats;
import mcp.mobius.opis.network.Packet_ChunkTopList;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandChunkList extends CommandBase {

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
		
		((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(Packet_ChunkTopList.create(chunks));		
		
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }	

	@Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(((EntityPlayerMP)sender).username);
    }	

}
