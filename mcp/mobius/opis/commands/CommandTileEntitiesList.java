package mcp.mobius.opis.commands;

import java.util.ArrayList;

import mcp.mobius.opis.data.TileEntityManager;
import mcp.mobius.opis.data.holders.TileEntityStats;
import mcp.mobius.opis.network.server.Packet_TileEntitiesTopList;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

public class CommandTileEntitiesList extends CommandBase {

	@Override
	public String getCommandName() {
		return "opis_te";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		ArrayList<TileEntityStats> tes = new ArrayList<TileEntityStats>(); 
		if (astring.length == 0){
			tes = TileEntityManager.getTopEntities(20);
		}
		else{
			try{
				tes = TileEntityManager.getTopEntities(Integer.valueOf(astring[0]));
			} catch (Exception e) {return;}
		}
		
		((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(Packet_TileEntitiesTopList.create(tes));
		
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
