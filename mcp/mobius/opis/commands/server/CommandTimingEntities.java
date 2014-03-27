package mcp.mobius.opis.commands.server;

import java.util.ArrayList;
import java.util.HashMap;

import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
import mcp.mobius.opis.data.managers.EntityManager;
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

public class CommandTimingEntities extends CommandBase implements IOpisCommand {

	@Override
	public String getCommandName() {
		return "opis_ent";
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
		
		ArrayList<StatsEntity> ents = new ArrayList<StatsEntity>(); 
		if (astring.length == 0){
			ents = EntityManager.getTopEntities(20);
		}
		else{
			try{
				ents = EntityManager.getTopEntities(Integer.valueOf(astring[0]));
			} catch (Exception e) {return;}
		}
		
		icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("[DIM X Z] Time NTEs"));
		for (StatsEntity stat : ents){
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
		return "Returns the 20 longest entities to update.";
	}

}
