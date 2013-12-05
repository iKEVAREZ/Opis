package mcp.mobius.opis.commands;

import java.util.ArrayList;
import java.util.HashMap;

import mcp.mobius.opis.data.EntityManager;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.data.holders.EntityStats;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

public class CommandEntities extends CommandBase {

	@Override
	public String getCommandName() {
		return "opis_ent";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		double totalTime = 0;
		System.out.printf("=====\n");
		/*
		for (Integer index : EntityManager.stats.keySet()){
			//System.out.printf("%s\n", EntityManager.stats.get(index));
			totalTime += EntityManager.stats.get(index).getGeometricMean();
		}
		*/

		/*
		for (EntityStats stats : EntityManager.getTopEntities(20)){
			System.out.printf("%s\n", stats);
		}		
		*/
		
		//System.out.printf("Total entity update time : %.3f \u00B5s\n", totalTime);
		
		HashMap<CoordinatesChunk, ArrayList<EntityStats>> entities = EntityManager.getEntitiesPerChunkInDim(0);
		for (CoordinatesChunk chunk : entities.keySet()){
			System.out.printf("%s %s\n", chunk, entities.get(chunk).size());
			for (EntityStats stats : entities.get(chunk)){
				System.out.printf("\t%s\n", stats.getName());
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
		if (sender instanceof DedicatedServer) return false;
		if (((EntityPlayerMP)sender).playerNetServerHandler.netManager instanceof MemoryConnection) return true;		
        return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(((EntityPlayerMP)sender).username);
    }

}
