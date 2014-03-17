package mcp.mobius.opis.commands.client;

import javax.swing.JFrame;
import javax.swing.JLabel;

import mcp.mobius.opis.network.ClientCommand;
import mcp.mobius.opis.network.server.Packet_ClientCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
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
