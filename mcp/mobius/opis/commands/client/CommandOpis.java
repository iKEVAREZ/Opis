package mcp.mobius.opis.commands.client;

import javax.swing.JFrame;
import javax.swing.JLabel;

import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.network.DataReqHandler;
import mcp.mobius.opis.network.enums.ClientCommand;
import mcp.mobius.opis.network.server.Packet_ClientCommand;
import mcp.mobius.opis.server.PlayerTracker;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandOpis extends CommandBase {

	@Override
	public String getCommandName() {
		return "opis";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		PlayerTracker.instance().playersSwing.add((Player)icommandsender);
		PlayerTracker.instance().playersOpis.add((Player)icommandsender);
		((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(Packet_ClientCommand.create(ClientCommand.SHOW_SWING));
	}
	
	@Override
    public int getRequiredPermissionLevel(){
        return 0;
    }	

	@Override
    public boolean canCommandSenderUseCommand(ICommandSender sender){
		return true;
    }	
}
