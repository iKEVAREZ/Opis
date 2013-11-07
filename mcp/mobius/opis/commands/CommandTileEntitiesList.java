package mcp.mobius.opis.commands;

import java.util.ArrayList;

import mcp.mobius.opis.data.TileEntityManager;
import mcp.mobius.opis.data.holders.TileEntityStats;
import mcp.mobius.opis.network.Packet_TileEntitiesTopList;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

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

		/*
		System.out.printf("=== ===\n");
		
		for (TileEntityStats stat : tes)
			System.out.printf("%s\n", stat);
		*/
		
		((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(Packet_TileEntitiesTopList.create(tes));
		
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }	

	@Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return true;
    }		
	
}
