package mcp.mobius.opis.commands.server;

import java.util.ArrayList;

import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.OpisPacketHandler;
import mcp.mobius.opis.network.enums.DataReq;
import mcp.mobius.opis.network.server.Packet_DataList;
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
		ArrayList<StatsTileEntity> tes = new ArrayList<StatsTileEntity>(); 
		if (astring.length == 0){
			tes = TileEntityManager.getTopEntities(20);
		}
		else{
			try{
				tes = TileEntityManager.getTopEntities(Integer.valueOf(astring[0]));
			} catch (Exception e) {return;}
		}
		
		if (icommandsender instanceof EntityPlayer)		
			OpisPacketHandler.validateAndSend(Packet_DataList.create(DataReq.LIST_TIMING_TILEENTS,  tes), (Player)icommandsender);
		else{
			for (StatsTileEntity stat : tes){
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
		if ((sender instanceof EntityPlayerMP) && ((EntityPlayerMP)sender).playerNetServerHandler.netManager instanceof MemoryConnection) return true;
		if (!(sender instanceof DedicatedServer) && !(sender instanceof EntityPlayerMP)) return true;
		return PlayerTracker.instance().isPrivileged(((EntityPlayerMP)sender).username);
    }			
	
	@Override
	public String getDescription() {
		return "Returns the 20 longest TEs to update.";
	}	
	
}
