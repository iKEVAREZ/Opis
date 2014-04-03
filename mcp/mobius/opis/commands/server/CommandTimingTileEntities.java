package mcp.mobius.opis.commands.server;

import java.util.ArrayList;

import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.newtypes.DataTileEntity;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.data.managers.TileEntityManager;
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

public class CommandTimingTileEntities extends CommandBase implements IOpisCommand {

	@Override
	public String getCommandName() {
		return "opis_te";
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
		
		ArrayList<DataTileEntity> tes = new ArrayList<DataTileEntity>(); 
		if (astring.length == 0){
			tes = TileEntityManager.INSTANCE.getWorses(20);
		}
		else{
			try{
				tes = TileEntityManager.INSTANCE.getWorses(Integer.valueOf(astring[0]));
			} catch (Exception e) {return;}
		}
		
		for (DataTileEntity stat : tes){
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
		return "Returns the 20 longest TEs to update.";
	}	
	
}
